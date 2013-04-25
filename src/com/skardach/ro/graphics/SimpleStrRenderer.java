package com.skardach.ro.graphics;

import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import com.skardach.ro.resource.ResourceException;
import com.skardach.ro.resource.Texture;
import com.skardach.ro.resource.str.KeyFrame;
import com.skardach.ro.resource.str.KeyFrameType;
import com.skardach.ro.resource.str.Layer;
import com.skardach.ro.resource.str.Str;

/**
 * Simple implementation of rendering STR files. Most of the credit goes to
 * open-ragnarok project whose implementation of rendering was a guideline to
 * this one.
 * @author kardasan
 *
 */
public class SimpleStrRenderer implements Renderer {
	private static final float STR_ANGLE_TO_DEGREES = 2.8444f;
	private static final int NO_FRAME = -1;
	// OpenGL utilities
	GLU _glu = new GLU();
	// Object rendered
	Str _effect;
	// Rendering parameters
	Point3D _renderPosition;
	float _xRotation;
	float _yRotation;
	float _zRotation;
	float _xScale;
	float _yScale;
	float _zScale;
	// rendering helper variables
	long _timeRemainderSinceLastFrame = 0;
	int _lastRenderedFrame = NO_FRAME;
	/**
	 * Keeps track of which base frame should be a base for rendering for given
	 * layer. i.e. If current frame is 35 and on layer x there is a base frame
	 * of number 35, then _currentBaseFrameOnLayer[x] == 35. Later if current
	 * frame is 40 and there were no base frames on layer x with numbers
	 * between 36-40 then still _currentBaseFrameOnLayer[x] == 35.
	 */
	int _currentBaseFrameOnLayer[];
	/**
	 * Keeps track of which animation frame should be applied when rendering
	 * given layer. The idea behind this table is the same as with
	 * _currentBaseFrameOnLayer but instead of base frames, it keeps track of
	 * animation frames.
	 */
	int _currentAnimationFrameOnLayer[];

	public SimpleStrRenderer(
			Str iEffect,
			Point3D iRenderPosition,
			float iXRotation,
			float iYRotation,
			float iZRotation,
			float iXScale,
			float iYScale,
			float iZScale) throws RenderException {
		assert(iEffect != null);
		if(iEffect == null)
			throw new RenderException("Effect cannot be null");
		_effect = iEffect;
		_renderPosition = iRenderPosition;
		_xRotation = iXRotation;
		_yRotation = iYRotation;
		_zRotation = iZRotation;
		// FIXME: -iXScale prevents mirror image when looking from +z axis
		// I think that effect files are described as a reflection or should
		// be viewed from -z axis... Not sure, to be checked when integrated to
		// ro client.
		_xScale = -iXScale;
		_yScale = iYScale;
		_zScale = iZScale;
	}
	/**
	 * Render a single frame. Preserves current matrix from being overwritten.
	 */
	@Override
	public void renderFrame(
			GLAutoDrawable ioCanvas,
			long iDelaySinceLastInvoke) throws RenderException {
		GL2 gl = ioCanvas.getGL().getGL2();
		beforeRender(gl);
		render(gl, iDelaySinceLastInvoke);
		afterRender(gl);
	}
	/**
	 * Preserve current processing matrix and move rendering according to
	 * renderer settings.
	 * @param iGL GL context
	 */
	private void beforeRender(GL2 iGL) {
		iGL.glPushMatrix();
		iGL.glTranslatef(
			_renderPosition._x,
			_renderPosition._y,
			_renderPosition._z);
		iGL.glRotatef(-_xRotation, 1, 0, 0);
		iGL.glRotatef(-_zRotation, 0, 0, 1);
		iGL.glRotatef(-_yRotation, 0, 1, 0);
		iGL.glScalef(_xScale, _yScale, _zScale);
		iGL.glEnable(GL.GL_TEXTURE_2D);
	}
	/**
	 * Restore original matrix.
	 * @param iGL GL context
	 */
	private void afterRender(GL2 iGL) {
		iGL.glDisable(GL.GL_TEXTURE_2D);
		iGL.glPopMatrix();
	}
	/**
	 * Main rendering method. Iterates through each layer and renders it.
	 * @param iGL
	 * @param iDelaySinceLastInvoke Time (in ms) since this method was last
	 * invoked. Required for calculating animation deltas and potential new
	 * key frames to apply
	 * @throws RenderException If something goes wrong with rendering (GL
	 * error, improper texture, etc.).
	 */
	private void render(
			GL2 iGL,
			long iDelaySinceLastInvoke) throws RenderException {
		// few assertion to be sure we're sane
		assert(_effect != null);
		assert(_effect.get_fps() > 0);
		// Check which frame should we render
		int frameToRender = calculateFrameToRender(iDelaySinceLastInvoke);
		int i = 0;
		for(Layer l : _effect.get_layers()) {
			renderLayer(
				i,
				l,
				frameToRender,
				iGL);
			i++;
		}
		_lastRenderedFrame = frameToRender;
	}
	/**
	 * Calculate which frame to render next. FPS in the effect files are crap
	 * so assume 30 FPS as the original client.
	 * @param iDelaySinceLastInvoke
	 * @return
	 */
	public int calculateFrameToRender(long iDelaySinceLastInvoke) {
		int frameToRender = 0;
		if(_lastRenderedFrame != NO_FRAME) { // always start with first frame
			frameToRender = // TODO: Original client doesn't care about FPS?
				_lastRenderedFrame // last frame rendered
				+ (int)(( // + delay since last frame / time for single frame
					iDelaySinceLastInvoke + _timeRemainderSinceLastFrame)
					/ (1000/30)); // 1s / 30 FPS
			_timeRemainderSinceLastFrame =
					(long) ((iDelaySinceLastInvoke + _timeRemainderSinceLastFrame)
					% (1000/30));
		}
		if(frameToRender >= _effect.get_frameCount()) {
			// Since we've made an animation loop first reset layer counters
			resetCurrentFrameTables();
			frameToRender %= _effect.get_frameCount();
		}
		return frameToRender;
	}
	/**
	 * Render a single layer. Texture and location data are taken from a current
	 * base key frame and then a current animation frame transformations are
	 * applied (given that animation frame has been reached).
	 * @param iLayerNumber Layer number in the effect stack (decides ordering
	 * on Z axis)
	 * @param iLayer layer object
	 * @param iFrameToRender which frame should be rendered (used to calculate
	 * current base and animation frames to use).
	 * @param iGL GL context
	 * @throws RenderException If anything goes wrong with rendering.
	 */
	private void renderLayer(
			int iLayerNumber,
			Layer iLayer,
			int iFrameToRender,
			GL2 iGL) throws RenderException {
		updateProcessedKeyFrames(
			iLayerNumber,
			iLayer.get_keyFrames(),
			iFrameToRender);

		if (_currentBaseFrameOnLayer[iLayerNumber] != NO_FRAME) {
			// We have a base frame to work on...
			float currentcolor[] = new float[4];
			iGL.glGetFloatv(GL2.GL_CURRENT_COLOR, currentcolor, 0);
			KeyFrame baseFrame =
				iLayer.get_keyFrames().get(
					_currentBaseFrameOnLayer[iLayerNumber]);
			// if alpha is more than 0, something is visible so let's draw
			if (baseFrame.get_color()._alpha > 0) {
				Color finalColor = new Color(baseFrame.get_color());
				Point2D finalPosition =
						new Point2D(// FIXME: openro: Why 320 and 290?
							baseFrame.get_position()._x - 320,
							baseFrame.get_position()._y - 290);
				float finalRotation =
						baseFrame.get_rotation() / STR_ANGLE_TO_DEGREES;
				Rectangle<Point2D> finalRectangle =
					new Rectangle<Point2D>(
						new Point2D(baseFrame.get_drawingRectangle().get_a()),
						new Point2D(baseFrame.get_drawingRectangle().get_b()),
						new Point2D(baseFrame.get_drawingRectangle().get_c()),
						new Point2D(baseFrame.get_drawingRectangle().get_d()));
				Rectangle<Point2D> finalTextureMapping =
					new Rectangle<Point2D>(
						new Point2D(baseFrame.get_textureUVMapping().get_a()),
						new Point2D(baseFrame.get_textureUVMapping().get_b()),
						new Point2D(baseFrame.get_textureUVMapping().get_c()),
						new Point2D(baseFrame.get_textureUVMapping().get_d()));
				Texture texture =
					iLayer.get_textures().get((int)baseFrame.get_textureId());

				if (_currentAnimationFrameOnLayer[iLayerNumber] != NO_FRAME) {
					KeyFrame animationFrame =
						iLayer.get_keyFrames().get(
							_currentAnimationFrameOnLayer[iLayerNumber]);
					applyAnimationFrame(animationFrame, iFrameToRender,
							finalColor, finalPosition, finalRotation,
							finalRectangle, finalTextureMapping);
				}
				iGL.glPushMatrix();

				Billboard(iGL);

				iGL.glColor4ub(
					(byte)finalColor._r,
					(byte)finalColor._g,
					(byte)finalColor._b,
					(byte)finalColor._alpha);
				iGL.glTranslatef(
					finalPosition._x,
					finalPosition._y,
					-0.5f); // 50 layers max?
				iGL.glRotatef(finalRotation, 0, 0, 1);

				iGL.glBlendFunc(
						baseFrame.get_sourceBlend().toGLValue(),
						baseFrame.get_destBlend().toGLValue());

				iGL.glEnable(GL.GL_BLEND);
				iGL.glAlphaFunc(GL.GL_GREATER, 0.5f);
				iGL.glEnable(GL2.GL_ALPHA_TEST);
				iGL.glEnable(GL.GL_TEXTURE_2D);

				if(!texture.isLoaded())
					try {
						texture.load(iGL);
					} catch (ResourceException e) {
						throw new RenderException(
							"Could not load texture: "
							+ texture
							+ ". Reason: "
							+ e);
					}
				texture.bind(iGL);

				iGL.glColorMask(true, true, true, false);

				iGL.glBegin(GL2.GL_QUADS);
				iGL.glTexCoord2f(
					finalTextureMapping._d._x,
					finalTextureMapping._d._y);
				iGL.glVertex3f(
					finalRectangle._c._x,
					finalRectangle._c._y,
					0.01f * iLayerNumber);

				iGL.glTexCoord2f(
					finalTextureMapping._c._x,
					finalTextureMapping._c._y);
				iGL.glVertex3f(
					finalRectangle._d._x,
					finalRectangle._d._y,
					0.01f * iLayerNumber);

				iGL.glTexCoord2f(
					finalTextureMapping._a._x,
					finalTextureMapping._a._y);
				iGL.glVertex3f(
					finalRectangle._a._x,
					finalRectangle._a._y,
					0.01f * iLayerNumber);

				iGL.glTexCoord2f(
					finalTextureMapping._b._x,
					finalTextureMapping._b._y);
				iGL.glVertex3f(
					finalRectangle._b._x,
					finalRectangle._b._y,
					0.01f * iLayerNumber);
				iGL.glEnd();

				iGL.glColorMask(true, true, true, true);
				iGL.glDisable(GL2.GL_ALPHA_TEST);
				iGL.glDisable(GL.GL_TEXTURE_2D);
				iGL.glDisable(GL.GL_BLEND);
				iGL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
				iGL.glColor4f(
					currentcolor[0],
					currentcolor[1],
					currentcolor[2],
					currentcolor[3]);
				iGL.glPopMatrix();
			}
		}
	}
	/**
	 * Make the effect face us.
	 * @param iGL GL context
	 */
	private void Billboard(GL2 iGL) {
		// Cheat Cylindrical. Credits:
		// http://www.lighthouse3d.com/opengl/billboarding/index.php3?billCheat1
		float modelview[] = new float[16];
		iGL.glGetFloatv(GL2.GL_MODELVIEW_MATRIX , modelview, 0);
		float v;
		for( int i=0; i<3; i+=2 )
		    for( int j=0; j<3; j++ ) {
				if ( i==j ) {
					if (i == 0)
						v = _xScale;
					else if (i == 1)
						v = _yScale;
					else
						v = 1.0f;
					modelview[i*4+j] = v;
				}
				else {
					modelview[i*4+j] = 0.0f;
				}
		    }
		iGL.glLoadMatrixf(modelview,0);
	}
	/**
	 * Applies animation frame modifications unto given set of drawing
	 * parameters.
	 * @param iAnimationFrame Animation frame to apply
	 * @param iFrameToRender current rendered frame (used to calculate
	 * animation intensity)
	 * @param ioFinalColor Base color to modify
	 * @param ioFinalPosition Position to modify
	 * @param ioFinalRotation Rotation of the texture to modify
	 * @param ioFinalRectangle Texture rectangle to modify
	 * @param ioFinalTextureMapping Texture mapping to modify
	 */
	private void applyAnimationFrame(KeyFrame iAnimationFrame,
			int iFrameToRender, Color ioFinalColor, Point2D ioFinalPosition,
			float ioFinalRotation, Rectangle<Point2D> ioFinalRectangle,
			Rectangle<Point2D> ioFinalTextureMapping) {
		int anifactor =
			iFrameToRender - iAnimationFrame.get_framenum();
		ioFinalColor._r += iAnimationFrame.get_color()._r * anifactor;
		ioFinalColor._g += iAnimationFrame.get_color()._g * anifactor;
		ioFinalColor._b += iAnimationFrame.get_color()._b * anifactor;
		ioFinalColor._alpha +=
			iAnimationFrame.get_color()._alpha * anifactor;
		ioFinalPosition._x +=
			iAnimationFrame.get_position()._x * anifactor;
		ioFinalPosition._y +=
				iAnimationFrame.get_position()._y * anifactor;
		ioFinalRotation +=
			(iAnimationFrame.get_rotation() / STR_ANGLE_TO_DEGREES)
			* anifactor;
		ioFinalRectangle._a._x +=
				iAnimationFrame.get_drawingRectangle()._a._x
				* anifactor;
		ioFinalRectangle._a._y +=
				iAnimationFrame.get_drawingRectangle()._a._y
				* anifactor;
		ioFinalRectangle._b._x +=
				iAnimationFrame.get_drawingRectangle()._b._x
				* anifactor;
		ioFinalRectangle._b._y +=
				iAnimationFrame.get_drawingRectangle()._b._y
				* anifactor;
		ioFinalRectangle._c._x +=
				iAnimationFrame.get_drawingRectangle()._c._x
				* anifactor;
		ioFinalRectangle._c._y +=
				iAnimationFrame.get_drawingRectangle()._c._y
				* anifactor;
		ioFinalRectangle._d._x +=
				iAnimationFrame.get_drawingRectangle()._d._x
				* anifactor;
		ioFinalRectangle._d._y +=
				iAnimationFrame.get_drawingRectangle()._d._y
				* anifactor;

		ioFinalTextureMapping._a._x +=
				iAnimationFrame.get_textureUVMapping()._a._x
				* anifactor;
		ioFinalTextureMapping._a._y +=
				iAnimationFrame.get_textureUVMapping()._b._y
				* anifactor;
		ioFinalTextureMapping._b._x +=
				iAnimationFrame.get_textureUVMapping()._b._x
				* anifactor;
		ioFinalTextureMapping._b._y +=
				iAnimationFrame.get_textureUVMapping()._c._y
				* anifactor;
		ioFinalTextureMapping._c._x +=
				iAnimationFrame.get_textureUVMapping()._c._x
				* anifactor;
		ioFinalTextureMapping._c._y +=
				iAnimationFrame.get_textureUVMapping()._d._y
				* anifactor;
		ioFinalTextureMapping._d._x +=
				iAnimationFrame.get_textureUVMapping()._d._x
				* anifactor;
		ioFinalTextureMapping._d._y +=
				iAnimationFrame.get_textureUVMapping()._a._y
				* anifactor;
	}
	/**
	 * Updates indexes of currently processed key frames for a layer given that
	 * we're currently at frame number iFrameToRender.
	 * @param iLayerNumber Layer number
	 * @param iFrameToRender
	 * @throws ResourceException
	 */
	private void updateProcessedKeyFrames(
			int iLayerNumber,
			List<KeyFrame> iLayerFrames,
			int iFrameToRender) throws RenderException {
		// First check if current frame to render is a base or animation
		// frame on this layer.
		for(
				int frameIdx = 1 + // start looking next frame after...
					(_currentAnimationFrameOnLayer[iLayerNumber] != NO_FRAME ?
					// After current animation frame because it's always after
					// base frame.
						_currentAnimationFrameOnLayer[iLayerNumber]
					// Since there is no animation frame processed then base
					// frame will be the best place to start search
						: _currentBaseFrameOnLayer[iLayerNumber]);
				frameIdx < iLayerFrames.size() // look until the end...
					&& iFrameToRender // or we pass the current frame number
						<= iLayerFrames.get(frameIdx).get_framenum();
				frameIdx++) {
			KeyFrame kf = iLayerFrames.get(frameIdx);
			if(kf.get_framenum() == iFrameToRender) {
				if(kf.get_frameType() == KeyFrameType.BASIC) {
					// new base frame, use it and reset animation frame
					// since it should end
					_currentBaseFrameOnLayer[iLayerNumber] = frameIdx;
					_currentAnimationFrameOnLayer[iLayerNumber] = NO_FRAME;
					break;
				} else if (kf.get_frameType() == KeyFrameType.MORPH) {
					// We got a new animation frame so set it to apply
					// to the basic frame that will be processed.
					_currentAnimationFrameOnLayer[iLayerNumber] =
							frameIdx;
					break;
				} else {
					throw new RenderException(
						"Unknown frame type "
						+ kf.get_frameType()
						+ "cannot render");
				}
			}
		}
	}

	@Override
	public void initialize(GLAutoDrawable ioDrawable) {
		System.out.println("Initializing Renderer...");
		resetCurrentFrameTables();
	}
	/**
	 * Resets tables which indicate current processing frames per layer.
	 */
	private void resetCurrentFrameTables() {
		_currentBaseFrameOnLayer = new int[_effect.get_layers().size()];
		_currentAnimationFrameOnLayer = new int[_effect.get_layers().size()];
		for(int i = 0; i < _effect.get_layers().size(); i++) {
			_currentBaseFrameOnLayer[i] = NO_FRAME;
			_currentAnimationFrameOnLayer[i] = NO_FRAME;
		}
	}

	@Override
	public void dispose(GLAutoDrawable ioDrawable) {
		System.out.println("Disposing Renderer...");
	}

	@Override
	public void handleReshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// nothing to do.
	}
}

package com.skardach.ro.graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.util.FPSAnimator;
import com.skardach.ro.resource.ResourceException;

/**
 * OpenGL wrapper class which is used in business logic to separate it from the
 * rendering logic.
 *
 * @author Stanislaw Kardach
 *
 */
public class OpenGLWrapper {
	// JOGL classes
	GLProfile _profile;
	FPSAnimator _animator;
	GLU _glu;

	LinkedList<RendererHandler> _registeredRenderers =
		new LinkedList<RendererHandler>();

	// Settings. TODO: This should be configurable
	private static class Settings {
		public static final float CLIPPING_NEAR = 1.0f;
		public static final float CLIPPING_FAR = 1000.0f;
		public static final float PERSPECTIVE_ANGLE = 45.0f;
	}

	/**
	 * Handler for Canvas GL and keyboard events. Performs some scene setup,
	 * draws axis and tries to offer some navigation (a very crude support for
	 * scene navigation and rotation is buggy as hell right now).
	 *
	 * @author Stanislaw Kardach
	 *
	 */
	private class CanvasEventHandler implements GLEventListener, KeyListener {
		int _eyeX = 0;
		int _eyeY = 100;
		int _eyeZ = 600;
		int _centerX = 0;
		int _centerY = 100;
		int _centerZ = 0;

		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
		}

		@Override
		public void init(GLAutoDrawable drawable) {
			initOpenGLInCanvas(drawable);
		}

		@Override
		public void dispose(GLAutoDrawable drawable) {
			finalizeOpenGL(drawable);
		}

		@Override
		public void display(GLAutoDrawable drawable) {
			GL2 gl = drawable.getGL().getGL2();
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			setPerspective(
				drawable,
				Settings.PERSPECTIVE_ANGLE,
				Settings.CLIPPING_NEAR,
				Settings.CLIPPING_FAR,
				drawable.getWidth(),
				drawable.getHeight(),
				_eyeX,
				_eyeY,
				_eyeZ,
				_centerX,
				_centerY,
				_centerZ);
			drawAxis(drawable);
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case (KeyEvent.VK_W):
				_eyeY += 10;
				_centerY += 10;
				break;
			case (KeyEvent.VK_S):
				_eyeY -= 10;
				_centerY -= 10;
				break;
			case (KeyEvent.VK_A):
				_eyeX -= 10;
				_centerX -= 10;
				break;
			case (KeyEvent.VK_D):
				_eyeX += 10;
				_centerX += 10;
				break;
			case (KeyEvent.VK_Q):
				_eyeZ -= 10;
				break;
			case (KeyEvent.VK_E):
				_eyeZ += 10;
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
	}

	/**
	 * Used in JOGL drawing event calls. Calls renderer callbacks.
	 *
	 * @author Stanislaw Kardach
	 *
	 */
	private class RendererHandler implements GLEventListener {
		Renderer _renderer;
		long _lastDisplayInvoke = 0;

		public RendererHandler(Renderer iRenderer) {
			_renderer = iRenderer;
		}

		@Override
		public void display(GLAutoDrawable drawable) {
			long displayInvokeDelay = 0;
			long last = _lastDisplayInvoke;
			_lastDisplayInvoke = System.nanoTime() / 1000000; // get millisecond
			if (last != 0)
				displayInvokeDelay = _lastDisplayInvoke - last;
			try {
				_renderer.renderFrame(drawable, displayInvokeDelay);
			} catch (RenderException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void dispose(GLAutoDrawable drawable) {
			_renderer.dispose(drawable);
		}

		@Override
		public void init(GLAutoDrawable drawable) {
			try {
				_renderer.initialize(drawable);
			} catch(ResourceException e) {
				System.err.println(
					"Unable to initialize renderer: "
					+ e.getLocalizedMessage());
			}
		}

		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
			_renderer.handleReshape(drawable, x, y, width, height);
		}

		public void reset() {
			_renderer.reset();
			resetTimer();
		}

		public void resetTimer() {
			_lastDisplayInvoke = 0;
		}
	}

	/**
	 * Set viewing perspective on given drawable. Required for STRViewer. If
	 * reusing rendering code for particular renderers, this will probably not
	 * be required.
	 *
	 * @param ioDrawable
	 *            target drawable
	 * @param iAngle
	 *            angle in degrees
	 * @param iClipNear
	 *            near clipping border (for frustum)
	 * @param oClipFar
	 *            far clipping border (for frustum)
	 * @param iWidth
	 *            width of the drawing field
	 * @param iHeight
	 *            height of the drawing field
	 */
	private void setPerspective(GLAutoDrawable ioDrawable, float iAngle,
			float iClipNear, float oClipFar, int iWidth, int iHeight,
			int iEyeX, int iEyeY, int iEyeZ, int iCenterX, int iCenterY,
			int iCenterZ) {
		GL2 gl = ioDrawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		float ratio = (float) iWidth / (float) iHeight;
		_glu.gluPerspective(iAngle, ratio, iClipNear, oClipFar);
		_glu.gluLookAt(iEyeX, iEyeY, iEyeZ, iCenterX, iCenterY, iCenterZ, 0,
				1, 0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	/**
	 * Creates an OpenGL wrapper for a given profile and creates an animator
	 * to use in all canvas created by this wrapper.
	 * @param iGraphicsProfile OpenGL profile to use. If null then
	 * GLProfile.getDefault() is used.
	 * @param iFps FPS of the animator used for Canvas created by this wrapper.
	 */
	public OpenGLWrapper(GLProfile iGraphicsProfile, int iFps) {
		if (iGraphicsProfile != null)
			_profile = iGraphicsProfile;
		else
			_profile = GLProfile.getDefault();
		_animator = new FPSAnimator(iFps);
	}

	/**
	 * Creates a GLCanvas and attaches it to the internal animator
	 *
	 * @return canvas ready to draw on and with animator.
	 */
	public GLCanvas createGLCanvasWithAnimator() {
		GLCapabilities capabilities = new GLCapabilities(_profile);
		capabilities.setHardwareAccelerated(true);
		capabilities.setDoubleBuffered(true);
		GLCanvas result = new GLCanvas(capabilities);
		CanvasEventHandler ceh = new CanvasEventHandler();
		result.addGLEventListener(ceh);
		result.addKeyListener(ceh);
		_animator.add(result);
		return result;
	}

	/**
	 * Performs destruction of a canvas and removes it from animator list.
	 *
	 * @param ioCanvas
	 */
	public void destroyCanvas(GLCanvas ioCanvas) {
		_animator.remove(ioCanvas);
		ioCanvas.destroy();
	}
	/**
	 * Global initialisation of OpenGL. Calls
	 * {@link GLProfile#initSingleton(boolean)}.
	 */
	public static void initOpenGL() {
		GLProfile.initSingleton(true);
	}
	/**
	 * Initialize OpenGL in Canvas. Required for STRViewer. If reusing rendering
	 * code for particular renderers, this should not be required.
	 * It is important to enable depth buffer, otherwise texture blending will
	 * not work.
	 */
	private void initOpenGLInCanvas(GLAutoDrawable ioCanvas) {
		_glu = new GLU();
		GL2 gl = ioCanvas.getGL().getGL2();
		ioCanvas.setGL(new DebugGL2(gl));
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glDepthMask(true);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
		gl.glClearDepth(1.0f);
	}

	/**
	 * Clean up after OpenGL in canvas. Required for STRViewer. If reusing
	 * rendering code for particular renderers, this should not be required.
	 */
	private void finalizeOpenGL(GLAutoDrawable ioCanvas) {
	}

	/**
	 * Fire up the animator.
	 */
	public void startAnimation() {
		_animator.start();
	}

	/**
	 * Stop the animator.
	 */
	public void stopAnimation() {
		_animator.stop();
		for(RendererHandler r : _registeredRenderers) {
			r.resetTimer();
		}
	}
	/**
	 * Reset animation state.
	 */
	public void resetAnimation() {
		for(RendererHandler r : _registeredRenderers) {
			r.reset();
		}
	}

	/**
	 * Add given renderer to the rendering pipeline on a given canvas.
	 *
	 * @param iRenderer Renderer to add
	 * @param ioCanvas GLCanvas to register renderer to
	 */
	public void registerRendererOnCanvas(Renderer iRenderer, GLCanvas ioCanvas) {
		if (iRenderer != null && ioCanvas != null) {
			RendererHandler rh = new RendererHandler(iRenderer);
			ioCanvas.addGLEventListener(rh);
			_registeredRenderers.add(rh);
		}
	}

	/**
	 * Draws axis to have some reference for the view.
	 *
	 * @param drawable
	 */
	public void drawAxis(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		// save environment
		gl.glPushMatrix();
		gl.glLoadIdentity();
		float currentcolor[] = new float[4];
		gl.glGetFloatv(GL2.GL_CURRENT_COLOR, currentcolor, 0);
		// draw axis
		gl.glLineWidth(1f);
		gl.glColor3d(255, 0, 0);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3i(0, 0, 0);
		gl.glVertex3i(100, 0, 0);
		gl.glEnd();
		gl.glColor3d(0, 255, 0);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3i(0, 0, 0);
		gl.glVertex3i(0, 100, 0);
		gl.glEnd();
		gl.glColor3d(0, 0, 255);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3i(0, 0, 0);
		gl.glVertex3i(0, 0, 100);
		gl.glEnd();
		// restore environment
		gl.glColor4f(currentcolor[0], currentcolor[1], currentcolor[2],
				currentcolor[3]);
		gl.glPopMatrix();
	}

	/**
	 * Create wrapper with a desktop profile.
	 * @param iFps FPS for animator used on canvas created by this wrapper
	 */
	public static OpenGLWrapper createDesktopWrapper(int iFps) {
		return new OpenGLWrapper(GLProfile.get(GLProfile.GL2), iFps);
	}

	/**
	 * Create wrapper with mobile profile
	 * @param iFps FPS for animator used on canvas created by this wrapper
	 */
	public static OpenGLWrapper createMobileWrapper(int iFps) {
		return new OpenGLWrapper(GLProfile.getGL2ES1(), iFps);
	}
}
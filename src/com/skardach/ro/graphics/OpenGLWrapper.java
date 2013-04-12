package com.skardach.ro.graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.FPSAnimator;

public class OpenGLWrapper {
	// JOGL classes
	GLProfile _profile;
	FPSAnimator _animator;
	GLU _glu;

	// Settings. TODO: This should be configurable
	private static class Settings {
		public static final float CLIPPING_NEAR = 1.0f;
		public static final float CLIPPING_FAR  = 1000.0f;
		public static final float PERSPECTIVE_ANGLE = 45.0f;
		public static final int ANIMATOR_FPS = 90;
	}

	class RendererHandler implements GLEventListener, KeyListener {
		Renderer _renderer;
		long _displayInvokeDelay = 0;
		long _lastDisplayInvoke = 0;
		
		int _eyeX = 0;
		int _eyeY = 0;
		int _eyeZ = 700;
		int _centerX = 0;
		int _centerY = 0;
		int _centerZ = 0;
		
		public RendererHandler(Renderer iRenderer) {
			_renderer = iRenderer;
		}
		@Override
		public void display(GLAutoDrawable drawable) {
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
			drawable.getGL().glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			drawAxis(drawable);
			long last = _lastDisplayInvoke;
			_lastDisplayInvoke = System.nanoTime()/1000000; // get millisecond
			if(last != 0)
				_displayInvokeDelay = _lastDisplayInvoke - last;
			try {
				_renderer.renderFrame(drawable, _displayInvokeDelay);
			} catch (RenderException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void dispose(GLAutoDrawable drawable) {
			_renderer.dispose(drawable);
			finalizeOpenGL(drawable);
		}
		@Override
		public void init(GLAutoDrawable drawable) {
			drawable.getGL().glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			initOpenGL(drawable);
			_renderer.initialize(drawable);
		}
		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
			_renderer.handleReshape(drawable, x, y, width, height);
		}
		@Override
		public void keyTyped(KeyEvent e) {
		}

		/**
		 * @param iX
		 * @param iY
		 * @return
		 */
		public double calculateCurrentAngle(int iX, int iY) {
			double currentAngle = Math.PI/2;
			if(iY == 0)
				iY = 1;
			currentAngle = Math.atan((double)iX/(double)iY);
			return currentAngle;
		}
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO: this is VERY crude...
			double deltaAngle = Math.PI/18.0; // 10 degrees
			switch(e.getKeyCode()) {
			case (KeyEvent.VK_LEFT):
				double currentAngle = calculateCurrentAngle(_eyeX, _eyeZ);
				double r = Math.sqrt(_eyeX*_eyeX + _eyeY*_eyeY);
				_eyeX = (int)(Math.sin(currentAngle-deltaAngle)*r);
				_eyeY = (int)(Math.cos(currentAngle-deltaAngle)*r);
				break;
			case (KeyEvent.VK_RIGHT):
				currentAngle = calculateCurrentAngle(_eyeX, _eyeZ);
				r = Math.sqrt(_eyeX*_eyeX + _eyeY*_eyeY);
				_eyeX = (int)(Math.sin(currentAngle+deltaAngle)*r);
				_eyeY = (int)(Math.cos(currentAngle+deltaAngle)*r);
				break;
			case (KeyEvent.VK_UP):
				currentAngle = calculateCurrentAngle(_eyeZ, _eyeY);
				r = Math.sqrt(_eyeZ*_eyeZ + _eyeY*_eyeY);
				_eyeZ = (int)(Math.sin(currentAngle-deltaAngle)*r);
				_eyeY = (int)(Math.cos(currentAngle-deltaAngle)*r);
				break;
			case (KeyEvent.VK_DOWN):
				currentAngle = calculateCurrentAngle(_eyeZ, _eyeY);
				r = Math.sqrt(_eyeZ*_eyeZ + _eyeY*_eyeY);
				_eyeZ = (int)(Math.sin(currentAngle+deltaAngle)*r);
				_eyeY = (int)(Math.cos(currentAngle+deltaAngle)*r);
				break;
			}
			
		}
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	/**
	 * Set viewing perspective on given drawable.
	 * Required for STRViewer.
	 * If reusing rendering code for particular renderers,
	 * this will probably not be required.
	 * @param ioDrawable target drawable
	 * @param iAngle angle in degrees
	 * @param iClipNear near clipping border (for frustum)
	 * @param oClipFar far clipping border (for frustum)
	 * @param iWidth width of the drawing field
	 * @param iHeight height of the drawing field
	 */
	private void setPerspective(
			GLAutoDrawable ioDrawable, 
			float iAngle,
			float iClipNear,
			float oClipFar,
			int iWidth,
			int iHeight,
			int iEyeX,
			int iEyeY,
			int iEyeZ,
			int iCenterX,
			int iCenterY,
			int iCenterZ) {
		GL2 gl = ioDrawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		float ratio = (float) iWidth / (float) iHeight;
		_glu.gluPerspective(
				iAngle,
				ratio,
				iClipNear,
				oClipFar);
		_glu.gluLookAt(
			iEyeX, 
			iEyeY, 
			iEyeZ, 
			iCenterX, 
			iCenterY, 
			iCenterZ, 
			0, 
			-1, 
			0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public OpenGLWrapper(GLProfile iGraphicsProfile) {
		if(iGraphicsProfile != null)
			_profile = iGraphicsProfile;
		else
			_profile = GLProfile.getDefault();
		_animator = new FPSAnimator(Settings.ANIMATOR_FPS);
	}

	/**
	 * Creates a GLCanvas and attaches it to the internal animator
	 * @return canvas ready to draw on and with animator.
	 */
	public GLCanvas createGLCanvasWithAnimator() {
		GLCapabilities capabilities = new GLCapabilities(_profile);
		capabilities.setHardwareAccelerated(true);
		capabilities.setDoubleBuffered(true);
		GLCanvas result = new GLCanvas(capabilities);
		_animator.add(result);
		return result;
	}

	/**
	 * Performs destruction of a canvas and removes it from
	 * animator list.
	 * @param ioCanvas
	 */
	public void destroyCanvas(GLCanvas ioCanvas) {
		_animator.remove(ioCanvas);
		ioCanvas.destroy();
	}
	
	/**
	 * Initialize OpenGL in Canvas. Required for STRViewer.
	 * If reusing rendering code for particular renderers,
	 * this should not be required.
	 */
	private void initOpenGL(GLAutoDrawable ioCanvas) {
		_glu = new GLU();
		System.out.println("INIT");
		GL2 gl = ioCanvas.getGL().getGL2();
		ioCanvas.setGL(new DebugGL2(gl));
	    gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
	    gl.glEnable(GL.GL_TEXTURE_2D);
	    gl.glShadeModel(GL2.GL_SMOOTH);
	    gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
	    gl.glClearDepth(1.0f);
	    gl.glEnable(GL.GL_DEPTH_TEST);
	}

	/**
	 * Clean up after OpenGL in canvas. Required for STRViewer.
	 * If reusing rendering code for particular renderers,
	 * this should not be required.
	 */
	private void finalizeOpenGL(GLAutoDrawable drawable) {
	}

	public void startAnimation() {
		_animator.start();
	}

	public void stopAnimation() {
		_animator.stop();
	}

	public void registerRendererOnCanvas(
			Renderer iRenderer, 
			GLCanvas ioCanvas) {
		if(iRenderer != null && ioCanvas != null) {
			RendererHandler rh = new RendererHandler(iRenderer);
			ioCanvas.addGLEventListener(rh);
			ioCanvas.addKeyListener(rh);
		}
	}

	/**
	 * @param drawable
	 * @throws GLException
	 */
	public void drawAxis(GLAutoDrawable drawable) throws GLException {
		GL2 gl = drawable.getGL().getGL2();
		// save environment
		float currentcolor[] = new float[4];
		gl.glGetFloatv(GL2.GL_CURRENT_COLOR, currentcolor, 0);
		// draw axis
		gl.glLineWidth(1f);
		gl.glColor3d(255, 0, 0);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3i(-100, 0, 0);
		gl.glVertex3i(100, 0, 0);
		gl.glEnd();
		gl.glColor3d(0, 255, 0);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3i(0,-100, 0);
		gl.glVertex3i(0, 100, 0);
		gl.glEnd();
		gl.glColor3d(0, 0, 255);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3i(0, 0,-100);
		gl.glVertex3i(0, 0, 100);
		gl.glEnd();
		// restore environment
		gl.glColor4f(
			currentcolor[0], 
			currentcolor[1],
			currentcolor[2],
			currentcolor[3]);
	}

	public static OpenGLWrapper createDesktopWrapper() {
		return new OpenGLWrapper(GLProfile.get(GLProfile.GL2));
	}
	
	public static OpenGLWrapper createMobileWrapper() {
		return new OpenGLWrapper(GLProfile.getGL2ES1());
	}
}
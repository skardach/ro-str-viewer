package com.skardach.ro.graphics;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.FPSAnimator;

public class OpenGLWrapper {
	// JOGL classes
	GLProfile _profile;
	FPSAnimator _animator;
	GLU _glu = new GLU();
	// Settings. TODO: This should be configurable
	private static class Settings {
		public static final float CLIPPING_NEAR = 1.0f;
		public static final float CLIPPING_FAR  = 1.0f;
		public static final float PERSPECTIVE_ANGLE = 45.0f;
	}
	
	class RendererInvoker implements GLEventListener {
		Renderer _renderer;
		
		public RendererInvoker(Renderer iRenderer) {
			_renderer = iRenderer;
		}
		@Override
		public void display(GLAutoDrawable drawable) {
			_renderer.renderFrame(drawable);
		}
		@Override
		public void dispose(GLAutoDrawable drawable) {
			_renderer.dispose(drawable);
			finalizeOpenGL(drawable);
		}
		@Override
		public void init(GLAutoDrawable drawable) {
			initOpenGL(drawable);
			_renderer.initialize(drawable);
		}
		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
			setPerspective(
				drawable, 
				Settings.PERSPECTIVE_ANGLE, 
				Settings.CLIPPING_NEAR,
				Settings.CLIPPING_FAR, 
				width, 
				height);
			_renderer.handleReshape(drawable, x, y, width, height);
		}
	}
	
	/**
	 * Set viewing perspective on given drawable.
	 * Required for STRViewer.
	 * If reusing rendering code for particular renderers,
	 * this should not be required.
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
			int iHeight) {
		GL2 gl = ioDrawable.getGL().getGL2();
	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
	    float ratio = (float) iWidth / (float) iHeight;
	    _glu.gluPerspective(
    		iAngle,
    		ratio,
    		iClipNear,
    		oClipFar);
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	public OpenGLWrapper(GLProfile iGraphicsProfile) {
		if(iGraphicsProfile != null)
			_profile = iGraphicsProfile;
		else
			_profile = GLProfile.getDefault();
		_animator = new FPSAnimator(FPSAnimator.DEFAULT_FRAMES_PER_INTERVAL);
	}

	/**
	 * Creates a GLCanvas and attaches it to the internal animator
	 * @return canvas ready to draw on and with animator.
	 */
	public GLCanvas createGLCanvasWithAnimator() {
		GLCapabilities capabilities = new GLCapabilities(_profile);
		GLCanvas result = new GLCanvas(capabilities);
		_animator.add(result);
		result.setAnimator(_animator);
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
		GL2 gl = ioCanvas.getGL().getGL2();
	    gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
	    gl.glEnable(GL.GL_TEXTURE_2D);
	    gl.glShadeModel(GL2.GL_SMOOTH);
	    gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
	    gl.glClearDepth(1.0f);
	    gl.glEnable(GL.GL_DEPTH_TEST);
	    setPerspective(
    		ioCanvas, 
    		Settings.PERSPECTIVE_ANGLE,
    		Settings.CLIPPING_NEAR,
    		Settings.CLIPPING_FAR,
    		ioCanvas.getWidth(), 
    		ioCanvas.getHeight());
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
			ioCanvas.addGLEventListener(new RendererInvoker(iRenderer));
		}		
	}
}
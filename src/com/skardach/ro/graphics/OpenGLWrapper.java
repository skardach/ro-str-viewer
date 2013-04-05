package com.skardach.ro.graphics;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;

public class OpenGLWrapper {
	GLProfile _profile;
	FPSAnimator _animator;
	
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
		}
		@Override
		public void init(GLAutoDrawable drawable) {
			_renderer.initialize(drawable);
		}
		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
			_renderer.handleReshape(drawable, x, y, width, height);
		}
		
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
	 * Initialize OpenGL. This should be done via separate method
	 * in case we want to defer the initialization.
	 */
	public void initOpenGL() {
		
	}

	/**
	 * Clean up after OpenGL
	 */
	public void finalizeOpenGL() {
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
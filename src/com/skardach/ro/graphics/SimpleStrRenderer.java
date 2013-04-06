package com.skardach.ro.graphics;

import javax.media.opengl.GLAutoDrawable;

import com.skardach.ro.resource.str.Str;

public class SimpleStrRenderer implements Renderer {
	Str _effect;

	public SimpleStrRenderer(Str iEffect) {
		_effect = iEffect;
	}

	@Override
	public void renderFrame(GLAutoDrawable ioCanvas) {
	}

	@Override
	public void initialize(GLAutoDrawable ioDrawable) {
		System.out.println("Initializing Renderer...");
		// Read everything for STR object
	}

	@Override
	public void dispose(GLAutoDrawable ioDrawable) {
		System.out.println("Disposing Renderer...");
	}

	@Override
	public void handleReshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

}

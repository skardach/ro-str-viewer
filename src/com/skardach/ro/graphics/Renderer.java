package com.skardach.ro.graphics;

import javax.media.opengl.GLAutoDrawable;

/**
 * Interface for a renderer object
 * @author Stanislaw Kardach
 *
 */
public interface Renderer {

	public void renderFrame(GLAutoDrawable ioDrawable);

	public void initialize(GLAutoDrawable ioDrawable);

	public void dispose(GLAutoDrawable ioDrawable);

	public void handleReshape(GLAutoDrawable drawable, int x, int y, int width,
			int height);

}

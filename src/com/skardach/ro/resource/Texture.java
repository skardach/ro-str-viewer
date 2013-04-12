package com.skardach.ro.resource;

import java.nio.Buffer;
import javax.media.opengl.GL2;

/**
 * Interface for accessing texture data.
 * @author Stanislaw Kardach
 *
 */
public interface Texture {
	/**
	 * Should return identifier of a texture. In RO this is
	 * a path of the texture relative to the directory containing
	 * rendered object.
	 */
	public String getName();
	/**
	 * Should return buffer with texture content of appropriate format.
	 * @throws ResourceException If the is any problem with 
	 * texture data. Implementations may choose to throw it i.e.
	 * when they are delaying the texture  load to the first
	 * access of texture data.
	 */
	public Buffer getData() throws ResourceException;
	/**
	 * Return texture width. Can be -1 if texture is not loaded.
	 */
	public int getWidth();
	/**
	 * Return texture height. Can be -1 if texture is not loaded,
	 * @return
	 */
	public int getHeight();
	/**
	 * Convenience method for printing out parsed data
	 * @param iPrefix Prefix to put before each line
	 * @return String representation of the texture.
	 */
	public String toString(String iPrefix);
	/**
	 * Load texture into memory in given OpenGL context.
	 * @param iGLContext Context to which texture will be registered.
	 * @throws ResourceException If something goes wrong, i.e. Data could not
	 * be located on the drive or registration failed. 
	 */
	public void load(GL2 iGLContext) throws ResourceException;
	/**
	 * Should release all resources taken by the texture, i.e. image data,
	 * OpenGL handles etc.
	 * @param iGLContext OpenGL context to use.
	 */
	public void unload(GL2 iGLContext);
	/**
	 * Return true if texture data is loaded and can be bound to GL context.
	 */
	public boolean isLoaded();
	/**
	 * Bind texture to given GL context
	 * @return True if bound successfully, false otherwise.
	 */
	public boolean bind(GL2 iGLContext);
}

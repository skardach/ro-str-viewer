package com.skardach.ro.graphics;

import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.skardach.ro.resource.ResourceException;
import com.skardach.ro.resource.Texture;
/**
 * Class describing a texture. Uses JOGL TextureIO toolkit to make things
 * easier.
 * @author Stanislaw Kardach
 *
 */
public class TextureImpl implements Texture {
	public static final int NAME_SIZE = 128;
	String _name;
	String _path;
	TextureData _data;
	com.jogamp.opengl.util.texture.Texture _joglTexture;
	Integer _GLName = null;
	
	public TextureImpl(String iName, String iBasePath) {
		_name = iName;
		_path = iBasePath;
	}
	
	@Override
	public String toString() {
		return toString("");
	}

	public String toString(String iPrefix) {
		return iPrefix 
			+ "Texture [_name=" 
			+ _name
			+ ", _path="
			+ _path
			+ ", _GLName=" 
			+ _GLName
			+ "]";
	}

	@Override
	public String getName() {
		return _path;
	}

	@Override
	public synchronized Buffer getData() throws ResourceException {
		return isLoaded() ? _data.getBuffer() : null;
	}

	@Override
	public synchronized int getWidth() {
		return _data != null ? _data.getWidth() : -1;
	}

	@Override
	public synchronized int getHeight() {
		return _data != null ? _data.getHeight() : -1;
	}

	@Override
	public synchronized void load(GL2 iGLContext) throws ResourceException {
		if(isLoaded())
			return;
		int idx = _name.lastIndexOf('.');
		if(idx < 0)
			throw new ResourceException(
				"Unsupported texture extension: " + _name);
		String extension = _name.substring(idx);
		try {
			// Read the image
			_data = TextureIO.newTextureData(
				iGLContext.getGLProfile(), 
				new File(_path, _name), 
				false, 
				extension);
			// create the texture
			_joglTexture = TextureIO.newTexture(_data);
			// set some parameters
			_joglTexture.setTexParameterf(
				iGLContext, 
				GL.GL_TEXTURE_WRAP_S, 
				GL.GL_REPEAT);
			_joglTexture.setTexParameterf(
				iGLContext, 
				GL.GL_TEXTURE_WRAP_T, 
				GL.GL_REPEAT);
			_joglTexture.setTexParameterf(
				iGLContext, 
				GL.GL_TEXTURE_MAG_FILTER,
				GL.GL_LINEAR);
			iGLContext.glTexEnvf(
				GL2.GL_TEXTURE_ENV, 
				GL2.GL_TEXTURE_ENV_MODE, 
				GL2.GL_MODULATE);
		} catch (IOException e) {
			throw new ResourceException(
				"Error loading texture from file: " 
				+ _path 
				+ File.pathSeparator 
				+ _name 
				+ ". Reason: " 
				+ e);
		}
	}

	@Override
	public synchronized void unload(GL2 iGLContext) {
		if(!isLoaded())
			return;
		_joglTexture.destroy(iGLContext);
		_data.destroy();
		_joglTexture = null;
		_data = null;
	}

	@Override
	public synchronized boolean isLoaded() {
		return _joglTexture != null;
	}

	@Override
	public boolean bind(GL2 iGLContext) {
		if(!isLoaded())
			return false;
		_joglTexture.bind(iGLContext);
		return true;
	}
}

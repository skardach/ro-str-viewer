package com.skardach.ro.resource.str;

import java.util.LinkedList;
import java.util.List;

import com.skardach.ro.graphics.Texture;

/**
 * Animation layer
 * @author Stanislaw Kardach
 *
 */
public class Layer {
	/*
	 * open-ragnarok implementation was using malloc to allocate
	 * space for array of textures and key frames. Here we don't
	 * care about memory allocation so the only limit is the
	 * max value of 32 bit unsigned integer.
	 */
	public static final int MAX_TEXTURE_COUNT = Integer.MAX_VALUE;
	public static final int MAX_KEYFRAME_COUNT = Integer.MAX_VALUE;
	LinkedList<Texture> _textures = new LinkedList<Texture>();
	LinkedList<KeyFrame> _keyFrames = new LinkedList<KeyFrame>();
	
	public List<Texture> get_textures() {
		return _textures;
	}
	public List<KeyFrame> get_keyFrames() {
		return _keyFrames;
	}
	
	@Override
	public String toString() {
		return toString("");
	}
	public String toString(String iPrefix) {
		String result = iPrefix + "Layer [\n"
			+ iPrefix + "  _textures=[\n";
		for(Texture t : _textures)
		{
			result += t.toString(iPrefix + "    ") + "\n";
		}
		result += iPrefix + "  ],\n"
			+ iPrefix + "  _keyFrames=[\n";
		for(KeyFrame kf : _keyFrames)
		{
			result += kf.toString(iPrefix + "    ") + "\n";
		}
		result += iPrefix + "  ]\n";
		result += iPrefix + "]";
		return result;
	}
}

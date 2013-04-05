package com.skardach.ro.graphics;

/**
 * Class describing a texture.
 * There probably should be a method for loading texture when needed.
 * @author kardach
 *
 */
public class Texture {
	public static final int NAME_SIZE = 128;
	String _name;
	public Texture(String iName) {
		_name = iName;
	}
	
	@Override
	public String toString() {
		return toString("");
	}

	public String toString(String iPrefix) {
		return iPrefix + "Texture [_name=" + _name + "]";
	}
}

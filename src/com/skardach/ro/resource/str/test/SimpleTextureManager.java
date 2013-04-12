package com.skardach.ro.resource.str.test;

import java.util.HashMap;

import com.skardach.ro.graphics.TextureImpl;
import com.skardach.ro.resource.Texture;
import com.skardach.ro.resource.TextureManager;

public class SimpleTextureManager implements TextureManager {
	String _textureBaseDir;
	HashMap<String, Texture> _textures = new HashMap<String, Texture>();
	
	public SimpleTextureManager(String iBasePath) {
		_textureBaseDir = iBasePath;
	}

	@Override
	public Texture getTexture(String iTextureName) {
		Texture result = null;
		if(iTextureName != null)
		{
			result = _textures.get(iTextureName);
			if(result == null)
			{
				result = new TextureImpl(iTextureName, _textureBaseDir);
				_textures.put(iTextureName, result);
			}
		}
		return result;
	}
}

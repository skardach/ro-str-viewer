package com.skardach.ro.str.test;

import java.util.HashMap;

import com.skardach.ro.TextureManager;
import com.skardach.ro.str.Texture;

public class SimpleTextureManager implements TextureManager {
	HashMap<String, Texture> _textures = new HashMap<String, Texture>();

	@Override
	public Texture getTexture(String iTextureName) {
		Texture result = null;
		if(iTextureName != null)
		{
			result = _textures.get(iTextureName);
			if(result == null)
			{
				result = new Texture(iTextureName);
				_textures.put(iTextureName, result);
			}
		}
		return result;
	}

}

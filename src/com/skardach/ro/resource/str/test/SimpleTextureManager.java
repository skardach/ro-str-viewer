package com.skardach.ro.resource.str.test;

import java.util.HashMap;

import com.skardach.ro.graphics.Texture;
import com.skardach.ro.resource.TextureManager;

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

package com.skardach.ro.resource.str.test;

import java.util.HashMap;

import com.skardach.ro.graphics.TextureImpl;
import com.skardach.ro.resource.Texture;
import com.skardach.ro.resource.TextureManager;
/**
 * Simple implementation of texture manager. It only maintains a map of
 * textures indexed by their pathname. It uses {@link TextureImpl} class for
 * texture objects.
 * @author Stanislaw Kardach
 *
 */
public class SimpleTextureManager implements TextureManager {
	String _textureBaseDir;
	HashMap<String, Texture> _textures = new HashMap<String, Texture>();
	/**
	 * Creates a new instance of SimpleTextureManager. All textures are
	 * created relative to the base path given.
	 * @param iBasePath Base path to prepend to all textures retrieved from
	 * this texture manager.
	 */
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

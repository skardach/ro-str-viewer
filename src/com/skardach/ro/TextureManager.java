package com.skardach.ro;

import com.skardach.ro.str.Texture;

/**
 * Flyweight repository for loading textures.
 * Provides methods for opening textures.
 * @author Stanislaw Kardach
 *
 */
public interface TextureManager {

	/**
	 * Returns a texture object.
	 * @param textureName Texture identifier (i.e. texture path).
	 * @return Texture object.
	 */
	Texture getTexture(String textureName);

}

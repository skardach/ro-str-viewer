package com.skardach.ro.resource;


/**
 * Repository for all the resources
 * @author Stanislaw Kardach
 *
 */
public class ResourceManager {
	TextureManager _textures;
	/**
	 * Create resource manager using given texture manager implementation.
	 * @param iTextureManagerImpl TextureManager implementation to be used.
	 */
	public ResourceManager(TextureManager iTextureManagerImpl) {
		_textures = iTextureManagerImpl;
	}
	/**
	 * @return Get texture manager
	 */
	public synchronized TextureManager getTextureManager() {
		return _textures;
	}

}

package com.skardach.ro;

/**
 * Repository for all the resources
 * @author Stanislaw Kardach
 *
 */
public class ResourceManager {
	TextureManager _textures;
	
	public ResourceManager(TextureManager iTextureManagerImpl) {
		_textures = iTextureManagerImpl;
	}
	
	public synchronized TextureManager getTextureManager() {
		return _textures;
	}

}

package service;

import java.awt.image.BufferedImage;
import java.net.URL;

import cache.LRUCache;

/*
 * Cache service is saving the last 20 used item (should be with a parameter).
 * Currently the implements is only for key=URL and value=BufferedImage, but this should be Generics 
 */
public class CacheService {

	private static int SIZE = 20;
	private static CacheService instance = null;
	private LRUCache<URL, BufferedImage> theCache = null;
	
	public static CacheService getInstance() {
		if (instance == null) {
			instance = new CacheService();
		}
		
		return instance;
	}
	
	private CacheService() {
		this.theCache = new LRUCache<>(SIZE);
	}
	
	public BufferedImage get(URL key) {
		return theCache.get(key);
	}
	
	public void put(URL key, BufferedImage value) {
		theCache.put(key, value);
	}
	
	public int getSize() {
		return SIZE;
	}
	
}

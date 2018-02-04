package service;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class CacheServiceTest {


	@Test
	public void equals() {
		URL url = null;
		try {
			url = new URL("www.ynet.co.il");
		} catch (MalformedURLException e) {
			
		}
		BufferedImage bf = new BufferedImage(1, 2, 3);
		CacheService.getInstance().put(url, bf);
		
		BufferedImage bf1 = CacheService.getInstance().get(url);
		Assert.assertEquals(bf, bf1);
	}
	
	@Test
	public void checkForQueue() {
		
		CacheService cacheService = CacheService.getInstance();
		URL url = null;
		URL first = null;
		boolean isFirst = true;
		
		for(int i = 0; i < cacheService.getSize() + 3; i++) {
			try {
				url = new URL("http://www.ynet" + i + ".co.il");
			} catch (MalformedURLException e) {
			}
			
			if (isFirst) {
				isFirst = false;
				first = url;
			}
			
			cacheService.put(url, new BufferedImage(1, 2, 3));
		}
		
		BufferedImage bufferedImage = cacheService.get(first);
		Assert.assertNull(bufferedImage);
	}
	
	

}

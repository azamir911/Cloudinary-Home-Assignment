package service;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class URLServiceTest {

	@Test
	public void getHttpURLConnectionTest() {
		URLService urlService = URLService.getInstance();
		URL url = null;
		try {
			url = new URL("http://www.ynet.co.il");
		} catch (MalformedURLException e) {
			fail("Problem will creating URL");
		}
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = urlService.getHttpURLConnection(url);
		} catch (IOException e) {
			fail("Problem will creating URL connection");
		}
		Assert.assertNotNull(httpURLConnection);
	}
	
	@Test
	public void doesURLExistTest() {
		URLService urlService = URLService.getInstance();
		URL url = null;
		try {
			url = new URL("http://images.one.co.il");
		} catch (MalformedURLException e) {
			fail("Problem will creating URL");
		}
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = urlService.getHttpURLConnection(url);
		} catch (IOException e) {
			fail("Problem will creating URL connection");
		}
		
		try {
			boolean doesURLExist = urlService.doesURLExist(httpURLConnection);
			
			Assert.assertTrue(doesURLExist);
		} catch (IOException e) {
			fail("Problem will checking if URL exists");
		}
	}
	
	@Test
	public void doesURLNotExistTest() {
		URLService urlService = URLService.getInstance();
		URL url = null;
		try {
			url = new URL("http://www.one.com");
		} catch (MalformedURLException e) {
			fail("Problem will creating URL");
		}
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = urlService.getHttpURLConnection(url);
		} catch (IOException e) {
			fail("Problem will creating URL connection");
		}
		
		try {
			boolean doesURLExist = urlService.doesURLExist(httpURLConnection);
			
			Assert.assertFalse(doesURLExist);
		} catch (IOException e) {
			fail("Problem will checking if URL exists");
		}
	}
	
	@Test
	public void getURLContentTypeTest() {
		URLService urlService = URLService.getInstance();
		URL url = null;
		try {
			url = new URL("http://images.one.co.il/images/column/columnist_1545201611070931413141.png");
		} catch (MalformedURLException e) {
			fail("Problem will creating URL");
		}
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = urlService.getHttpURLConnection(url);
		} catch (IOException e) {
			fail("Problem will creating URL connection");
		}
		
		try {
			boolean doesURLExist = urlService.doesURLExist(httpURLConnection);
			
			Assert.assertTrue(doesURLExist);
		} catch (IOException e) {
			fail("Problem will checking if URL exists");
		}
		
		String urlContentType = urlService.getURLContentType(httpURLConnection);
		Assert.assertTrue(urlContentType.startsWith("image"));
		
	}

	@Test
	public void getURLContentTypeIsNotImageTest() {
		URLService urlService = URLService.getInstance();
		URL url = null;
		try {
			url = new URL("http://www.one.co.il/Article/17-18/1,1,1,0/306673.html?ref=hp");
		} catch (MalformedURLException e) {
			fail("Problem will creating URL");
		}
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = urlService.getHttpURLConnection(url);
		} catch (IOException e) {
			fail("Problem will creating URL connection");
		}
		
		try {
			boolean doesURLExist = urlService.doesURLExist(httpURLConnection);
			
			Assert.assertTrue(doesURLExist);
		} catch (IOException e) {
			fail("Problem will checking if URL exists");
		}
		
		String urlContentType = urlService.getURLContentType(httpURLConnection);
		Assert.assertFalse(urlContentType.startsWith("image"));
		
	}

	
}

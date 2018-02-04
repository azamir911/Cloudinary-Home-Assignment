package service;

import image.ImageDimensions;

import org.junit.Assert;
import org.junit.Test;

public class ImageServiceTest {

	@Test
	public void getNewDimensionsSmallNoPaddingTest() {
		ImageService imageService = ImageService.getInstance();
		ImageDimensions newDimensions = null;  
		newDimensions = imageService.getNewDimensions(250, 167, 200, 134);
		Assert.assertEquals(200, newDimensions.getWidth());
		Assert.assertEquals(134, newDimensions.getHeight());
		Assert.assertEquals(0, newDimensions.getTopBottomPadding());
		Assert.assertEquals(0, newDimensions.getLeftRightPadding());
	}
	
	@Test
	public void getNewDimensionsSmallWithTopPaddingTest() {
		ImageService imageService = ImageService.getInstance();
		ImageDimensions newDimensions = null;  
		newDimensions = imageService.getNewDimensions(250, 167, 200, 200);
		Assert.assertEquals(200, newDimensions.getWidth());
		Assert.assertEquals(134, newDimensions.getHeight());
		Assert.assertEquals(33, newDimensions.getTopBottomPadding());
		Assert.assertEquals(0, newDimensions.getLeftRightPadding());
	}

	@Test
	public void getNewDimensionsSmallWithLeftPaddingTest() {
		ImageService imageService = ImageService.getInstance();
		ImageDimensions newDimensions = null;  
		newDimensions = imageService.getNewDimensions(250, 167, 300, 134);
		Assert.assertEquals(201, newDimensions.getWidth());
		Assert.assertEquals(134, newDimensions.getHeight());
		Assert.assertEquals(0, newDimensions.getTopBottomPadding());
		Assert.assertEquals(49, newDimensions.getLeftRightPadding());
	}

	@Test
	public void getNewDimensionsBigPaddingTest() {
		ImageService imageService = ImageService.getInstance();
		ImageDimensions newDimensions = null;  
		newDimensions = imageService.getNewDimensions(250, 167, 300, 300);
		Assert.assertEquals(250, newDimensions.getWidth());
		Assert.assertEquals(167, newDimensions.getHeight());
		Assert.assertEquals(66, newDimensions.getTopBottomPadding());
		Assert.assertEquals(25, newDimensions.getLeftRightPadding());
//		fail("Not yet implemented");
	}

}

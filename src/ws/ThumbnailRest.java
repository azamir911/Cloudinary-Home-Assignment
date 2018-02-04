package ws;

import image.ImageDimensions;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import service.CacheService;
import service.HTMLImageService;
import service.ImageService;
import service.URLService;


@Path("t")
public class ThumbnailRest {

	private static final CacheService theCacheService = CacheService.getInstance();
	private static final HTMLImageService theHTMLImageService = HTMLImageService.getInstance();
	private static final ImageService theImageService = ImageService.getInstance();
	private static final URLService theURLService = URLService.getInstance();

	
//	@GET
//	@Produces(MediaType.TEXT_PLAIN)
//	public String thumbnail(@QueryParam("url") String url, @QueryParam("width") int width, @QueryParam("height") int height) {
//		return "width is " + width + ", and height is " + height + ", and url is " + url;
//	}

//	@GET
//	@Produces(MediaType.TEXT_PLAIN)
//	public Response thumbnail(@QueryParam("url") String url, @QueryParam("width") int width, @QueryParam("height") int height) {
//		String str = "width is " + width + ", and height is " + height + ", and url is " + url;
//		ResponseBuilder builder = Response.ok(str);
//		return builder.build();
//	}

//	@GET
//	@Produces(MediaType.TEXT_PLAIN)
//	public Response thumbnail(@QueryParam("url") String url, @QueryParam("width") int width, @QueryParam("height") int height) {
//		String str = "width is " + width + ", and height is " + height + ", and url is " + url;
//		ResponseBuilder builder = Response.ok(str);
//		return Response.status(Response.Status.NOT_FOUND).build();
//	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response thumbnail(@QueryParam("url") String url, @QueryParam("width") String width, @QueryParam("height") String height) throws IOException {
		
		if (url == null || url.trim().isEmpty()) {
	        return Response.status(Response.Status.NOT_FOUND).entity("url attribute not found").build();
		}
		else if (width == null || width.trim().isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity("width attribute not found").build();
		}
		else if (height == null || height.trim().isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity("height attribute not found").build();
		}

		Integer theWidth = null;
		Integer theHeight = null;
		// Checking validation for the width parameter
		try {
			theWidth = Integer.valueOf(width);
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.PRECONDITION_FAILED).entity("got a bad value for width").build();
		}

		// Checking validation for the height parameter 
		try {
			theHeight = Integer.valueOf(height);
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.PRECONDITION_FAILED).entity("got a bad value for height").build();
		}
		
		URL theUrl = new URL(url);
		
		// Checking if we already got this image before
		BufferedImage bufferedImage = theCacheService.get(theUrl);
		
		// If the image was not exists, then checking the URL, the content type, and add the new image to the cache.
		if (bufferedImage == null) {
			// Checking the URL 
			HttpURLConnection httpURLConnection = theURLService.getHttpURLConnection(theUrl);
			boolean doesURLExist = theURLService.doesURLExist(httpURLConnection);
			if (!doesURLExist) {
				return Response.status(Response.Status.NOT_FOUND).entity("Url not found").build();
				
			}
			// Checking the URL's content
			String urlContentType = theURLService.getURLContentType(httpURLConnection);
			if (!urlContentType.startsWith("image")) {
				return Response.status(Response.Status.NOT_FOUND).entity("Url is not an image").build();
			}
			
			// Reading the image from the url
			bufferedImage = theImageService.readImage(theUrl);
			
			// If successes, add it to the cache (next time it will be faster)
			if (bufferedImage != null) {
				theCacheService.put(theUrl, bufferedImage);
			}
		}
		
		// If image exist, start to resize it.
		if (bufferedImage != null) {
			ImageDimensions newDimensions = null;
			BufferedImage scaledInstance = null;
			
			// Checking if the new dimension is different from the based
			if (bufferedImage.getWidth() != theWidth || bufferedImage.getHeight() != theHeight) {
				newDimensions = theImageService.getNewDimensions(bufferedImage.getWidth(), bufferedImage.getHeight(), theWidth, theHeight);
				
				scaledInstance = theImageService.resizeImage(bufferedImage, newDimensions);
			}
			else {
				scaledInstance = bufferedImage;
			}
			
			// Getting the image binary
			String imageBinary = theImageService.getAsBinary(scaledInstance);
			String htmlImageTag = null;
			
			// Getting the HTML tag
			if (newDimensions != null) {
				htmlImageTag = theHTMLImageService.getHTMLImageTag(imageBinary, newDimensions);
			}
			else {
				htmlImageTag = theHTMLImageService.getHTMLImageTag(imageBinary);
			}

			ResponseBuilder builder = Response.ok(htmlImageTag);
			// Write the tag to the response
			return builder.build();
		}

		return Response.status(Response.Status.NOT_FOUND).entity("Image was not found in the URL").build();
		
//		String str = "width is " + width + ", and height is " + height + ", and url is " + url;
//		ResponseBuilder builder = Response.ok(str);
//		return builder.build();
	}


}

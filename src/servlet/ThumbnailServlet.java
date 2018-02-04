package servlet;


import image.ImageDimensions;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CacheService;
import service.HTMLImageService;
import service.ImageService;
import service.URLService;

/**
 * Servlet implementation class Thumbnail
 */
@WebServlet(
		asyncSupported = true, 
		urlPatterns = { "/thumbnail" }, 
		initParams = { 
				@WebInitParam(name = "url", value = "", description = "A url pointing to the origin image"), 
				@WebInitParam(name = "width", value = "", description = "Result thumbnail width"), 
				@WebInitParam(name = "height", value = "", description = "Result thumbnail height")
		})
public class ThumbnailServlet extends HttpServlet {

	private static final long serialVersionUID = -6754399009022630372L;
	
	private static final CacheService theCacheService = CacheService.getInstance();
	private static final HTMLImageService theHTMLImageService = HTMLImageService.getInstance();
	private static final ImageService theImageService = ImageService.getInstance();
	private static final URLService theURLService = URLService.getInstance();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		// Getting the out stream
		ServletOutputStream out = response.getOutputStream();
		
		// Populate the parameters from the request
		String urlParameter = request.getParameter("url");
		String widthParameter = request.getParameter("width");
		String heightParameter = request.getParameter("height");
		
		// Checking validation
		if (urlParameter == null || urlParameter.trim().isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "url attribute not found");
			return;
		}
		else if (widthParameter == null || widthParameter.trim().isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "width attribute not found");
			return;
		}
		else if (heightParameter == null || heightParameter.trim().isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "height attribute not found");
			return;
		}
		
		Integer width = null;
		Integer height = null;
		// Checking validation for the width parameter
		try {
			width = Integer.valueOf(widthParameter);
		}
		catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "got a bad value for width");
			return;
		}

		// Checking validation for the height parameter 
		try {
			height = Integer.valueOf(heightParameter);
		}
		catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "got a bad value for height");
			return;
		}
		
		URL url = new URL(urlParameter);
		
		// Checking if we already got this image before
		BufferedImage bufferedImage = theCacheService.get(url);
		
		// If the image was not exists, then checking the URL, the content type, and add the new image to the cache.
		if (bufferedImage == null) {
			// Checking the URL 
			HttpURLConnection httpURLConnection = theURLService.getHttpURLConnection(url);
			boolean doesURLExist = theURLService.doesURLExist(httpURLConnection);
			if (!doesURLExist) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Url not found");
				return;
				
			}
			// Checking the URL's content
			String urlContentType = theURLService.getURLContentType(httpURLConnection);
			if (!urlContentType.startsWith("image")) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Url is not an image");
				return;
			}
			
			// Reading the image from the url
			bufferedImage = theImageService.readImage(url);
			
			// If successes, add it to the cache (next time it will be faster)
			if (bufferedImage != null) {
				theCacheService.put(url, bufferedImage);
			}
		}
		
		// If image exist, start to resize it.
		if (bufferedImage != null) {
			ImageDimensions newDimensions = null;
			BufferedImage scaledInstance = null;
			
			// Checking if the new dimension is different from the based
			if (bufferedImage.getWidth() != width || bufferedImage.getHeight() != height) {
				newDimensions = theImageService.getNewDimensions(bufferedImage.getWidth(), bufferedImage.getHeight(), width, height);
				
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

			// Write the tag to the response
			out.print(htmlImageTag);
			
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image was not found in the URL");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
}

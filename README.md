# Cloudinary-Home-Assignment

I have used Eclipse for this project.

Server: Tomcat v8.0

Synopsis
This project is getting a request with a url for a source image, width and height.
The image is scaled down to fill the given width and height while retaining the
original aspect ratio and with all of the original image visible. If the requested
dimensions are bigger than the original image's, the image doesn’t scale up. If
the proportions of the original image do not match the given width and height,
black padding is added to the image to reach the required size.

API Reference
While installing it localy using a web application server, use the next url for calling the rezise service: /thumbnail?url={url}&width={width}&height={height}

Tests
The tests are located in the "test" folder, and may be run as a JUnit tests.

Using Jars
Servlet-api => Open source by Apache

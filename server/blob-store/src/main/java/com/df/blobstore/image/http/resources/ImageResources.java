package com.df.blobstore.image.http.resources;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.df.blobstore.image.Image;
import com.df.blobstore.image.ImageFormat;
import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.ImageService;
import com.df.blobstore.image.http.ImageOwnerHolder;
import com.df.blobstore.image.http.exception.ImageServiceException;
import com.google.common.io.BaseEncoding;

@Path("/images")
@Produces("application/json;charset=UTF-8")
@Component
public class ImageResources {

	private ImageService imageService;

	private static final Logger logger = LoggerFactory.getLogger(ImageResources.class);

	public ImageResources(ImageService imageService) {
		this.imageService = imageService;
	}

	@PUT
	public String addImage(String imageData, String imageName) {
		String owner = ImageOwnerHolder.getImageOwner();
		byte[] imageBytes;
		BaseEncoding base64 = BaseEncoding.base64();
		try {
			imageBytes = base64.decode(imageData);
		} catch (IllegalArgumentException ex) {
			throw ImageServiceException.InvalidBase64Encoding();
		}
		return imageService.uploadImage(new ByteArrayInputStream(imageBytes), owner, imageName).getKey();
	}

	@DELETE
	public void deleteImage(String imagekey) {
		imageService.deleteImage(new ImageKey(imagekey));
	}

	@GET
	@Path("/{imageId}")
	public Response getImage(@PathParam("imageId") String imageId) {
		int index = imageId.lastIndexOf(".");
		if(index!=-1){
			imageId = imageId.substring(0, index);
		}
		Image image = imageService.fetchImage(new ImageKey(imageId));
		if (image == null) {
			return Response.status(Status.NOT_FOUND).type(MediaType.WILDCARD_TYPE).build();
		}

		InputStream in = null;
		try {
			ImageFormat format = image.getImageAttributes().getFormat();
			ResponseBuilder builder = Response.ok();
			builder.type(format.getMIMEType());
			in = image.getBundleValue().getDataInBundle();
			byte[] imageData = new byte[image.getBundleValue().getSize()];

			in.read(imageData, 0, imageData.length);
			return builder.entity(imageData).build();
		} catch (Throwable ex) {
			String msg = "Cannot get image from key " + imageId;
			logger.error(msg, ex);
			return Response.status(Status.NOT_FOUND).type(MediaType.WILDCARD_TYPE).build();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Throwable ex) {
				}
			}
		}
	}

}

package com.df.http.rs.provider.image;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.df.blobstore.image.http.exception.ImageServiceException;
import com.df.http.rs.provider.ErrorResponse;

public class ImageServiceExceptionMapper implements ExceptionMapper<ImageServiceException> {

	@Override
	public Response toResponse(ImageServiceException ex) {
		ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
		return Response.status(400).entity(error).build();
	}
}

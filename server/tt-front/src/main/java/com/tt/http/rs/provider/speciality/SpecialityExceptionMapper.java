package com.tt.http.rs.provider.speciality;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.tt.http.rs.provider.ErrorResponse;
import com.tt.spec.locality.exception.SpecialityBaseException;

@Provider
public class SpecialityExceptionMapper implements ExceptionMapper<SpecialityBaseException> {

	@Override
	public Response toResponse(SpecialityBaseException ex) {
		ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
		return Response.status(400).entity(error).build();
	}
}

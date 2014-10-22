package com.tt.http.rs.provider.idm;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.tt.http.rs.provider.ErrorResponse;
import com.tt.idm.exception.IdmException;

@Provider
public class IdmExceptionMapper implements ExceptionMapper<IdmException> {

	@Override
	public Response toResponse(IdmException ex) {
		ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
		return Response.status(400).entity(error).build();
	}
}

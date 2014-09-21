package com.df.http.rs.provider.speciality;

import java.util.ArrayList;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.df.http.rs.provider.ErrorResponse;
import com.df.http.rs.provider.ErrorResponse.ErrorFormat;
import com.df.spec.locality.exception.validation.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

	private ObjectMapper objectMapper;

	public static final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";

	private static final Logger logger = LoggerFactory.getLogger(ValidationExceptionMapper.class);

	public ValidationExceptionMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public Response toResponse(ValidationException ex) {
		ConstraintViolation<?>[] violations = ex.getViolations();
		ArrayList<ValidationError> errors = new ArrayList<ValidationError>();
		for (ConstraintViolation<?> violation : violations) {
			errors.add(new ValidationError(violation.getMessageTemplate(), violation.getMessage()));
		}
		try {
			String jsonErrors = objectMapper.writeValueAsString(errors);
			ErrorResponse errorResponse = new ErrorResponse(VALIDATION_ERROR_CODE, jsonErrors);
			errorResponse.setErrorFormat(ErrorFormat.JSON);
			return Response.status(400).entity(errorResponse).build();
		} catch (Throwable error) {
			logger.error("error to marshall validation error to json ", error);
			return Response.status(500).build();
		}
	}

	static class ValidationError {
		private String code;

		private String message;

		public ValidationError(String code, String message) {
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

	}
}

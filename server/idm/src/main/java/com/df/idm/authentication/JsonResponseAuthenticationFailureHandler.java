package com.df.idm.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.df.idm.authentication.http.AuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonResponseAuthenticationFailureHandler implements AuthenticationFailureHandler {
	private ObjectMapper objectMapper;

	public JsonResponseAuthenticationFailureHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public JsonResponseAuthenticationFailureHandler() {
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException,
			ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");
		AuthenticationResponse resp = new AuthenticationResponse();
		resp.setAuthenticated(false);
		resp.setErrorMessage(ex.getLocalizedMessage());
		response.getWriter().write(objectMapper.writeValueAsString(resp));
	}

}

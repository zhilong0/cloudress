package com.df.idm.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.df.idm.authentication.http.AuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonResponseAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private ObjectMapper objectMapper;

	public JsonResponseAuthenticationSuccessHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public JsonResponseAuthenticationSuccessHandler() {
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {
		response.setContentType("application/json;charset=UTF-8");
		AuthenticationResponse resp = new AuthenticationResponse();
		resp.setAuthenticated(true);
		response.getWriter().write(objectMapper.writeValueAsString(resp));
	}

}

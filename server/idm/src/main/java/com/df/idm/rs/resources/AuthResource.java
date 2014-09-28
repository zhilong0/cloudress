package com.df.idm.rs.resources;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Component;

import com.df.idm.authentication.UserPropertyAuthenticationToken;
import com.df.idm.authentication.http.AuthenticationRequest;
import com.df.idm.authentication.http.AuthenticationResponse;
import com.df.idm.model.User;
import com.df.idm.service.contract.UserManagementService;

@Path("/auth")
@Produces("application/json;charset=UTF-8")
@Component
public class AuthResource {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserManagementService userManagementService;

	private RememberMeServices rememberMeServices = new NullRememberMeServices();

	private static final Logger logger = LoggerFactory.getLogger(AuthResource.class);

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void setRememberMeServices(RememberMeServices rememberMeServices) {
		this.rememberMeServices = rememberMeServices;
	}

	public UserManagementService getUserManagementService() {
		return userManagementService;
	}

	@POST
	@Path("/login")
	public AuthenticationResponse login(@Context HttpServletResponse response, @Context HttpServletRequest request, AuthenticationRequest ar) {
		AuthenticationResponse aRep = new AuthenticationResponse();
		if (ar == null || ar.getCode() == null) {
			aRep.setAuthenticated(false);
			aRep.setErrorMessage("Invalid parameter, user account must not be null");
			return aRep;
		}
		Authentication authentication = new UserPropertyAuthenticationToken(ar.getCode(), ar.getPassword());
		try {
			authentication = authenticationManager.authenticate(authentication);
			aRep.setAuthenticated(true);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			rememberMeServices.loginSuccess(request, response, authentication);
			User user = userManagementService.getUserByCode(ar.getCode());
			if (user != null) {
				userManagementService.updateUserLastLogin(user.getId(), new Date());
			}
		} catch (AuthenticationException ex) {
			logger.error("authentication failure for user " + ar.getCode(), ex);
			aRep.setAuthenticated(false);
			aRep.setErrorMessage(ex.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return aRep;
	}

	@POST
	@GET
	@Path("/logout")
	public void logout(@Context HttpServletResponse response, @Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(null);
		SecurityContextHolder.clearContext();
	}
}

package com.df.idm.rs.resources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.df.idm.authentication.http.AuthenticationRequest;
import com.df.idm.exception.UserException;
import com.df.idm.model.User;
import com.df.idm.registration.EmailVerificationResultHandler;
import com.df.idm.registration.ForwardVerificationResultHandler;
import com.df.idm.service.contract.UserManagementService;

@Path("/users")
@Produces("application/json;charset=UTF-8")
@Component
public class UserResources {

	@Autowired
	private UserManagementService userManagementService;

	@Autowired
	private EmailVerificationResultHandler verificationResultHandler = new ForwardVerificationResultHandler();

	public void setUserManagementService(UserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}

	public void setVerificationResultHandler(EmailVerificationResultHandler verificationResultHandler) {
		this.verificationResultHandler = verificationResultHandler;
	}

	@POST
	@Path("/email")
	public User newUserByEmail(AuthenticationRequest request) {
		User newUser = userManagementService.createUserByEmail(request.getCode(), request.getPassword());
		newUser.cleanPassword();
		return newUser;
	}

	@POST
	@Path("/code")
	public User newUserByCode(AuthenticationRequest request) {
		User newUser = userManagementService.createUserByCode(request.getCode(), request.getPassword());
		newUser.cleanPassword();
		return newUser;
	}

	@POST
	@Path("/cellphone")
	public User newUserByCellphone(AuthenticationRequest request) {
		User newUser = userManagementService.createUserByCellphone(request.getCode(), request.getPassword());
		return newUser;
	}

	@PUT
	@Path("/")
	public User updateUser(User user) {
		return userManagementService.updateUser(user);
	}

	@GET
	@Path("/")
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			String name = authentication.getName();
			return userManagementService.getUserByCode(name);
		}
		return null;
	}

	@GET
	@Path("/id/{id}")
	public User getUserById(@PathParam("id") String userId) {
		User found = userManagementService.getUserById(userId);
		if (found != null) {
			found.cleanPassword();
			return found;
		}
		throw UserException.userIdNotFound(userId);
	}

	@DELETE
	@Path("/id/{id}")
	public void disableUserById(@PathParam("id") String userId) {
		userManagementService.disableUser(userId);
	}

	@GET
	@Path("/code/{code}")
	public User getUserByCode(@PathParam("code") String userCode) {
		User found = userManagementService.getUserByCode(userCode);
		if (found != null) {
			found.cleanPassword();
			return found;
		}
		throw UserException.userCodeNotFound(userCode);
	}

	@GET
	@Path("/email/verify")
	public void emalRegistrationVerify(@Context HttpServletResponse response, @Context HttpServletRequest request, @QueryParam("token") String token)
			throws Exception {
		User user = userManagementService.verifyEmailRegistrationToken(token);
		if (user != null) {
			verificationResultHandler.onVerificationSucceed(user.getEmail(), request, response);
		} else {
			verificationResultHandler.onVerificationFailure(request, response);
		}
	}
}

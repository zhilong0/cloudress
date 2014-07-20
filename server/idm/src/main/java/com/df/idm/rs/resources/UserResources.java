package com.df.idm.rs.resources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	public User newUserByEmail(@HeaderParam("email") String email, @HeaderParam("password") String password) {
		User newUser = userManagementService.createUserByEmail(email, password);
		newUser.cleanPassword();
		return newUser;
	}

	@POST
	@Path("/code")
	public User newUserByCode(@HeaderParam("code") String userCode, @HeaderParam("password") String password) {
		User newUser = userManagementService.createUserByCode(userCode, password);
		newUser.cleanPassword();
		return newUser;
	}

	@POST
	@Path("/cellphone")
	public User newUserByCellphone(@HeaderParam("cellphone") String cellphone, @HeaderParam("password") String password) {
		User newUser = userManagementService.createUserByCellphone(cellphone, password);
		return newUser;
	}

	@PUT
	@Path("/")
	public User updateUser(User user) {
		return userManagementService.updateUser(user);
	}

	@GET
	@Path("/id/{id}")
	public User getUserById(@PathParam("id") String userId) {
		User found = userManagementService.getUserById(userId);
		if (found != null) {
			found.cleanPassword();
		}
		return found;
	}

	@GET
	@Path("/code/{code}")
	public User getUserByCode(@PathParam("code") String userCode) {
		User found = userManagementService.getUserByCode(userCode);
		if (found != null) {
			found.cleanPassword();
		}
		return found;
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

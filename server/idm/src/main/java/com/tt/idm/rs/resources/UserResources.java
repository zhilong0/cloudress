package com.tt.idm.rs.resources;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tt.common.utils.StringUtils;
import com.tt.idm.authentication.UserPropertyAuthenticationToken;
import com.tt.idm.authentication.http.AuthenticationRequest;
import com.tt.idm.exception.IdmException;
import com.tt.idm.exception.UserException;
import com.tt.idm.model.User;
import com.tt.idm.registration.EmailVerificationResultHandler;
import com.tt.idm.registration.ForwardVerificationResultHandler;
import com.tt.idm.service.contract.UserManagementService;

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
		if (request.getNickName() != null) {
			newUser.setNickName(request.getNickName());
			userManagementService.updateUser(newUser);
		}
		newUser.cleanPassword();
		return newUser;
	}

	@POST
	@Path("/code")
	public User newUserByCode(AuthenticationRequest request) {
		User newUser = userManagementService.createUserByCode(request.getCode(), request.getPassword());
		if (request.getNickName() != null) {
			newUser.setNickName(request.getNickName());
			userManagementService.updateUser(newUser);
		}
		newUser.cleanPassword();
		return newUser;
	}

	@POST
	@Path("/cellphone")
	public User newUserByCellphone(AuthenticationRequest request) {
		User newUser = userManagementService.createUserByCellphone(request.getCode(), request.getPassword(), request.getPassword());
		if (request.getNickName() != null) {
			newUser.setNickName(request.getNickName());
			userManagementService.updateUser(newUser);
		}
		newUser.cleanPassword();
		return newUser;
	}

	@GET
	@POST
	@Path("/reg_token")
	public void requestCellphoneRegistrationToken(@Context HttpServletRequest request, @QueryParam("cellphone") String cellphone) {
		if (!StringUtils.isValidCellPhone(cellphone)) {
			throw new IdmException(null, RequestParamsErrorCode.INVALID_CELLPHONE, "Invalid cellphone");
		}
		HttpSession session = request.getSession(true);
		Object value = session.getAttribute("CELLPHONE_REGISTRATION_TOKEN_LAST_REQUEST");
		if (value == null) {
			userManagementService.sendCellphoneRegistrationToken(cellphone);
			session.setAttribute("CELLPHONE_REGISTRATION_TOKEN_LAST_REQUEST", new Date().getTime());
		} else {
			long lastSend = (Long) value;
			long now = new Date().getTime();
			if (TimeUnit.MINUTES.toMillis(1) < (now - lastSend)) {
				userManagementService.sendCellphoneRegistrationToken(cellphone);
				session.setAttribute("CELLPHONE_REGISTRATION_TOKEN_LAST_REQUEST", new Date().getTime());
			}
		}
	}

	@POST
	@Path("/")
	public User newUser(AuthenticationRequest request) {
		String code = request.getCode();
		User newUser = null;
		if (StringUtils.isValidEmail(code)) {
			newUser = userManagementService.createUserByEmail(request.getCode(), request.getPassword());
		} else if (StringUtils.isValidCellPhone(code)) {
			newUser = userManagementService.createUserByCellphone(request.getCode(), request.getPassword(), request.getPassword());
		} else {
			newUser = userManagementService.createUserByCode(request.getCode(), request.getPassword());
		}
		if (request.getNickName() != null) {
			newUser.setNickName(request.getNickName());
			userManagementService.updateUser(newUser);
		}
		newUser.cleanPassword();
		return newUser;
	}

	@PUT
	@Path("/")
	public User updateUser(User user) {
		User updatedUser = userManagementService.updateUser(user);
		updatedUser.cleanPassword();
		return updatedUser;
	}

	@POST
	@Path("/code/{userCode}/unlock")
	@PreAuthorize("hasPermission('SYS','USER_ACCOUNT_EDIT')")
	public void unlock(@PathParam("userCode") String userCode) {
		userManagementService.unLockUser(userCode);
	}

	@POST
	@Path("/code/{userCode}/lock")
	@PreAuthorize("hasPermission('SYS','USER_ACCOUNT_EDIT')")
	public void lock(@PathParam("userCode") String userCode) {
		userManagementService.lockUser(userCode);
	}

	@POST
	@Path("/code/{userCode}/disable")
	@PreAuthorize("hasPermission('SYS','USER_ACCOUNT_EDIT')")
	public void disable(@PathParam("userCode") String userCode) {
		userManagementService.disableUser(userCode);
	}

	@POST
	@Path("/code/{userCode}/enable")
	@PreAuthorize("hasPermission('SYS','USER_ACCOUNT_EDIT')")
	public void enable(@PathParam("userCode") String userCode) {
		userManagementService.enableUser(userCode);
	}

	@POST
	@PUT 
	@Path("/code/{userCode}/change_password")
	@PreAuthorize("hasPermission('SYS','USER_ACCOUNT_EDIT')")
	public void changePassword(@PathParam("userCode") String userCode, PasswordChange passwordChange) {
		userManagementService.updatePassword(userCode, passwordChange.getNewPassword());
	}

	@POST
	@Path("/change_password")
	@PreAuthorize("isAuthenticated()")
	public void changeSelfPassword(PasswordChange passwordChange) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		userManagementService.updatePassword(authentication.getName(), passwordChange.getOldPassword(), passwordChange.getNewPassword());
	}

	@GET
	@Path("/")
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication instanceof UserPropertyAuthenticationToken) {
			String name = authentication.getName();
			User found = userManagementService.getUserByCode(name);
			if (found != null) {
				found.cleanPassword();
				return found;
			}
		}
		return null;
	}

	@GET
	@Path("/list")
	@PreAuthorize("hasPermission('SYS','USER_LIST_READ')")
	public List<User> getUserList(@QueryParam("offset") int offset, @QueryParam("limit") int limit) {
		if (offset < 0) {
			offset = 0;
		}
		if (limit <= 0) {
			limit = 20;
		}
		List<User> users = userManagementService.getUserList(offset, limit);
		for (User user : users) {
			user.cleanPassword();
		}
		return users;
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
	public void emailRegistrationVerify(@Context HttpServletResponse response, @Context HttpServletRequest request, @QueryParam("token") String token)
			throws Exception {
		User user = userManagementService.verifyEmailRegistrationToken(token);
		if (user != null) {
			verificationResultHandler.onVerificationSucceed(user.getEmail(), request, response);
		} else {
			verificationResultHandler.onVerificationFailure(request, response);
		}
	}
}

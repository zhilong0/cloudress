package com.df.idm.authentication;

import java.util.Date;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import com.df.idm.service.contract.UserManagementService;

public class UserSuccessAuthenticationListener implements ApplicationListener<AuthenticationSuccessEvent> {

	private UserManagementService userManagementService;

	public UserSuccessAuthenticationListener(UserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		String userCode = event.getAuthentication().getName();
		userManagementService.updateUserLastLogin(userCode, new Date());
	}

}

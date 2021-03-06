package com.tt.idm.service.impl;

import com.tt.idm.model.Role;
import com.tt.idm.model.User;
import com.tt.idm.service.contract.SecurityModelRepository;
import com.tt.idm.service.contract.UserAuthorityService;
import com.tt.idm.service.contract.UserManagementService;

public class UserAuthorityServiceImpl implements UserAuthorityService {

	private UserManagementService userManagementService;

	private SecurityModelRepository securityModelRepository;

	public UserAuthorityServiceImpl(UserManagementService userManagementService, SecurityModelRepository securityModelRepository) {
		this.userManagementService = userManagementService;
		this.securityModelRepository = securityModelRepository;
	}

	@Override
	public boolean assign(String userCode, String roleName) {
		Role role = securityModelRepository.getRole(roleName);
		User user = userManagementService.getUserByCode(userCode);
		if (role != null && user != null) {
			user.addRole(role);
			userManagementService.updateUser(user);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean revoke(String userCode, String roleName) {
		User user = userManagementService.getUserByCode(userCode);
		if (user == null) {
			return false;
		}
		boolean hasRole = user.removeRole(roleName);
		if (hasRole) {
			userManagementService.updateUser(user);
			return true;
		} else {
			return false;
		}
	}

}

package com.tt.idm.authentication.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tt.idm.exception.UserNotFoundException;
import com.tt.idm.model.User;
import com.tt.idm.service.contract.UserManagementService;

public class UserObjectServiceImpl implements UserObjectService {

	@Autowired
	private UserManagementService userManagementService;

	public UserObjectServiceImpl(UserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}

	public void setUserManagementService(UserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userManagementService.getUserByCode(userName);
		if (user == null) {
			throw new UserNotFoundException(String.format("User with code %s is not found", userName));
		}
		return new UserObject(user);
	}
}

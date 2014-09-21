package com.df.idm.authentication.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.df.idm.exception.UserNotFoundException;
import com.df.idm.model.User;
import com.df.idm.service.contract.UserManagementService;

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
	public UserObject loadUserByEmail(String emailOrTelehone) {
		User user = userManagementService.getUserByEmail(emailOrTelehone);
		if (user == null) {
			throw new UserNotFoundException(String.format("User with mail %s is not found", emailOrTelehone));
		}
		return new UserObject(user);
	}

	@Override
	public UserObject loadUserByCellphone(String cellPhone) {
		User user = userManagementService.getUserByCellphone(cellPhone);
		if (user == null) {
			throw new UserNotFoundException(String.format("User with cell phone %s is not found", cellPhone));
		}
		return new UserObject(user);

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

package com.df.idm.service.impl;

import com.df.idm.dao.UserDao;
import com.df.idm.model.Role;
import com.df.idm.model.User;
import com.df.idm.service.contract.UserAuthorityService;

public class UserAuthorityServiceImpl implements UserAuthorityService {

	private UserDao userDao;

	public UserAuthorityServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void assign(User user, Role role) {
		user.addRole(role);
		userDao.updateUser(user);
	}

	@Override
	public void revoke(User user, Role role) {
		user.removeRole(role);
		userDao.updateUser(user);
	}

}

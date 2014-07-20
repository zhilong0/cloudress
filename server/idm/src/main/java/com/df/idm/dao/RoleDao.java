package com.df.idm.dao;

import com.df.idm.model.Constants;
import com.df.idm.model.Role;
import com.df.idm.model.User;

public interface RoleDao extends Constants {

	void insertRole(User user, Role role);

	void deleteRole(User user, Role role);

	public Role findRole(String roleName);
}

package com.tt.idm.dao;

import java.util.List;

import com.tt.idm.model.Constants;
import com.tt.idm.model.Permission;
import com.tt.idm.model.Role;

public interface RoleDao extends Constants {

	Role insertRole(Role role);

	boolean deleteRole(String roleName);

	Role findRole(String roleName);

	List<Role> findRoles(String... roleNames);

	int bulkUpdate(List<Role> roles);

	List<Role> getRoleList();

	List<String> getRoleIdentifierListWithPermission(Permission permission);

}

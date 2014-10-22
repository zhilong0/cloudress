package com.tt.idm.service.contract;

import java.util.List;

import com.tt.idm.model.Permission;
import com.tt.idm.model.Role;

public interface SecurityModelRepository {

	Role addRole(Role role);

	Role getRole(String roleName);

	boolean updateRole(Role role);

	Permission addPermission(Permission permission);

	boolean removePermission(Permission permission);

	List<Permission> getPermissionList(String domain);

	List<Role> getRoleList();

	boolean hasPermission(Permission permission);

	boolean updatePermissionDescription(Permission permission);

}

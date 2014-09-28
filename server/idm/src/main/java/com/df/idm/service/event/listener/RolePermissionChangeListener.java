package com.df.idm.service.event.listener;

import java.util.List;

import com.df.idm.model.Permission;
import com.df.idm.model.Role;

public interface RolePermissionChangeListener {

	void onPermissionChange(Role role, List<Permission> oldPList, List<Permission> newPList);
}

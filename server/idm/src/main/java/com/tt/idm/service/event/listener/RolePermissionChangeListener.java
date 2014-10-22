package com.tt.idm.service.event.listener;

import java.util.List;

import com.tt.idm.model.Permission;
import com.tt.idm.model.Role;

public interface RolePermissionChangeListener {

	void onPermissionChange(Role role, List<Permission> oldPList, List<Permission> newPList);
}

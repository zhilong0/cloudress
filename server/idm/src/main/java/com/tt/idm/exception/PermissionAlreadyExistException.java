package com.tt.idm.exception;

import com.tt.idm.model.Permission;

public class PermissionAlreadyExistException extends IdmException {

	private static final long serialVersionUID = 1L;

	private Permission permission;

	public PermissionAlreadyExistException(Throwable ex, Permission permission) {
		super(null, PermissionErrorCode.PERMISSIONE_ALREADY_EXIST, "permission %s already exist", permission);
		this.permission = permission;
	}

	public Permission getPermission() {
		return permission;
	}
}

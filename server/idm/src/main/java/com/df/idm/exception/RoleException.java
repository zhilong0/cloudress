package com.df.idm.exception;

public class RoleException extends IdmException {

	private static final long serialVersionUID = 1L;

	public static final String ROLE_WITH_NAME_ALREADY_EXIST = "R100001";

	public static final String ROLE_WITH_NAME_NOT_EXIST = "R100002";

	public RoleException(Throwable cause, String errorCode) {
		super(cause, errorCode);
	}

	public RoleException(Throwable cause, String errorCode, String messageFormat, Object... args) {
		super(cause, errorCode, messageFormat, args);
	}

	public RoleException(String errorCode, String messageFormat, Object... args) {
		super(null, errorCode, messageFormat, args);
	}

	public static RoleException roleWithNameNotExist(String roleName) {
		String msg = "Role %s does not exist";
		return new RoleException(ROLE_WITH_NAME_NOT_EXIST, msg, roleName);
	}

	public static RoleException roleWithNameAlreadyExist(String roleName) {
		String msg = "Role %s already exist";
		return new RoleException(ROLE_WITH_NAME_ALREADY_EXIST, msg, roleName);
	}
}

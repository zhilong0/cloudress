package com.df.spec.locality.exception;

public class PermissionDeniedException extends SpecialityBaseException {

	private static final long serialVersionUID = 1L;

	public static final String PERMISSION_DENIED = "PM100001";

	public PermissionDeniedException(String messageFormat, Object... args) {
		super(PERMISSION_DENIED, messageFormat, args);
	}

	public PermissionDeniedException() {
		super(PERMISSION_DENIED, "permission denied");
	}

}

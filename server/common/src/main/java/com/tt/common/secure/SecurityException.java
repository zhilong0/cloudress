package com.tt.common.secure;

public class SecurityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SecurityException(Throwable cause) {
		super(cause);
	}

	public SecurityException(String format, Object... args) {
		super(String.format(format, args));
	}

	public SecurityException(Throwable cause, String format, Object... args) {
		super(String.format(format, args), cause);
	}
}

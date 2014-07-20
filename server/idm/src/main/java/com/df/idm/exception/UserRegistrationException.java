package com.df.idm.exception;

public class UserRegistrationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserRegistrationException(Throwable cause) {
		super(cause);
	}

	public UserRegistrationException(Throwable cause, String messageFormat, Object... args) {
		super(String.format(messageFormat, args), cause);
	}

	public UserRegistrationException(String messageFormat, Object... args) {
		super(String.format(messageFormat, args));
	}

}

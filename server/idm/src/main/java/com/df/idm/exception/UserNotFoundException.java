package com.df.idm.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}

	public UserNotFoundException(String msg) {
		super(msg);
	}
}

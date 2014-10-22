package com.tt.idm.authentication.oauth2.qq;

public class QQResourceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public QQResourceException(Throwable cause) {
		super(cause);
	}

	public QQResourceException(Throwable cause, String messageFormat, Object... args) {
		super(String.format(messageFormat, args), cause);
	}

	public QQResourceException(String messageFormat, Object... args) {
		super(String.format(messageFormat, args));
	}

}
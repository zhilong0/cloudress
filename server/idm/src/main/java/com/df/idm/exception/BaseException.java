package com.df.idm.exception;

public class BaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(Throwable cause, String messageFormat, Object... args) {
		super(String.format(messageFormat, args), cause);
	}

	public BaseException(String messageFormat, Object... args) {
		super(String.format(messageFormat, args));
	}

}

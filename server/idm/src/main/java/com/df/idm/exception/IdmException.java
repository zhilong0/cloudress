package com.df.idm.exception;

public class IdmException extends BaseException {

	private static final long serialVersionUID = 1L;

	private String errorCode;

	public IdmException(Throwable cause) {
		super(cause);
	}

	public IdmException(Throwable throwable, String messageFormat, Object... args) {
		super(throwable, messageFormat, args);
	}

	public IdmException(Throwable cause, String errorCode) {
		this(cause);
		this.errorCode = errorCode;
	}

	public IdmException(Throwable throwable, String errorCode, String messageFormat, Object... args) {
		super(throwable, messageFormat, args);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}

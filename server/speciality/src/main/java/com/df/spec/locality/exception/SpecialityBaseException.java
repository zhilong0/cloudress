package com.df.spec.locality.exception;

public class SpecialityBaseException extends BaseException {
	private static final long serialVersionUID = 1L;

	private String errorCode;

	public SpecialityBaseException(Throwable cause) {
		super(cause);
	}

	public SpecialityBaseException(Throwable throwable, String messageFormat, Object... args) {
		super(throwable, messageFormat, args);
	}

	public SpecialityBaseException(String errorCode, String messageFormat, Object... args) {
		this((Throwable) null, messageFormat, args);
		this.errorCode = errorCode;
	}

	public SpecialityBaseException(Throwable cause, String errorCode) {
		super(cause, errorCode);
		this.errorCode = errorCode;
	}

	public SpecialityBaseException(String errorCode) {
		this(null, errorCode);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}

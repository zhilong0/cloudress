package com.df.http.rs.provider;

import java.io.Serializable;

public class ErrorResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private String errorCode;

	private String error;

	private ErrorFormat errorFormat = ErrorFormat.TEXT;

	public static enum ErrorFormat {
		TEXT, JSON
	}

	public ErrorResponse(String errorCode, String error) {
		this.errorCode = errorCode;
		this.error = error;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public ErrorFormat getErrorFormat() {
		return errorFormat;
	}

	public void setErrorFormat(ErrorFormat errorFormat) {
		this.errorFormat = errorFormat;
	}
}

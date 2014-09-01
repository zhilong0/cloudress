package com.df.common.provision;

public class DataProvisionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataProvisionException(Throwable cause) {
		super(cause);
	}

	public DataProvisionException(String format, Object... args) {
		super(String.format(format, args));
	}

	public DataProvisionException(Throwable cause, String format, Object... args) {
		super(String.format(format, args), cause);
	}
}

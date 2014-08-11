package com.df.blobstore.image.exception;

public class UnknownImageFormatException extends ImageException {

	private static final long serialVersionUID = 1L;

	public UnknownImageFormatException() {
		super("Unknown image format");
	}

	public UnknownImageFormatException(Throwable cause) {
		super(cause);
	}

	public UnknownImageFormatException(Throwable throwable, String messageFormat, Object... args) {
		super(String.format(messageFormat, args), throwable);
	}

	public UnknownImageFormatException(String messageFormat, Object... args) {
		this(null, messageFormat, args);
	}
}

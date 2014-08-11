package com.df.blobstore.image.http.exception;

public class ImageServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorCode;

	public static final String INVALID_BASE64_ENCODING = "IMG100001";

	public ImageServiceException(Throwable cause) {
		super(cause);
	}

	public ImageServiceException(Throwable throwable, String messageFormat, Object... args) {
		super(String.format(messageFormat, args), throwable);
	}

	public ImageServiceException(Throwable cause, String errorCode) {
		this(cause);
		this.errorCode = errorCode;
	}

	public ImageServiceException(Throwable throwable, String errorCode, String messageFormat, Object... args) {
		this(throwable, messageFormat, args);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public static ImageServiceException InvalidBase64Encoding() {
		throw new ImageServiceException(null, INVALID_BASE64_ENCODING, "Invalid base64 encoding");
	}
}

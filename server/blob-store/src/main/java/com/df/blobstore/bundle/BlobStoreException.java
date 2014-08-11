package com.df.blobstore.bundle;

public class BlobStoreException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BlobStoreException(Throwable cause) {
		super(cause);
	}

	public BlobStoreException(Throwable throwable, String messageFormat, Object... args) {
		super(String.format(messageFormat, args), throwable);
	}

	public BlobStoreException(String messageFormat, Object... args) {
		this(null, messageFormat, args);
	}

}

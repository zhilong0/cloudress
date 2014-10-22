package com.tt.blobstore.image.exception;

import com.tt.blobstore.bundle.BlobStoreException;

public class ImageException extends BlobStoreException {

	private static final long serialVersionUID = 1L;

	public ImageException(Throwable cause) {
		super(cause);
	}

	public ImageException(Throwable throwable, String messageFormat, Object... args) {
		super(String.format(messageFormat, args), throwable);
	}

	public ImageException(String messageFormat, Object... args) {
		this(null, messageFormat, args);
	}

}

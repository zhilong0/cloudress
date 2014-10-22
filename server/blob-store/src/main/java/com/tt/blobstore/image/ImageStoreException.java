package com.tt.blobstore.image;

import com.tt.blobstore.image.exception.ImageException;

public class ImageStoreException extends ImageException {

	private static final long serialVersionUID = 1L;

	public ImageStoreException(Throwable cause) {
		super(cause);
	}

	public ImageStoreException(Throwable throwable, String messageFormat, Object... args) {
		super(String.format(messageFormat, args), throwable);
	}

	public ImageStoreException(String messageFormat, Object... args) {
		this(null, messageFormat, args);
	}

}

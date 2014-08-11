package com.df.blobstore.image.http;

import com.df.blobstore.image.ImageKey;

public interface ImageReferenceFactory {

	ImageReference createImageReference(ImageKey imageKey);
}

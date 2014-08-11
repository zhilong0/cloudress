package com.df.blobstore.image.http;

import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageKey;

public interface ImageLinkCreator {

	String createImageLink(ImageKey imageKey, ImageAttributes attributes);
}

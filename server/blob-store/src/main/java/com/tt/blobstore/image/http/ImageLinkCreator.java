package com.tt.blobstore.image.http;

import com.tt.blobstore.image.ImageAttributes;
import com.tt.blobstore.image.ImageKey;

public interface ImageLinkCreator {

	String createImageLink(ImageKey imageKey, ImageAttributes attributes);
}

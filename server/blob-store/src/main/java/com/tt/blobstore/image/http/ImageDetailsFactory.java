package com.tt.blobstore.image.http;

import com.tt.blobstore.image.ImageKey;

public interface ImageDetailsFactory {

	ImageDetails createImageDetails(ImageKey imageKey);
}

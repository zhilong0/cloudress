package com.df.blobstore.image.http;

import com.df.blobstore.image.ImageKey;

public interface ImageDetailsFactory {

	ImageDetails createImageDetails(ImageKey imageKey);
}

package com.df.blobstore.image;

import com.df.blobstore.bundle.BundleKey;
import com.df.blobstore.bundle.BundleService;

public interface ImageServiceRoute {

	BundleService getBundleService(ImageKey imageKey);

	BundleService getThumbnailBundleService(ImageKey imageKey, int width, int heigth);

	BundleKey resolveBundleKey(ImageKey imageKey);

	BundleKey resolveThumbnailBundleKey(ImageKey imageKey, int width, int heigth);

	ImageKey hash(ImageAttributes attributes);

	ImageAttributesLoader getImageAttributesLoader();

}

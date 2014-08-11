package com.df.blobstore.image;

import com.df.blobstore.bundle.BundleKey;
import com.df.blobstore.bundle.BundleService;

public interface ImageServiceRoute {

	BundleService getBundleService(ImageKey imageKey);

	BundleKey resolveBundleKey(ImageKey imageKey);

	ImageKey hash(ImageAttributes attributes);

	ImageAttributesLoader getImageAttributesLoader();

}

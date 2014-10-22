package com.tt.blobstore.image;

import com.tt.blobstore.bundle.BundleKey;
import com.tt.blobstore.bundle.BundleService;

public interface ImageServiceRoute {

	BundleService getBundleService(ImageKey imageKey);

	BundleService getThumbnailBundleService(ImageKey imageKey, int width, int heigth);

	BundleKey resolveBundleKey(ImageKey imageKey);

	BundleKey resolveThumbnailBundleKey(ImageKey imageKey, int width, int heigth);

	ImageKey hash(ImageAttributes attributes);

	ImageAttributesLoader getImageAttributesLoader();

}

package com.df.blobstore.image;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.df.blobstore.bundle.BundleKey;
import com.df.blobstore.bundle.BundleService;
import com.df.blobstore.image.exception.ImageException;

public class ImageServiceImpl implements ImageService {

	private ImageServiceRoute serviceRoute;

	public ImageServiceImpl(ImageServiceRoute serviceRoute) {
		this.serviceRoute = serviceRoute;
	}

	@Override
	public ImageKey uploadImage(InputStream in, String owner, String imageName) {
		Image image;
		try {
			image = ImageUtils.createImage(in, owner, imageName);
		} catch (IOException ex) {
			throw new ImageException(ex, "Failed to upload image");
		}
		image.getImageAttributes().setCreatedDate(new Date());
		ImageKey imageKey = serviceRoute.hash(image.getImageAttributes());
		BundleService bundleService = serviceRoute.getBundleService(imageKey);
		bundleService.addBlob(image, serviceRoute.resolveBundleKey(imageKey));
		return imageKey;
	}

	@Override
	public void deleteImage(ImageKey imageKey) {
		BundleService bundleService = serviceRoute.getBundleService(imageKey);
		BundleKey bundleKey = serviceRoute.resolveBundleKey(imageKey);
		bundleService.deleteBlob(bundleKey);
	}

	@Override
	public Image fetchImage(ImageKey imageKey) {
		Image image = new Image();
		BundleKey bundleKey = serviceRoute.resolveBundleKey(imageKey);
		BundleService bundleService = serviceRoute.getBundleService(imageKey);
		boolean succeed = bundleService.fetchBlob(image, bundleKey);
		if (succeed && image.getImageAttributes() == null) {
			ImageAttributes attributes = serviceRoute.getImageAttributesLoader().loadImageAttributes(imageKey);
			image.setImageAttributes(attributes);
		}
		return succeed ? image : null;
	}
}

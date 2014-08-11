package com.df.blobstore.image.http;

import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageKey;

public class DefaultImageLinkCreator implements ImageLinkCreator {

	private String imageRequestPrefix;

	public DefaultImageLinkCreator() {
	}

	public DefaultImageLinkCreator(String imageRequestPrefix) {
		this.imageRequestPrefix = imageRequestPrefix;
	}

	public void setImageRequestPrefix(String imageRequestPrefix) {
		this.imageRequestPrefix = imageRequestPrefix;
	}

	@Override
	public String createImageLink(ImageKey imageKey, ImageAttributes attributes) {
		String suffix = attributes.getFormat().name().toLowerCase();
		if (imageRequestPrefix != null) {
			return imageRequestPrefix + "/" + imageKey + "." + suffix;
		} else {
			return "/" + imageKey + "." + suffix;
		}
	}

}

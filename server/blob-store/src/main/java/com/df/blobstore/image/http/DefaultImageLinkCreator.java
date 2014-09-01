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
		String suffix = "";
		String link = "";
		if (attributes != null) {
			suffix = attributes.getFormat().getFileSuffix().toLowerCase();
		}
		if (imageRequestPrefix != null) {
			link = imageRequestPrefix + "/" + imageKey + "." + suffix;
		} else {
			link = "/" + imageKey + "." + suffix;
		}

		if (suffix.length() == 0) {
			link = link.substring(0, link.length() - 1);
		}
		return link;

	}

}

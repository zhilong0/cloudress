package com.df.blobstore.image.http;

import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.ImageService;
import com.df.blobstore.image.http.ImageLinkCreator;

public class DefaultImageDetailsFactory implements ImageDetailsFactory {

	private ImageLinkCreator imageLinkCreator;

	private ImageService imageService;

	public DefaultImageDetailsFactory(ImageLinkCreator imageLinkCreator, ImageService imageService) {
		this.imageLinkCreator = imageLinkCreator;
		this.imageService = imageService;
	}

	@Override
	public ImageDetails createImageDetails(ImageKey imageKey) {
		ImageAttributes attributes = imageService.getImageAttributes(imageKey);
		ImageDetails details = new ImageDetails(imageKey.getKey(), attributes);
		if (attributes == null) {
			return details;
		}
		details.setImageLink(imageLinkCreator.createImageLink(imageKey, attributes));
		return details;
	}

}

package com.tt.blobstore.image.http;

import com.tt.blobstore.image.ImageAttributes;
import com.tt.blobstore.image.ImageKey;
import com.tt.blobstore.image.ImageService;
import com.tt.blobstore.image.http.ImageLinkCreator;

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

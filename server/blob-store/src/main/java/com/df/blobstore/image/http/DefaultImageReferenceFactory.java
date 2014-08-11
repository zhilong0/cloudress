package com.df.blobstore.image.http;

import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageAttributesLoader;
import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.http.ImageLinkCreator;

public class DefaultImageReferenceFactory implements ImageReferenceFactory {

	private ImageLinkCreator imageLinkCreator;

	private ImageAttributesLoader imageAttributeLoader;

	public DefaultImageReferenceFactory(ImageLinkCreator imageLinkCreator, ImageAttributesLoader imageAttributeLoader) {
		this.imageLinkCreator = imageLinkCreator;
		this.imageAttributeLoader = imageAttributeLoader;
	}

	@Override
	public ImageReference createImageReference(ImageKey imageKey) {
		ImageAttributes attributes = imageAttributeLoader.loadImageAttributes(imageKey);
		ImageReference ref = new ImageReference();
		ref.setImageId(imageKey.getKey());
		ref.setWidth(attributes.getWidth());
		ref.setHeigth(attributes.getHeigth());
		ref.setFormat(attributes.getFormat().name());
		ref.setImageLink(imageLinkCreator.createImageLink(imageKey, attributes));
		return ref;
	}

}

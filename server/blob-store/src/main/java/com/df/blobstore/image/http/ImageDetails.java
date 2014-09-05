package com.df.blobstore.image.http;

import java.io.Serializable;

import com.df.blobstore.image.ImageAttributes;

public class ImageDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private String imageId;

	private String imageLink;

	private ImageAttributes attributes;

	ImageDetails() {
	}

	public ImageDetails(String imageId, ImageAttributes attributes) {
		this.imageId = imageId;
		this.attributes = attributes;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public ImageAttributes getAttributes() {
		return attributes;
	}

	public void setAttributes(ImageAttributes attributes) {
		this.attributes = attributes;
	}

}

package com.df.spec.locality.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.http.ImageDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ImageSet {

	private List<ImageDetails> images = new ArrayList<ImageDetails>();

	public void addImage(String imageId, ImageAttributes imageAttributes) {
		images.add(new ImageDetails(imageId, imageAttributes));
	}

	public void addImage(ImageDetails imageDetails) {
		images.add(imageDetails);
	}

	public void removeImage(String imageId) {
		for (ImageDetails image : this.images) {
			if (image.getImageId().equals(imageId)) {
				images.remove(imageId);
				break;
			}
		}
	}

	public boolean hasImageWithName(String imageName) {
		Assert.notNull(imageName);
		for (ImageDetails image : this.images) {
			ImageAttributes attributes = image.getAttributes();
			if (attributes != null && imageName.equals(attributes.getName())) {
				return true;
			}
		}
		return false;
	}

	public void clear() {
		images.clear();
	}

	public List<ImageDetails> getImages() {
		return images;
	}

	@JsonIgnore
	public int getImageCount() {
		return images == null ? 0 : images.size();
	}

	public String getCover() {
		if (images.size() > 0) {
			return images.get(0).getImageId();
		}
		return null;
	}
}
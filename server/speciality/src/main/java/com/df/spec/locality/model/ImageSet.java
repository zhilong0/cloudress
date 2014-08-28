package com.df.spec.locality.model;

import java.util.ArrayList;
import java.util.List;

public class ImageSet {

	private List<String> images = new ArrayList<String>();

	public void addImage(String imageId) {
		images.add(imageId);
	}

	public void removeImage(String imageId) {
		images.remove(imageId);
	}

	public void clear() {
		images.clear();
	}

	public List<String> getImages() {
		return images;
	}

	public int getImageCount() {
		return images == null ? 0 : images.size();
	}
}

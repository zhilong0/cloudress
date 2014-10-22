package com.tt.blobstore.image;

import java.io.InputStream;

public interface ImageService {

	public ImageKey uploadImage(InputStream in, String owner, String imageName);

	public void deleteImage(ImageKey imageKey);

	public Image fetchImage(ImageKey imageKey);

	public Image fetchImage(ImageKey imageKey, int width, int heigth);

	public ImageAttributes getImageAttributes(ImageKey imageKey);

}
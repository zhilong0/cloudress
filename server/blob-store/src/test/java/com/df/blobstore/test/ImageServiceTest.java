package com.df.blobstore.test;

import java.io.InputStream;

import org.junit.Test;

import junit.framework.TestCase;

import com.df.blobstore.image.Image;
import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.ImageServiceConfig;
import com.df.blobstore.image.ImageServiceImpl;
import com.df.blobstore.image.ImageServiceRoute;
import com.df.blobstore.image.mongo.MongoDBImageServiceRoute;

public class ImageServiceTest extends MongoDBAware {

	@Test
	public void testSaveWithThumbnails() {
		ImageServiceRoute route = new MongoDBImageServiceRoute(this.getDB(), "image_store");
		ImageServiceConfig config = new ImageServiceConfig();
		config.saveThumbnail(30, 30);
		ImageServiceImpl imageService = new ImageServiceImpl(route, config);
		String testImage = "bsd.jpg";
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(testImage);
		ImageKey key = null;
		try {
			key = imageService.uploadImage(in, "tester", testImage);
			Image image = imageService.fetchImage(key, 30, 30);
			TestCase.assertNotNull(image);
		} finally {
			if (key != null) {
				imageService.deleteImage(key);
			}
		}

	}
}

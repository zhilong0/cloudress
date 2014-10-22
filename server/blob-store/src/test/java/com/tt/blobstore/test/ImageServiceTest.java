package com.tt.blobstore.test;

import java.io.InputStream;

import org.junit.Test;

import junit.framework.TestCase;

import com.tt.blobstore.image.Image;
import com.tt.blobstore.image.ImageKey;
import com.tt.blobstore.image.ImageServiceConfig;
import com.tt.blobstore.image.ImageServiceImpl;
import com.tt.blobstore.image.ImageServiceRoute;
import com.tt.blobstore.image.mongo.MongoDBImageServiceRoute;

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

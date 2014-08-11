package com.df.blobstore.test;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import com.df.blobstore.bundle.BundleKey;
import com.df.blobstore.bundle.BundleService;
import com.df.blobstore.image.Image;
import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageFormat;
import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.mongo.MongoDBImageServiceRoute;

public class MongoDBBundleServiceTest extends MongoDBAware {
	@Test
	public void testSaveAndDeleteImage() {
		ImageAttributes attribute = new ImageAttributes("testName", 12, 24, ImageFormat.GIF);
		attribute.setOwner("test");
		attribute.setCreatedDate(new Date());
		String collectionName = "image_store";
		MongoDBImageServiceRoute serviceRoute = new MongoDBImageServiceRoute(this.getDB(), collectionName);
		ImageKey imageKey = serviceRoute.hash(attribute);
		BundleService bundleService = serviceRoute.getBundleService(imageKey);
		Image image = new Image(attribute, new byte[] { 12, 12 });
		BundleKey bundleKey = serviceRoute.resolveBundleKey(imageKey);
		bundleService.addBlob(image, bundleKey);
		Image found = new Image();
		bundleService.fetchBlob(found, bundleKey);
		TestCase.assertTrue(found.getBundleValue().getSize() > 0);
		bundleService.deleteBlob(bundleKey);
		TestCase.assertFalse(bundleService.fetchBlob(found, bundleKey));
	}
}

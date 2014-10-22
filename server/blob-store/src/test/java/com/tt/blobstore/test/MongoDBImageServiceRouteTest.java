package com.tt.blobstore.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.tt.blobstore.bundle.BundleKey;
import com.tt.blobstore.bundle.BundleService;
import com.tt.blobstore.image.Image;
import com.tt.blobstore.image.ImageAttributes;
import com.tt.blobstore.image.ImageFormat;
import com.tt.blobstore.image.ImageKey;
import com.tt.blobstore.image.ImageServiceRoute;
import com.tt.blobstore.image.mongo.MongoDBImageServiceRoute;

public class MongoDBImageServiceRouteTest extends MongoDBAware {

	@Test
	public void testHashImageKey() {
		ImageServiceRoute serviceRoute = new MongoDBImageServiceRoute(getDB(), "image_store");
		ImageAttributes metadata = new ImageAttributes("testName", 12, 24, ImageFormat.GIF);
		metadata.setOwner("testowner");
		ImageKey key = serviceRoute.hash(metadata);
		TestCase.assertNotNull(key);
	}

	@Test
	public void testImageAttributesLoader() {
		ImageServiceRoute serviceRoute = new MongoDBImageServiceRoute(getDB(), "image_store");
		ImageAttributes attributes = new ImageAttributes("testName3", 12, 24, ImageFormat.GIF);
		attributes.setOwner("testowner");
		ImageKey key = serviceRoute.hash(attributes);
		BundleService bundleService = serviceRoute.getBundleService(key);
		BundleKey bundleKey = serviceRoute.resolveBundleKey(key);
		Image image = new Image(attributes);
		bundleService.addBlob(image, bundleKey);
		ImageAttributes attr2 = serviceRoute.getImageAttributesLoader().loadImageAttributes(key);
		TestCase.assertEquals(attributes.getName(), attr2.getName());
		TestCase.assertEquals(attributes.getWidth(), attr2.getWidth());
		TestCase.assertEquals(attributes.getHeigth(), attr2.getHeigth());
		TestCase.assertEquals(attributes.getOwner(), attr2.getOwner());
	}

}

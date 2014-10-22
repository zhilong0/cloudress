package com.tt.blobstore.test;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.junit.Test;

import com.tt.blobstore.bundle.BundleKey;
import com.tt.blobstore.bundle.BundleService;
import com.tt.blobstore.image.Image;
import com.tt.blobstore.image.ImageKey;
import com.tt.blobstore.image.ImageServiceRoute;
import com.tt.blobstore.image.ImageUtils;
import com.tt.blobstore.image.fs.FileSystemImageServiceRoute;
import com.tt.blobstore.image.mongo.MongoDBImageServiceRoute;

public class ImageUtilsTest extends MongoDBAware {

	@Test
	public void testCreateImageToFileSystem() throws IOException {
		String testImage = "bsd.jpg";
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(testImage);
		Image image = ImageUtils.createImage(in, null, null);
		ImageServiceRoute serviceRoute = new FileSystemImageServiceRoute("./build/target/test_image_root");
		ImageKey imageKey = serviceRoute.hash(image.getImageAttributes());
		BundleService bundleService = serviceRoute.getBundleService(imageKey);
		BundleKey bundleKey = serviceRoute.resolveBundleKey(imageKey);
		bundleService.addBlob(image, bundleKey);
		image = new Image();
		bundleService.fetchBlob(image, serviceRoute.resolveBundleKey(imageKey));
		TestCase.assertTrue(image.getBundleValue().getSize() > 1);
	}

	@Test
	public void testCreateImageToMongoDB() throws IOException {
		String testImage = "bsd.jpg";
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(testImage);
		Image image = ImageUtils.createImage(in, null, null);
		ImageServiceRoute serviceRoute = new MongoDBImageServiceRoute(this.getDB(), "image_store");
		ImageKey imageKey = serviceRoute.hash(image.getImageAttributes());
		BundleService bundleService = serviceRoute.getBundleService(imageKey);
		BundleKey bundleKey = serviceRoute.resolveBundleKey(imageKey);
		bundleService.addBlob(image, bundleKey);
		image = new Image();
		bundleService.fetchBlob(image, serviceRoute.resolveBundleKey(imageKey));
		TestCase.assertTrue(image.getBundleValue().getSize() > 1);
	}

}

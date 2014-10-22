package com.tt.blobstore.test;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import com.tt.blobstore.bundle.BundleKey;
import com.tt.blobstore.bundle.BundleService;
import com.tt.blobstore.image.Image;
import com.tt.blobstore.image.ImageAttributes;
import com.tt.blobstore.image.ImageFormat;
import com.tt.blobstore.image.ImageKey;
import com.tt.blobstore.image.fs.FileSystemImageServiceRoute;

public class FileSystemBundleServiceTest {

	@Test
	public void testSaveAndDeleteImage() {
		ImageAttributes attribute = new ImageAttributes("testName", 12, 24, ImageFormat.GIF);
		attribute.setOwner("test");
		attribute.setCreatedDate(new Date());
		FileSystemImageServiceRoute serviceRoute = new FileSystemImageServiceRoute("./build/target/test_image_root");
		ImageKey imageKey = serviceRoute.hash(attribute);
		BundleService bundleService = serviceRoute.getBundleService(imageKey);
		Image image = new Image(attribute, new byte[] { 12, 12 });
		bundleService.addBlob(image, serviceRoute.resolveBundleKey(imageKey));
		BundleKey bundleKey = serviceRoute.resolveBundleKey(imageKey);
		Image found = new Image();
		bundleService.fetchBlob(found, bundleKey);
		TestCase.assertTrue(found.getBundleValue().getSize() > 0);
		bundleService.deleteBlob(bundleKey);
		TestCase.assertFalse(bundleService.fetchBlob(found, bundleKey));
	}
}

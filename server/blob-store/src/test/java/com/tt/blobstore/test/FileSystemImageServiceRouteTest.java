package com.tt.blobstore.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.tt.blobstore.image.ImageAttributes;
import com.tt.blobstore.image.ImageFormat;
import com.tt.blobstore.image.ImageKey;
import com.tt.blobstore.image.fs.FileSystemImageServiceRoute;

public class FileSystemImageServiceRouteTest {

	@Test
	public void testHashImageKey() {
		FileSystemImageServiceRoute serviceRoute = new FileSystemImageServiceRoute("./build/target");
		ImageAttributes metadata = new ImageAttributes("testName", 12, 24, ImageFormat.GIF);
		metadata.setOwner("testowner");
		ImageKey key = serviceRoute.hash(metadata);
		metadata = serviceRoute.getImageAttributesLoader().loadImageAttributes(key);
		TestCase.assertEquals(12, metadata.getWidth());
		TestCase.assertEquals(24, metadata.getHeigth());
		TestCase.assertEquals(ImageFormat.GIF, metadata.getFormat());
		TestCase.assertEquals("testowner", metadata.getOwner());
		TestCase.assertEquals("testName", metadata.getName());
	}

	@Test
	public void testHashImageKeyWihoutOwnerAttr() {
		FileSystemImageServiceRoute serviceRoute = new FileSystemImageServiceRoute("./build/target");
		ImageAttributes metadata = new ImageAttributes("testName2", 12, 24, ImageFormat.GIF);
		ImageKey key = serviceRoute.hash(metadata);
		metadata = serviceRoute.loadImageAttributes(key);
		TestCase.assertEquals(metadata.getOwner(), null);
	}

	@Test
	public void testImageAttributesLoader() {
		FileSystemImageServiceRoute serviceRoute = new FileSystemImageServiceRoute("./build/target");
		ImageAttributes metadata = new ImageAttributes("testName3", 12, 24, ImageFormat.GIF);
		metadata.setOwner("testowner");
		ImageKey key = serviceRoute.hash(metadata);
		System.out.println("Hash key is:" + key.getKey());
		ImageAttributes metadata2 = serviceRoute.loadImageAttributes(key);
		TestCase.assertEquals(metadata.getName(), metadata2.getName());
		TestCase.assertEquals(metadata.getWidth(), metadata2.getWidth());
		TestCase.assertEquals(metadata.getHeigth(), metadata2.getHeigth());
	}
}

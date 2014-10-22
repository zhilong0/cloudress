package com.tt.spec.locality.provision;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import com.google.common.io.ByteStreams;

class ImageSource {

	private String image;

	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	private Resource resource;

	public ImageSource(String imageBaseUri, String image) {
		Assert.notNull(imageBaseUri, image);
		this.image = image;
		resource = resourceLoader.getResource(imageBaseUri + image);
	}

	public String getImageName() {
		String[] parts = image.split("/|\\\\");
		return parts[parts.length - 1];
	}

	public URI getUri() throws IOException {
		return resource.getURI();

	}

	public InputStream read() throws IOException {
		return resource.getInputStream();
	}

	public byte[] readToBytes() throws IOException {
		return ByteStreams.toByteArray(read());
	}

}

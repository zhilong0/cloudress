package com.df.blobstore.image.fs;

import java.util.UUID;

import com.df.blobstore.image.ImageAttributes;

public class UUIDGenerator implements IdGenerator {

	@Override
	public String randomUniqueId(ImageAttributes attributes) {
		return UUID.randomUUID().toString();
	}

}

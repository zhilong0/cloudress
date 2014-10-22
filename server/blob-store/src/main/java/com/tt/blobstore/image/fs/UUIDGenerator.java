package com.tt.blobstore.image.fs;

import java.util.UUID;

import com.tt.blobstore.image.ImageAttributes;

public class UUIDGenerator implements IdGenerator {

	@Override
	public String randomUniqueId(ImageAttributes attributes) {
		return UUID.randomUUID().toString();
	}

}

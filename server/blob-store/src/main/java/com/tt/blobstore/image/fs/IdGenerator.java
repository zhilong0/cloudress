package com.tt.blobstore.image.fs;

import com.tt.blobstore.image.ImageAttributes;

public interface IdGenerator {

	String randomUniqueId(ImageAttributes attributes);
}

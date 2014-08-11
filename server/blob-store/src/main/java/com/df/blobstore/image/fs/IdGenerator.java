package com.df.blobstore.image.fs;

import com.df.blobstore.image.ImageAttributes;

public interface IdGenerator {

	String randomUniqueId(ImageAttributes attributes);
}

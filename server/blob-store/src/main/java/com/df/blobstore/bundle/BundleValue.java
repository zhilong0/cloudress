package com.df.blobstore.bundle;

import java.io.InputStream;

public interface BundleValue {

	int getSize();

	InputStream getDataInBundle();

}

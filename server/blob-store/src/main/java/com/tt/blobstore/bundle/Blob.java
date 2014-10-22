package com.tt.blobstore.bundle;

import java.io.IOException;
import java.io.InputStream;

public interface Blob {

	BundleValue getBundleValue();

	BlobDescriptor getBlobDescriptor();

	void setBlobDescriptor(BlobDescriptor descriptor);

	void readBundleValue(InputStream input) throws IOException;

}

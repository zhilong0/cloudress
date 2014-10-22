package com.tt.blobstore.bundle.fs;

import com.tt.blobstore.bundle.BundleKey;

public class FileSystemBundleKey implements BundleKey {

	private String filePath;

	public FileSystemBundleKey(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public Object getKeyInBundle() {
		return filePath;
	}

}

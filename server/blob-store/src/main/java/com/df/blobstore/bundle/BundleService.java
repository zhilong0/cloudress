package com.df.blobstore.bundle;

public interface BundleService {

	void addBlob(Blob blob, BundleKey key);

	boolean fetchBlob(Blob blob, BundleKey key);

	void deleteBlob(BundleKey key);

	void updateBlob(Blob blob, BundleKey key);

	boolean hasBlob(BundleKey key);

}

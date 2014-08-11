package com.df.blobstore.bundle.mongo;

import org.bson.types.ObjectId;

import com.df.blobstore.bundle.BundleKey;

public class MongoDBBundleKey implements BundleKey {

	private ObjectId objectId;

	public MongoDBBundleKey(ObjectId objectId) {
		this.objectId = objectId;
	}

	@Override
	public Object getKeyInBundle() {
		return objectId;
	}

}

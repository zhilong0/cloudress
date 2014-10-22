package com.tt.blobstore.test;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.tt.blobstore.bundle.BlobStoreException;

public abstract class MongoDBAware {
	private static MongoClient client;

	protected synchronized DB getDB() {
		if (client == null) {
			try {
				client = new MongoClient("127.0.0.1:27017");
			} catch (UnknownHostException ex) {
				throw new BlobStoreException(ex);
			}
		}
		return client.getDB("blob_store");
	}
}

package com.df.blobstore.test;

import java.net.UnknownHostException;

import com.df.blobstore.bundle.BlobStoreException;
import com.mongodb.DB;
import com.mongodb.MongoClient;

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

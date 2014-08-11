package com.df.blobstore.bundle.mongo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import org.bson.types.ObjectId;

import com.df.blobstore.bundle.Blob;
import com.df.blobstore.bundle.BlobDescriptor;
import com.df.blobstore.bundle.BlobStoreException;
import com.df.blobstore.bundle.BundleKey;
import com.df.blobstore.bundle.BundleService;
import com.google.common.io.ByteStreams;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;

public class MongoDBBundleService implements BundleService {

	private DB db;

	private String collectionName;

	private static final String BLOB_DATA_FILED = "blob_data";

	public MongoDBBundleService(DB db, String collectionName) {
		this.db = db;
		this.collectionName = collectionName;
	}

	@Override
	public void addBlob(Blob blob, BundleKey bundleKey) {
		if (!(bundleKey instanceof MongoDBBundleKey)) {
			throw new BlobStoreException("Bundle Key must be type of %s", MongoDBBundleKey.class.getCanonicalName());
		}
		ObjectId objectId = (ObjectId) bundleKey.getKeyInBundle();
		BasicDBObject query = new BasicDBObject();
		query.append("_id", objectId);
		BasicDBObject update = new BasicDBObject();
		update.append("_id", objectId);
		ByteArrayOutputStream copy = new ByteArrayOutputStream();
		byte[] bytes = null;
		if (blob.getBundleValue() != null) {
			try {
				ByteStreams.copy(blob.getBundleValue().getDataInBundle(), copy);
				bytes = copy.toByteArray();
			} catch (IOException ex) {
				throw new BlobStoreException(ex);
			}
		} else {
			bytes = new byte[0];
		}
		update.append(BLOB_DATA_FILED, bytes);
		BlobDescriptor blobDescriptor = blob.getBlobDescriptor();
		if (blobDescriptor != null) {
			String[] attributeNames = blobDescriptor.getAttributeNames();
			for (String attributeName : attributeNames) {
				update.append(attributeName, blobDescriptor.getAttribute(attributeName));
			}
		}
		DBCollection collection = db.getCollection(collectionName);
		collection.update(query, update, true, false);
	}

	@Override
	public boolean fetchBlob(Blob blob, BundleKey bundleKey) {
		ObjectId objectId = (ObjectId) bundleKey.getKeyInBundle();
		DBCollection collection = db.getCollection(collectionName);
		BasicDBObject query = new BasicDBObject();
		query.append("_id", objectId);
		DBObject found = collection.findOne(query);
		if (found != null) {
			byte[] data = (byte[]) found.get(BLOB_DATA_FILED);
			try {
				blob.readBundleValue(new ByteArrayInputStream(data));
				found.removeField("_id");
				found.removeField(BLOB_DATA_FILED);
				Set<String> keys = found.keySet();
				BlobDescriptor descriptor = new BlobDescriptor();
				for (String key : keys) {
					if (!key.equals("_id") && !key.equals(BLOB_DATA_FILED)) {
						descriptor.setAttribute(key, found.get(key));
					}
				}
				blob.setBlobDescriptor(descriptor);
				return true;
			} catch (IOException ex) {
				throw new BlobStoreException(ex);
			}
		} else {
			return false;
		}
	}

	@Override
	public void deleteBlob(BundleKey bundleKey) {
		ObjectId objectId = (ObjectId) bundleKey.getKeyInBundle();
		DBCollection collection = db.getCollection(collectionName);
		BasicDBObject query = new BasicDBObject();
		query.append("_id", objectId);
		collection.remove(query, WriteConcern.JOURNALED);
	}

	@Override
	public boolean hasBlob(BundleKey bundleKey) {
		ObjectId objectId = (ObjectId) bundleKey.getKeyInBundle();
		DBCollection collection = db.getCollection(collectionName);
		BasicDBObject query = new BasicDBObject();
		query.append("_id", objectId);
		return collection.count(query) >= 1;
	}

	@Override
	public void updateBlob(Blob blob, BundleKey key) {
		this.addBlob(blob, key);
	}

}

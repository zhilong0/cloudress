package com.df.blobstore.image.mongo;

import org.bson.types.ObjectId;

import com.df.blobstore.bundle.BundleKey;
import com.df.blobstore.bundle.BundleService;
import com.df.blobstore.bundle.mongo.MongoDBBundleKey;
import com.df.blobstore.bundle.mongo.MongoDBBundleService;
import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageAttributesLoader;
import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.ImageServiceRoute;
import com.mongodb.DB;

public class MongoDBImageServiceRoute implements ImageServiceRoute {

	private DB db;

	private String collectionName;

	private ImageAttributesLoader imageAttributesLoader;

	public MongoDBImageServiceRoute(DB db, String collectionName) {
		this(db, collectionName, new MongoDBImageAttributesLoader(db, collectionName));
	}

	public MongoDBImageServiceRoute(DB db, String collectionName, ImageAttributesLoader imageAttributesLoader) {
		this.db = db;
		this.collectionName = collectionName;
		this.imageAttributesLoader = imageAttributesLoader;
	}

	@Override
	public BundleService getBundleService(ImageKey imageKey) {
		return new MongoDBBundleService(db, collectionName);
	}

	@Override
	public BundleKey resolveBundleKey(ImageKey imageKey) {
		return new MongoDBBundleKey(new ObjectId(imageKey.getKey()));
	}

	@Override
	public ImageKey hash(ImageAttributes attributes) {
		return new ImageKey(new ObjectId().toHexString());
	}

	@Override
	public ImageAttributesLoader getImageAttributesLoader() {
		return imageAttributesLoader;
	}

	@Override
	public BundleService getThumbnailBundleService(ImageKey imageKey, int width, int heigth) {
		String cn = String.format("%s_%s_%d_%d", collectionName, "thumbnails", width, heigth);
		return new MongoDBBundleService(db, cn);
	}

	@Override
	public BundleKey resolveThumbnailBundleKey(ImageKey imageKey, int width, int heigth) {
		return this.resolveBundleKey(imageKey);
	}
}

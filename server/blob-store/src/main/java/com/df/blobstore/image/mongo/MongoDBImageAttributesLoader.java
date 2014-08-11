package com.df.blobstore.image.mongo;

import java.util.Date;

import org.bson.types.ObjectId;

import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageAttributesLoader;
import com.df.blobstore.image.ImageFormat;
import com.df.blobstore.image.ImageKey;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MongoDBImageAttributesLoader implements ImageAttributesLoader {
	public static final String NAME_FIELD = "name";

	public static final String WIDTH_FIELD = "width";

	public static final String HEIGTH_FIELD = "heigth";

	public static final String FORMAT_FIELD = "format";

	public static final String OWNER_FIELD = "owner";

	public static final String CREATED_DATE_FIELD = "created";

	private DB db;

	private String collectionName;

	public MongoDBImageAttributesLoader(DB db, String collectionName) {
		this.db = db;
		this.collectionName = collectionName;
	}

	@Override
	public ImageAttributes loadImageAttributes(ImageKey imageKey) {
		ObjectId objectId = new ObjectId(imageKey.getKey());
		BasicDBObject query = new BasicDBObject();
		query.append("_id", objectId);
		DBCollection dbCollection = db.getCollection(collectionName);
		DBObject found = dbCollection.findOne(query);
		if (found == null) {
			return null;
		}
		String name = (String) found.get(NAME_FIELD);
		ImageAttributes attributes = new ImageAttributes(name);
		attributes.setWidth((Integer) found.get(WIDTH_FIELD) == null ? 0 : (Integer) found.get(WIDTH_FIELD));
		attributes.setHeigth((Integer) found.get(HEIGTH_FIELD) == null ? 0 : (Integer) found.get(HEIGTH_FIELD));
		attributes.setOwner((String) found.get(OWNER_FIELD));
		attributes.setCreatedDate((Date) found.get(CREATED_DATE_FIELD));
		String format = (String) found.get(FORMAT_FIELD);
		if (format != null) {
			attributes.setFormat(ImageFormat.valueOf(format));
		}
		return attributes;
	}

}

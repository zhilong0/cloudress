package com.tt.idm.registration;

import org.mongodb.morphia.Datastore;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class TokenStoreImpl implements TokenStore {

	private Datastore ds;

	private String collectionName = "token_store";

	private static final String TOKEN_PROPERTY = "_id";

	private static final String WHEN_EXPIRED_PROPERTY = "when_expired";

	private static final String STICK_PROPERTY = "stick";

	public TokenStoreImpl(Datastore ds) {
		this.ds = ds;
	}

	public void setCollectionName(String collectionName) {
		if (collectionName != null) {
			this.collectionName = collectionName;
		}
	}

	@Override
	public Token getToken(String token) {
		if (token == null) {
			return null;
		}
		DB db = ds.getDB();
		DBCollection collection = db.getCollection(collectionName);
		BasicDBObject query = new BasicDBObject();
		query.append(TOKEN_PROPERTY, token);
		DBObject found = collection.findOne(query);
		if (found == null) {
			return null;
		}
		Token t = new Token(token, (Long) found.get(WHEN_EXPIRED_PROPERTY));
		t.setStick((String) found.get(STICK_PROPERTY));
		return t;
	}

	@Override
	public boolean save(Token token) {
		DB db = ds.getDB();
		DBCollection collection = db.getCollection(collectionName);
		BasicDBObject object = new BasicDBObject();
		object.append(TOKEN_PROPERTY, token.getToken());
		object.append(WHEN_EXPIRED_PROPERTY, token.getWhenExpired());
		object.append(STICK_PROPERTY, token.getStick());
		return collection.save(object).getN() >= 1;
	}

	@Override
	public boolean expire(String token) {
		DB db = ds.getDB();
		DBCollection collection = db.getCollection(collectionName);
		BasicDBObject query = new BasicDBObject();
		query.append(TOKEN_PROPERTY, token);
		return collection.remove(query).getN() >= 1;
	}

}

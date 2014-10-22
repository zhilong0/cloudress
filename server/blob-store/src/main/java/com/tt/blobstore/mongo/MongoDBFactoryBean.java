package com.tt.blobstore.mongo;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDBFactoryBean extends AbstractFactoryBean<DB> {

	private String dbName;

	private MongoClient mongoClient;

	@Override
	public Class<?> getObjectType() {
		return DB.class;
	}

	@Override
	protected DB createInstance() throws Exception {
		return mongoClient.getDB(dbName);
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (this.mongoClient == null) {
			throw new IllegalArgumentException("mongoClient property must be set");
		}
		if (this.dbName == null) {
			throw new IllegalArgumentException("dbName property must be set");
		}
	}

}

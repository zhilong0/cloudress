package com.df.idm.morphia;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.MongoClient;

public class DataStoreFactoryBean extends AbstractFactoryBean<Datastore> {

	private Morphia morphia;

	private MongoClient mongoClient;

	private String dbName;

	@Override
	public Class<?> getObjectType() {
		return Datastore.class;
	}

	@Override
	protected Datastore createInstance() throws Exception {
		Datastore dataStore = morphia.createDatastore(mongoClient, dbName);
		dataStore.ensureIndexes();
		return dataStore;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (mongoClient == null) {
			throw new IllegalStateException("mongo client is not set");
		}
		if (morphia == null) {
			throw new IllegalStateException("morphia is not set");
		}
	}

	public void setMorphia(Morphia morphia) {
		this.morphia = morphia;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
}

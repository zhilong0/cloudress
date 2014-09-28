package com.df.common.morphia;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;
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
		dataStore.ensureIndexes(true);
		changeDatastoreProvider(morphia, dataStore);
		return dataStore;
	}

	private void changeDatastoreProvider(Morphia morphia, Datastore datastore) throws Exception {
		Mapper mapper = morphia.getMapper();
		Field field = Mapper.class.getDeclaredField("datastoreProvider");
		if (field == null) {
			throw new IllegalStateException("morphia version is not compliant, is it changed?");
		}
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(mapper, new InheritedDatastoreProvider(datastore));
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

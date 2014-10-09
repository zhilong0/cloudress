package com.df.spec.locality.dao.mongo;

import java.util.List;
import java.util.Properties;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.df.spec.locality.model.Approvable;
import com.df.spec.locality.model.Approvable.Status;
import com.df.spec.locality.model.Constants;
import com.mongodb.MongoClient;

public class BaseDao<T, K> extends BasicDAO<T, K> {
	public BaseDao(Datastore ds) {
		super(ds);
	}

	public BaseDao(MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
	}

	public int update(Class<T> type, Object id, Properties properties) {
		Query<T> filter = this.getDatastore().createQuery(type).filter(Mapper.ID_KEY, id);
		return this.update(type, filter, properties);
	}

	public int update(Class<T> type, Query<T> filter, Properties properties) {
		if (properties.size() == 0) {
			return 0;
		}
		Query<T> query = this.getDatastore().createQuery(type);
		UpdateOperations<T> updateOperations = this.getDatastore().createUpdateOperations(type);
		for (Object key : properties.keySet()) {
			Object value = properties.get(key);
			if (value == null) {
				updateOperations.unset(key.toString());
			} else {
				updateOperations.set(key.toString(), value);
			}
		}
		return this.update(query, updateOperations).getUpdatedCount();
	}

	public <AT extends Approvable> List<AT> getWaitList(Class<AT> type, int offset, int limit) {
		Query<AT> query = this.getDatastore().createQuery(type);
		query.filter(Constants.APPROVABLE.STATUS, Status.WAIT_FOR_APPROVE);
		query.offset(offset);
		query.limit(limit);
		return query.asList();
	}
}

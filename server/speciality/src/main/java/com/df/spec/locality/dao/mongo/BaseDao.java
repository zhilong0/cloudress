package com.df.spec.locality.dao.mongo;

import java.util.Properties;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
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

	public <AT extends Approvable> boolean approve(AT object, String approver) {
		Key<AT> key = this.getDs().getMapper().getKey(object);
		@SuppressWarnings("unchecked")
		UpdateOperations<AT> updateOperations = (UpdateOperations<AT>) this.getDatastore().createUpdateOperations(object.getClass());
		updateOperations.set(Constants.APPROVABLE.APPROVED_BY, object.getApprovedBy());
		updateOperations.set(Constants.SPECIALITY.APPROVED_TIME, object.getApprovedTime());
		updateOperations.set(Constants.SPECIALITY.STATUS, Status.APPROVED);
		updateOperations.unset(Constants.SPECIALITY.REJECT_REASON);
		return this.getDatastore().update(key, updateOperations).getUpdatedExisting();
	}

	public <AT extends Approvable> boolean reject(AT object, String approver, String rejectReason) {
		Key<AT> key = this.getDs().getMapper().getKey(object);
		@SuppressWarnings("unchecked")
		UpdateOperations<AT> updateOperations = (UpdateOperations<AT>) this.getDatastore().createUpdateOperations(object.getClass());
		updateOperations.set(Constants.APPROVABLE.APPROVED_BY, object.getApprovedBy());
		updateOperations.set(Constants.SPECIALITY.APPROVED_TIME, object.getApprovedTime());
		updateOperations.set(Constants.SPECIALITY.STATUS, Status.REJECTED);
		updateOperations.set(Constants.SPECIALITY.REJECT_REASON, rejectReason);
		return this.getDatastore().update(key, updateOperations).getUpdatedExisting();
	}
}

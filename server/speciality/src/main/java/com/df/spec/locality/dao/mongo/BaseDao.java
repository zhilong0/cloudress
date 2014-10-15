package com.df.spec.locality.dao.mongo;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.df.spec.locality.dao.DaoTemplate;
import com.df.spec.locality.model.Approvable;
import com.df.spec.locality.model.Approvable.Status;
import com.df.spec.locality.model.Constants;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

public class BaseDao<T, K> extends BasicDAO<T, K> implements DaoTemplate {
	public BaseDao(Datastore ds) {
		super(ds);
	}

	public BaseDao(MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
	}

	public <V> int update(Class<V> type, Query<V> filter, Map<String, Object> properties) {
		if (properties.size() == 0) {
			return 0;
		}
		Query<V> query = this.getDatastore().createQuery(type);
		UpdateOperations<V> updateOperations = this.getDatastore().createUpdateOperations(type);
		for (Object key : properties.keySet()) {
			Object value = properties.get(key);
			if (value == null) {
				updateOperations.unset(key.toString());
			} else {
				updateOperations.set(key.toString(), value);
			}
		}
		return this.getDatastore().update(query, updateOperations).getUpdatedCount();
	}

	public <AT extends Approvable> List<AT> getWaitList(Class<AT> type, int offset, int limit) {
		Query<AT> query = this.getDatastore().createQuery(type);
		query.filter(Constants.APPROVABLE.STATUS, Status.WAIT_FOR_APPROVE);
		query.offset(offset);
		query.limit(limit);
		return query.asList();
	}

	@Override
	public <V> V add(V entity) {
		this.getDatastore().save(entity, WriteConcern.JOURNALED);
		return entity;
	}

	@Override
	public <V> boolean deleteById(Class<V> type, Object entityId) {
		Query<V> query = this.getDatastore().createQuery(type);
		MappedField mapperField = this.getDs().getMapper().getMappedClass(type).getMappedIdField();
		if (mapperField.getType() == ObjectId.class && entityId instanceof String) {
			if (ObjectId.isValid(entityId.toString())) {
				query.filter("_id", new ObjectId((String) entityId));
			} else {
				return false;
			}
		} else {

			query.filter("_id", entityId);
		}
		return this.getDatastore().delete(query).getN() >= 1;
	}

	@Override
	public <V> boolean update(Class<V> type, Object entityId, Map<String, Object> properties) {
		Query<V> filter = this.getDatastore().createQuery(type).filter(Mapper.ID_KEY, entityId);
		return this.update(type, filter, properties) >= 1;
	}

	@Override
	public <V> V findById(Class<V> type, Object entityId) {
		Query<V> query = this.getDatastore().createQuery(type);
		MappedField mapperField = this.getDs().getMapper().getMappedClass(type).getMappedIdField();
		if (mapperField.getType() == ObjectId.class && entityId instanceof String) {
			if (ObjectId.isValid(entityId.toString())) {
				query.filter("_id", new ObjectId((String) entityId));
			} else {
				return null;
			}
		} else {

			query.filter("_id", entityId);
		}
		return query.get();
	}
}

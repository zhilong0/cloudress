package com.df.idm.dao.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.CriteriaContainerImpl;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.df.idm.dao.PermissionDao;
import com.df.idm.model.Constants;
import com.df.idm.model.Permission;
import com.mongodb.MongoClient;

public class PermissionDaoImpl extends BasicDAO<Permission, ObjectId> implements PermissionDao {

	public PermissionDaoImpl(Datastore ds) {
		super(ds);
	}

	public PermissionDaoImpl(MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
	}

	@Override
	public Permission insert(Permission permission) {
		this.save(permission);
		return permission;
	}

	@Override
	public boolean delete(String domain, String name) {
		Query<Permission> query = this.createQuery();
		query.filter(Constants.PERMISSION.DOMAIN_PROPERTY, domain);
		query.filter(Constants.PERMISSION.NAME_PROPERTY, name);
		return this.deleteByQuery(query).getN() > 0;
	}

	@Override
	public boolean update(Permission permssion) {
		Query<Permission> query = this.createQuery();
		query.filter(Constants.PERMISSION.DOMAIN_PROPERTY, permssion.getDomain());
		query.filter(Constants.PERMISSION.NAME_PROPERTY, permssion.getName());
		UpdateOperations<Permission> updateOperations = this.createUpdateOperations();
		if (permssion.getDescription() != null) {
			updateOperations.set(Constants.PERMISSION.DESCRIPTION_PROPERTY, permssion.getDescription());
		} else {
			updateOperations.unset(Constants.PERMISSION.DESCRIPTION_PROPERTY);

		}
		return this.update(query, updateOperations).getUpdatedCount() > 0;

	}

	@Override
	public Permission find(String domain, String name) {
		Query<Permission> query = this.createQuery();
		query.filter(Constants.PERMISSION.DOMAIN_PROPERTY, domain);
		query.filter(Constants.PERMISSION.NAME_PROPERTY, name);
		return query.get();
	}

	@Override
	public List<Permission> getPermissionList(String domain) {
		Query<Permission> query = this.createQuery();
		query.filter(Constants.PERMISSION.DOMAIN_PROPERTY, domain);
		return query.asList();
	}

	@Override
	public List<Permission> getPermissionList(List<Permission> permssions) {
		if (permssions.isEmpty()) {
			return new ArrayList<Permission>();
		}
		Query<Permission> query = this.createQuery();
		ArrayList<Criteria> criterias = new ArrayList<Criteria>();
		for (Permission permssion : permssions) {
			Query<Permission> q = this.createQuery();
			CriteriaContainerImpl c1 = q.criteria(Constants.PERMISSION.DOMAIN_PROPERTY).equal(permssion.getDomain());
			CriteriaContainerImpl c2 = q.criteria(Constants.PERMISSION.NAME_PROPERTY).equal(permssion.getName());
			criterias.add(q.and(c1, c2));
		}
		query.or(criterias.toArray(new Criteria[0]));
		return query.asList();
	}

}

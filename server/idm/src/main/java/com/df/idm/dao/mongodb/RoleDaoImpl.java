package com.df.idm.dao.mongodb;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.df.idm.dao.RoleDao;
import com.df.idm.model.Role;
import com.df.idm.model.User;
import com.mongodb.MongoClient;

public class RoleDaoImpl extends BasicDAO<User, ObjectId> implements RoleDao {

	public RoleDaoImpl(Datastore ds) {
		super(ds);
	}

	public RoleDaoImpl(MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
	}

	@Override
	public void insertRole(User user, Role role) {
	}

	@Override
	public void deleteRole(User user, Role role) {

	}

	@Override
	public Role findRole(String roleName) {
		return null;
	}

}

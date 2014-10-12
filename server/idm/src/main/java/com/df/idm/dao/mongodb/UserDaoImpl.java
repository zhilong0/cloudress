package com.df.idm.dao.mongodb;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.df.idm.dao.Property;
import com.df.idm.dao.UserDao;
import com.df.idm.model.Constants;
import com.df.idm.model.ExternalUserReference.Provider;
import com.df.idm.model.User;
import com.mongodb.MongoClient;

public class UserDaoImpl extends BasicDAO<User, ObjectId> implements UserDao {

	public UserDaoImpl(Datastore ds) {
		super(ds);
	}

	public UserDaoImpl(MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
	}

	@Override
	public User getUserById(String id) {
		return this.getDatastore().get(User.class, new ObjectId(id));
	}

	@Override
	public User getUserByEmail(String email) {
		return this.getDatastore().find(User.class, USER.EMAIL_PROPERTY, email).get();
	}

	@Override
	public User getUserByCellphone(String cellphone) {
		return this.getDatastore().find(User.class, USER.CELL_PHONE_PROPERTY, cellphone).get();
	}

	@Override
	public User getUserByExternalId(String externalId, Provider provider) {
		Query<User> query = this.getDatastore().createQuery(User.class);
		query.filter(USER.EXTERNAL_USER_ID_PROPERTY + " =", externalId);
		query.filter(USER.EXTERNAL_USER_PROVIDER_PROPERTY + " =", provider);
		return query.get();
	}

	@Override
	public int updateUserPassword(String userCode, String newEncodedPassword) {
		UpdateOperations<User> ops = this.getDatastore().createUpdateOperations(User.class);
		ops.set(USER.PASSWORD_PROPERTY, newEncodedPassword);
		Query<User> query = this.createQuery();
		query.filter(Constants.USER.CODE_PROPERTY, userCode);
		return this.getDatastore().update(query, ops).getUpdatedCount();
	}

	@Override
	public void insertUser(User user) {
		this.getDatastore().save(user);
	}

	@Override
	public int updateUser(User user) {
		Query<User> query = this.createQuery();
		user.setChangedTime(new Date());
		query.filter(USER.CODE_PROPERTY, user.getCode());
		return this.getDatastore().updateFirst(query, user, false).getUpdatedCount();
	}

	@Override
	public User getUserByCode(String code) {
		return this.getDatastore().find(User.class, USER.CODE_PROPERTY, code).get();
	}

	@Override
	public int updateUserProperties(String userCode, Property<?>... properties) {
		Query<User> query = this.createQuery();
		query.filter(Constants.USER.CODE_PROPERTY, userCode);
		if (properties == null || properties.length == 0) {
			return 0;
		}
		UpdateOperations<User> ops = this.getDatastore().createUpdateOperations(User.class);
		for (Property<?> p : properties) {
			ops.set(p.getName(), p.getValue());
		}
		return this.getDatastore().update(query, ops).getUpdatedCount();
	}

	@Override
	public boolean deleteUserById(String userId) {
		return this.deleteById(new ObjectId(userId)).getN() >= 1;
	}

	@Override
	public boolean deleteUserByCode(String code) {
		Query<User> query = this.createQuery();
		query.filter(Constants.USER.CODE_PROPERTY, code);
		return this.deleteByQuery(query).getN() >= 1;
	}

	@Override
	public List<User> getUserList(int offset, int limit) {
		Query<User> query = this.createQuery();
		query.filter(Constants.USER.IS_DISABLED_PROPERTY, false);
		query.offset(offset);
		query.limit(limit);
		return query.asList();
	}
}

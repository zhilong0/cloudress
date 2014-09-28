package com.df.idm.dao.mongodb;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.df.idm.dao.PermissionDao;
import com.df.idm.dao.RoleDao;
import com.df.idm.model.Constants;
import com.df.idm.model.Permission;
import com.df.idm.model.Role;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

public class RoleDaoImpl extends BasicDAO<Role, ObjectId> implements RoleDao {

	private PermissionDao permissionDao;

	public RoleDaoImpl(PermissionDao permissionDao, Datastore ds) {
		super(ds);
		this.permissionDao = permissionDao;
	}

	public RoleDaoImpl(PermissionDao permissionDao, MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
		this.permissionDao = permissionDao;
	}

	@Override
	public Role findRole(String roleName) {
		Query<Role> query = this.createQuery();
		query.filter(Constants.ROLE.NAME_PROPERTY, roleName);
		return query.get();
	}

	@Override
	public Role insertRole(Role role) {
		ArrayList<Permission> validPermissions = new ArrayList<Permission>();
		for (Permission permission : role.getPermissions()) {
			if (this.getPermissionId(permission) != null) {
				validPermissions.add(permission);
			}
		}
		role.setPermissions(validPermissions);
		this.save(role, WriteConcern.JOURNALED);
		return role;
	}

	@Override
	public boolean deleteRole(String roleName) {
		Query<Role> query = this.createQuery();
		query.filter(Constants.ROLE.NAME_PROPERTY, roleName);
		return this.deleteByQuery(query).getN() > 0;
	}

	@Override
	public List<Role> getRoleList() {
		Query<Role> query = this.createQuery();
		return query.asList();
	}

	@Override
	public List<String> getRoleIdentifierListWithPermission(Permission permission) {
		ObjectId id = this.getPermissionId(permission);
		List<String> roles = new ArrayList<String>();
		if (id == null) {
			return roles;
		}
		BasicDBObject query = new BasicDBObject();
		BasicDBObject elementMatch = new BasicDBObject();
		BasicDBObject objectEqual = new BasicDBObject();
		objectEqual.put("$eq", id);
		elementMatch.put("$elemMatch", objectEqual);
		query.append(Constants.ROLE.PERMISSIONS_PROPERTY, elementMatch);
		DBCursor cursor = this.getCollection().find(query);
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			String roleName = (String) dbObject.get(Constants.ROLE.NAME_PROPERTY);
			if (roleName != null) {
				roles.add(roleName);
			}
		}
		return roles;
	}

	@Override
	public int bulkUpdate(List<Role> roles) {
		if (roles == null || roles.size() == 0) {
			return 0;
		}
		DBCollection collection = this.getDatastore().getCollection(Role.class);
		BulkWriteOperation bulkOperation = collection.initializeUnorderedBulkOperation();
		for (Role role : roles) {
			if (role.getName() == null) {
				continue;
			}
			List<Permission> permissions = role.getPermissions();
			ArrayList<ObjectId> permisionIds = new ArrayList<ObjectId>();
			for (Permission permission : permissions) {
				ObjectId id = this.getPermissionId(permission);
				if (id != null) {
					permisionIds.add(id);
				}
			}
			BasicDBObject dbObject = new BasicDBObject();
			dbObject.append(Constants.ROLE.NAME_PROPERTY, role.getName());
			BasicDBObject update = new BasicDBObject();
			update.append(Constants.ROLE.PERMISSIONS_PROPERTY, permisionIds);
			update.append(Constants.ROLE.DESCRIPTION_PROPERTY, role.getDescription());
			BasicDBObject setList = new BasicDBObject();
			setList.append("$set", update);
			bulkOperation.find(dbObject).updateOne(setList);
		}
		return bulkOperation.execute(WriteConcern.JOURNALED).getModifiedCount();
	}

	ObjectId getPermissionId(Permission permission) {
		try {
			Field field = Permission.class.getDeclaredField("id");
			field.setAccessible(true);
			ObjectId oid = (ObjectId) field.get(permission);
			if (oid == null) {
				Permission found = permissionDao.find(permission.getDomain(), permission.getName());
				if (found == null) {
					return null;
				} else {
					return (ObjectId) field.get(found);
				}
			} else {
				return oid;
			}
		} catch (Throwable ex) {
			// should not reach here
			return null;
		}
	}

	@Override
	public List<Role> findRoles(String... roleNames) {
		if (roleNames == null) {
			return new ArrayList<Role>();
		}
		List<String> roleIdentifiers = Arrays.asList(roleNames);
		Query<Role> query = this.createQuery();
		query.criteria(Constants.ROLE.NAME_PROPERTY).in(roleIdentifiers);
		return query.asList();
	}
}

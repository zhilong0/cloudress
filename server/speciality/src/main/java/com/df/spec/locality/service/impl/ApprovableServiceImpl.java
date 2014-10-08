package com.df.spec.locality.service.impl;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.UpdateOperations;

import com.df.spec.locality.model.Approvable;
import com.df.spec.locality.model.Constants;
import com.df.spec.locality.model.Approvable.Status;
import com.df.spec.locality.service.ApprovableService;
import com.mongodb.MongoClient;

public class ApprovableServiceImpl implements ApprovableService {

	private DatastoreImpl ds;

	public ApprovableServiceImpl(Datastore ds) {
		if (!(ds instanceof DatastoreImpl)) {
			throw new IllegalArgumentException("ds must be instance of " + DatastoreImpl.class.getCanonicalName());
		}
		this.ds = (DatastoreImpl) ds;
	}

	public ApprovableServiceImpl(MongoClient mongoClient, Morphia morphia, String dbName) {
		ds = new DatastoreImpl(morphia, mongoClient, dbName);
	}

	public Datastore getDatastore() {
		return ds;
	}

	public boolean approve(Approvable approvable, String approver) {
		approvable.approve(approver);
		@SuppressWarnings("unchecked")
		UpdateOperations<Approvable> updateOperations = (UpdateOperations<Approvable>) this.getDatastore().createUpdateOperations(approvable.getClass());
		updateOperations.set(Constants.APPROVABLE.APPROVED_BY, approvable.getApprovedBy());
		updateOperations.set(Constants.SPECIALITY.APPROVED_TIME, approvable.getApprovedTime());
		updateOperations.set(Constants.SPECIALITY.STATUS, Status.APPROVED);
		updateOperations.unset(Constants.SPECIALITY.REJECT_REASON);
		return this.getDatastore().update(approvable, updateOperations).getUpdatedExisting();
	}

	public boolean reject(Approvable approvable, String approver, String rejectReason) {
		approvable.reject(approver, rejectReason);
		@SuppressWarnings("unchecked")
		UpdateOperations<Approvable> updateOperations = (UpdateOperations<Approvable>) this.getDatastore().createUpdateOperations(approvable.getClass());
		updateOperations.set(Constants.APPROVABLE.APPROVED_BY, approvable.getApprovedBy());
		updateOperations.set(Constants.SPECIALITY.APPROVED_TIME, approvable.getApprovedTime());
		updateOperations.set(Constants.SPECIALITY.STATUS, Status.APPROVED);
		updateOperations.unset(Constants.SPECIALITY.REJECT_REASON);
		return this.getDatastore().update(approvable, updateOperations).getUpdatedExisting();
	}

}

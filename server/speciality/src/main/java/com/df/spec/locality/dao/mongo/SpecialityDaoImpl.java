package com.df.spec.locality.dao.mongo;

import java.util.List;
import java.util.Properties;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.util.Assert;

import com.df.blobstore.image.http.ImageDetails;
import com.df.spec.locality.dao.SpecialityDao;
import com.df.spec.locality.exception.SpecialityAlreadyExistException;
import com.df.spec.locality.model.Constants;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Speciality;
import com.df.spec.locality.model.Approval.Status;
import com.google.common.collect.Lists;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;

public class SpecialityDaoImpl extends BasicDAO<Speciality, ObjectId> implements SpecialityDao {

	public SpecialityDaoImpl(Datastore ds) {
		super(ds);
	}

	public SpecialityDaoImpl(MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
	}

	@Override
	public void add(Speciality spec, Region region) {
		Assert.notNull(spec.getName());
		Assert.notNull(region);
		Assert.notNull(region.getCode());
		Query<Speciality> query = this.createQuery();
		query.filter(Constants.SPECIALITY.REGION_CODE + " =", region.getCode());
		query.filter(Constants.SPECIALITY.NAME + " =", spec.getName());
		Speciality found = this.findOne(query);
		if (found != null) {
			throw new SpecialityAlreadyExistException(null, spec.getName(), region);
		}
		spec.setRegionCode(region.getCode());
		try {
			this.save(spec);
		} catch (DuplicateKeyException ex) {
			throw new SpecialityAlreadyExistException(ex, spec.getName(), region);
		}
	}

	@Override
	public boolean update(Speciality speciality) {
		Assert.notNull(speciality.getCode());
		Query<Speciality> query = this.createQuery();
		query.filter(Constants.SPECIALITY.CODE, new ObjectId(speciality.getCode()));
		UpdateOperations<Speciality> updateOperations = this.createUpdateOperations();
		if (speciality.getDescription() == null) {
			updateOperations.unset(Constants.SPECIALITY.DESCRIPTION);
		} else {
			updateOperations.set(Constants.SPECIALITY.DESCRIPTION, speciality.getDescription());
		}
		updateOperations.set(Constants.SPECIALITY.RANK, speciality.getRank());
		updateOperations.set(Constants.SPECIALITY.START_MONTH, speciality.getStartMonth());
		updateOperations.set(Constants.SPECIALITY.END_MONTH, speciality.getEndMonth());
		updateOperations.set(Constants.SPECIALITY.IMAGE_SET, speciality.getImageSet());
		if (speciality.getApproval() != null) {
			updateOperations.set(Constants.SPECIALITY.APPROVAL, speciality.getApproval());
		} else {
			updateOperations.unset(Constants.SPECIALITY.APPROVAL);
		}

		return this.update(query, updateOperations).getUpdatedCount() > 0;
	}

	@Override
	public void delete(String specialityCode) {
		this.deleteById(new ObjectId(specialityCode));
	}

	@Override
	public void addImage(String specialityCode, ImageDetails imageDetails) {
		Assert.notNull(imageDetails);
		Speciality found = this.getSpecialityByCode(specialityCode);
		if (found != null) {
			found.getImageSet().addImage(imageDetails);
			Query<Speciality> query = this.createQuery();
			query.filter(Constants.SPECIALITY.CODE, new ObjectId(specialityCode));
			UpdateOperations<Speciality> updateOperations = this.createUpdateOperations();
			updateOperations.set(Constants.SPECIALITY.IMAGE_SET, found.getImageSet());
			this.update(query, updateOperations);
		}
	}

	@Override
	public void removeImage(String specialityCode, String imageId) {
		Speciality found = this.getSpecialityByCode(specialityCode);
		if (found != null) {
			found.getImageSet().removeImage(imageId);
			Query<Speciality> query = this.createQuery();
			query.filter(Constants.SPECIALITY.CODE, new ObjectId(specialityCode));
			UpdateOperations<Speciality> updateOperations = this.createUpdateOperations();
			updateOperations.set(Constants.SPECIALITY.IMAGE_SET, found.getImageSet());
			this.update(query, updateOperations);
			this.update(found);
		}
	}

	@Override
	public Speciality getSpecialityByCode(String specialityCode) {
		if (ObjectId.isValid(specialityCode)) {
			return this.get(new ObjectId(specialityCode));
		} else {
			return null;
		}
	}

	@Override
	public List<Speciality> getSpecialityListByRegionCode(String regionCode) {
		Query<Speciality> query = this.createQuery();
		query.filter(Constants.SPECIALITY.REGION_CODE + " =", regionCode);
		query.filter(Constants.SPECIALITY.STATUS + " =", Status.APPROVED);
		query.order("-" + Constants.SPECIALITY.RANK);
		return Lists.newArrayList(this.find(query));
	}

	@Override
	public Speciality findSpeciality(String regionCode, String specialityName) {
		Query<Speciality> query = this.createQuery();
		query.filter(Constants.SPECIALITY.REGION_CODE + " =", regionCode);
		query.filter(Constants.SPECIALITY.NAME + " =", specialityName);
		return this.findOne(query);
	}

	@Override
	public boolean update(String specialityCode, Properties properties) {
		if (properties.size() == 0) {
			return false;
		}
		Query<Speciality> query = this.createQuery();
		query.filter(Constants.SPECIALITY.CODE, new ObjectId(specialityCode));
		UpdateOperations<Speciality> updateOperations = this.createUpdateOperations();
		for (Object key : properties.keySet()) {
			Object value = properties.get(key);
			if (value == null) {
				updateOperations.unset(key.toString());
			} else {
				updateOperations.set(key.toString(), value);
			}
		}
		return this.update(query, updateOperations).getUpdatedCount() > 0;
	}

	@Override
	public List<Speciality> getSpecialityListByCreatedBy(String createdBy) {
		Query<Speciality> query = this.createQuery();
		query.filter(Constants.SPECIALITY.CREATED_BY + " =", createdBy);
		query.order("-" + Constants.SPECIALITY.CREATED_TIME);
		return Lists.newArrayList(this.find(query));
	}
}

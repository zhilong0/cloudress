package com.df.spec.locality.dao.mongo;

import java.util.List;

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
import com.google.common.collect.ImmutableList;
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
	public void update(Speciality speciality) {
		Assert.notNull(speciality.getCode());
		Query<Speciality> query = this.createQuery();
		query.filter(Constants.SPECIALITY.CODE, new ObjectId(speciality.getCode()));
		UpdateOperations<Speciality> updateOperations = this.createUpdateOperations();
		updateOperations.set(Constants.SPECIALITY.DESCRIPTION, speciality.getDescription());
		this.update(query, updateOperations);
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
		return this.get(new ObjectId(specialityCode));
	}

	@Override
	public List<Speciality> getSpecialityListByRegionCode(String regionCode) {
		Query<Speciality> query = this.createQuery();
		query.filter(Constants.SPECIALITY.REGION_CODE + " =", regionCode);
		return ImmutableList.copyOf(this.find(query));
	}

	@Override
	public Speciality findSpeciality(String regionCode, String specialityName) {
		Query<Speciality> query = this.createQuery();
		query.filter(Constants.SPECIALITY.REGION_CODE + " =", regionCode);
		query.filter(Constants.SPECIALITY.NAME + " =", specialityName);
		return this.findOne(query);
	}
}
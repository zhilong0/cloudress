package com.df.spec.locality.dao.mongo;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.springframework.util.Assert;

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
		Speciality found = this.getSpecialityByCode(speciality.getCode());
		if (found != null) {
			found.setDescriptor(speciality.getDescriptor());
			this.save(found);
		}
	}

	@Override
	public void delete(String specialityCode) {
		this.deleteById(new ObjectId(specialityCode));
	}

	@Override
	public void addImage(String specialityCode, String imageId) {
		Speciality found = this.getSpecialityByCode(specialityCode);
		if (found != null) {
			found.addImage(imageId);
			this.save(found);
		}
	}

	@Override
	public void removeImage(String specialityCode, String imageId) {
		Speciality found = this.getSpecialityByCode(specialityCode);
		if (found != null) {
			found.removeImage(imageId);
			this.save(found);
		}
	}

	@Override
	public Speciality getSpecialityByCode(String specialityCode) {
		Query<Speciality> query = this.createQuery();
		query.filter(Constants.SPECIALITY.CODE + " =", specialityCode);
		return this.findOne(query);
	}

	@Override
	public List<Speciality> getSpecialityListByRegionCode(String regionCode) {
		Query<Speciality> query = this.createQuery();
		query.filter(Constants.SPECIALITY.REGION_CODE + " =", regionCode);
		return ImmutableList.copyOf(this.find(query));
	}
}

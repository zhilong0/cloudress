package com.tt.spec.locality.dao.mongo;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import com.tt.blobstore.image.http.ImageDetails;
import com.tt.spec.locality.dao.SpecialityDao;
import com.tt.spec.locality.exception.SpecialityAlreadyExistException;
import com.tt.spec.locality.model.Constants;
import com.tt.spec.locality.model.Region;
import com.tt.spec.locality.model.Speciality;
import com.tt.spec.locality.model.Approvable.Status;

public class SpecialityDaoImpl extends BaseDao<Speciality, ObjectId> implements SpecialityDao {

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
		return this.update(query, updateOperations).getUpdatedCount() > 0;
	}

	@Override
	public void delete(String specialityCode) {
		this.deleteById(new ObjectId(specialityCode));
	}

	@Override
	public boolean addImages(String specialityCode, ImageDetails[] images) {
		Assert.notNull(images);
		Speciality found = this.findById(Speciality.class, specialityCode);
		if (found != null) {
			for (ImageDetails image : images) {
				if(!found.getImageSet().hasImageWithId(image.getImageId())){
					found.getImageSet().addImage(image);
				}
			}
			Query<Speciality> query = this.createQuery();
			query.filter(Constants.SPECIALITY.CODE, new ObjectId(specialityCode));
			UpdateOperations<Speciality> updateOperations = this.createUpdateOperations();
			updateOperations.set(Constants.SPECIALITY.IMAGE_SET, found.getImageSet());
			return this.update(query, updateOperations).getUpdatedCount() >= 1;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeImages(String specialityCode, String[] imageIds) {
		Assert.notNull(imageIds);
		Speciality found = this.findById(Speciality.class, specialityCode);
		if (found != null) {
			for (String imageId : imageIds) {
				found.getImageSet().removeImage(imageId);
			}
			Query<Speciality> query = this.createQuery();
			query.filter(Constants.SPECIALITY.CODE, new ObjectId(specialityCode));
			UpdateOperations<Speciality> updateOperations = this.createUpdateOperations();
			updateOperations.set(Constants.SPECIALITY.IMAGE_SET, found.getImageSet());
			return this.update(query, updateOperations).getUpdatedCount() >= 1;
		} else {
			return false;
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
	public List<Speciality> getSpecialityListByCreatedBy(String createdBy) {
		Query<Speciality> query = this.createQuery();
		query.filter(Constants.SPECIALITY.CREATED_BY + " =", createdBy);
		query.order("-" + Constants.SPECIALITY.CREATED_TIME);
		return Lists.newArrayList(this.find(query));
	}

	@Override
	public List<Speciality> getWaitToApproveSpecialityList(int offset, int limit) {
		return this.getWaitList(Speciality.class, offset, limit);
	}
}

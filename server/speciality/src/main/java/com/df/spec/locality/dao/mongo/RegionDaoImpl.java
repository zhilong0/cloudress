package com.df.spec.locality.dao.mongo;

import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.CriteriaContainerImpl;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.springframework.util.Assert;

import com.df.spec.locality.dao.RegionDao;
import com.df.spec.locality.exception.DuplicateRegionException;
import com.df.spec.locality.model.Constants;
import com.df.spec.locality.model.Region;
import com.google.common.collect.ImmutableList;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class RegionDaoImpl extends BasicDAO<Region, ObjectId> implements RegionDao {

	public RegionDaoImpl(Datastore ds) {
		super(ds);
	}

	public RegionDaoImpl(MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
	}

	@Override
	public Region addRegion(Region newRegion) {
		try {
			Assert.notNull(newRegion.getCode());
			this.save(newRegion);
			return newRegion;
		} catch (DuplicateKeyException ex) {
			throw new DuplicateRegionException(ex, newRegion);
		}
	}

	@Override
	public Region findRegion(Region region) {
		Query<Region> subQuery1 = this.createQuery();
		CriteriaContainerImpl c1 = subQuery1.criteria(Constants.REGION.PROVINCE_PROPERTY).equal(region.getProvince());
		CriteriaContainerImpl c2 = subQuery1.criteria(Constants.REGION.CITY_PROPERTY).equal(region.getCity());
		CriteriaContainerImpl c3 = subQuery1.criteria(Constants.REGION.DISTRICT_PROPERTY).equal(region.getDistrict());
		Query<Region> subQuery2 = this.createQuery();
		CriteriaContainerImpl c21 = subQuery2.criteria(Constants.REGION.PROVINCE_PROPERTY).equal(region.getProvince());
		CriteriaContainerImpl c22 = subQuery2.criteria(Constants.REGION.CITY_PROPERTY).equal(region.getCity());
		CriteriaContainerImpl c23 = subQuery2.criteria(Constants.REGION.DISTRICT_PROPERTY).doesNotExist();
		Query<Region> query = this.createQuery();
		query.or(subQuery1.and(c1, c2, c3), subQuery2.and(c21, c22, c23));
		return this.findOne(query);
	}

	@Override
	public List<Region> getRegionList() {
		QueryResults<Region> result = this.find();
		Iterator<Region> iter = result.iterator();
		return ImmutableList.copyOf(iter);
	}

	@Override
	public boolean deleteRegion(Region region) {
		Query<Region> query = this.createQuery();
		query.filter(Constants.REGION.PROVINCE_PROPERTY + " =", region.getProvince());
		query.filter(Constants.REGION.CITY_PROPERTY + " =", region.getCity());
		query.filter(Constants.REGION.DISTRICT_PROPERTY + " =", region.getDistrict());
		WriteResult result = this.deleteByQuery(query);
		return result.getN() >= 1;
	}

	@Override
	public Region getRegionByCode(String regionCode) {
		if (regionCode == null) {
			return null;
		}
		Query<Region> query = this.createQuery();
		query.filter(Constants.REGION.CODE_PROPERTY + " =", regionCode);
		return this.findOne(query);
	}

}

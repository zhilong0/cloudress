package com.df.spec.locality.dao.mongo;

import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.util.Assert;

import com.df.spec.locality.dao.ShopDao;
import com.df.spec.locality.exception.DuplicateShopException;
import com.df.spec.locality.model.Approvable.Status;
import com.df.spec.locality.model.Constants;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Shop;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

public class ShopDaoImpl extends BaseDao<Shop, ObjectId> implements ShopDao {

	public ShopDaoImpl(Datastore ds) {
		super(ds);
	}

	public ShopDaoImpl(MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
	}

	@Override
	public void addShop(Shop newShop, Region region) {
		Assert.notNull(region.getCode());
		Assert.notNull(newShop.getName());
		Assert.notNull(newShop.getAddress());
		try {
			Query<Shop> query = this.createQuery();
			query.filter(Constants.SHOP.ADDRESS, newShop.getAddress());
			query.filter(Constants.SHOP.NAME, newShop.getName());
			Shop found = query.get();
			if (found != null) {
				throw new DuplicateShopException(null, newShop.getName(), newShop.getAddress());
			}
			newShop.setRegionCode(region.getCode());
			newShop.setScore(5);
			this.save(newShop, WriteConcern.JOURNALED);
		} catch (DuplicateKeyException ex) {
			throw new DuplicateShopException(ex, newShop.getName(), newShop.getAddress());
		}
	}

	@Override
	public void deleteShop(String shopCode) {
		this.deleteById(new ObjectId(shopCode));
	}

	@Override
	public List<Shop> getShopByRegion(String regionCode) {
		Query<Shop> query = this.createQuery();
		query.filter(Constants.SHOP.REGION_CODE + " =", regionCode);
		query.filter(Constants.SHOP.STATUS + " =", Status.APPROVED);
		QueryResults<Shop> result = this.find(query);
		return ImmutableList.copyOf(result.iterator());
	}

	@Override
	public Shop getShopByCode(String shopCode) {
		return this.get(new ObjectId(shopCode));
	}

	@Override
	public List<Shop> getShopListBySpeciality(String specialityCode) {
		Query<Shop> query = this.createQuery();
		query = query.field(Constants.SHOP.GOODS_SPECIALITY).equal(specialityCode);
		QueryResults<Shop> result = this.find(query);
		Iterator<Shop> iter = result.iterator();
		return ImmutableList.copyOf(iter);
	}

	@Override
	public Shop findShop(String shopName, String address) {
		Query<Shop> query = this.createQuery();
		query.filter(Constants.SHOP.NAME + " =", shopName);
		query.filter(Constants.SHOP.ADDRESS + " =", address);
		return this.findOne(query);
	}

	@Override
	public void update(Shop shop) {
		Assert.notNull(shop.getCode());
		Query<Shop> query = this.createQuery();
		query.filter(Constants.SPECIALITY.CODE, new ObjectId(shop.getCode()));
		UpdateOperations<Shop> updateOperations = this.createUpdateOperations();
		updateOperations.set(Constants.SHOP.GOODS_LIST, shop.getGoodsList());
		updateOperations.set(Constants.SHOP.COORDINATE, shop.getLocation().getCoordinate());
		if (shop.getDescription() == null) {
			updateOperations.unset(Constants.SHOP.DESCRIPTION);
		} else {
			updateOperations.set(Constants.SHOP.DESCRIPTION, shop.getDescription());
		}
		if (shop.getTelephone() == null) {
			updateOperations.unset(Constants.SHOP.TEL);
		} else {
			updateOperations.set(Constants.SHOP.TEL, shop.getTelephone());
		}
		if (shop.getContact() == null) {
			updateOperations.unset(Constants.SHOP.CONTACT);
		} else {
			updateOperations.set(Constants.SHOP.CONTACT, shop.getContact());
		}
		if (shop.getBusinessHour() == null) {
			updateOperations.unset(Constants.SHOP.BUSINESS_HOUR);
		} else {
			updateOperations.set(Constants.SHOP.BUSINESS_HOUR, shop.getBusinessHour());
		}
		updateOperations.set(Constants.SHOP.IMAGESET, shop.getImageSet());
		this.update(query, updateOperations);
	}

	@Override
	public List<Shop> getWaitList(int offset, int limit) {
		return this.getWaitList(Shop.class, offset, limit);
	}

	@Override
	public List<Shop> getShopListByCreatedBy(String createdBy) {
		Query<Shop> query = this.createQuery();
		query.filter(Constants.SHOP.CREATED_BY + " =", createdBy);
		query.order("-" + Constants.SPECIALITY.CREATED_TIME);
		return Lists.newArrayList(this.find(query));
	}

}

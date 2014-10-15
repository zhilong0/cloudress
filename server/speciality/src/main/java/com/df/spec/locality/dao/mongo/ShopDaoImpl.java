package com.df.spec.locality.dao.mongo;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;

import com.df.spec.locality.dao.ShopDao;
import com.df.spec.locality.model.Approvable.Status;
import com.df.spec.locality.model.Constants;
import com.df.spec.locality.model.Goods;
import com.df.spec.locality.model.Shop;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
	public List<Shop> getShopListInRegion(String regionCode, int offset, int limit) {
		Query<Shop> query = this.createQuery();
		query.filter(Constants.SHOP.REGION_CODE + " =", regionCode);
		query.filter(Constants.SHOP.STATUS + " =", Status.APPROVED);
		query.filter(Constants.SHOP.IS_DISABLED + " =", false);
		query.offset(offset);
		query.limit(limit);
		return query.asList();
	}

	@Override
	public List<Shop> getShopListBySpeciality(String specialityCode) {
		Query<Shop> query = this.createQuery();
		query.filter(Constants.SHOP.IS_DISABLED, false);
		query = query.field(Constants.SHOP.SELLING_SPECIALITIES).contains(specialityCode);
		QueryResults<Shop> result = this.find(query);
		Iterator<Shop> iter = result.iterator();
		return ImmutableList.copyOf(iter);
	}

	@Override
	public Shop find(String shopName, String address) {
		Query<Shop> query = this.createQuery();
		query.filter(Constants.SHOP.NAME + " =", shopName);
		query.filter(Constants.SHOP.ADDRESS + " =", address);
		return this.findOne(query);
	}

	@Override
	public boolean update(Shop shop) {
		if (shop.getCode() != null || ObjectId.isValid(shop.getCode())) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.put(Constants.SHOP.NAME, shop.getName());
			p.put(Constants.SHOP.DESCRIPTION, shop.getDescription());
			p.put(Constants.SHOP.BUSINESS_HOUR, shop.getBusinessHour());
			p.put(Constants.SHOP.CONTACT, shop.getContact());
			p.put(Constants.SHOP.TEL, shop.getTelephone());
			p.put(Constants.SHOP.IMAGESET, shop.getImageSet());
			p.put(Constants.SHOP.CHANGED_TIME, new Date());
			p.put(Constants.SHOP.SELLING_SPECIALITIES, shop.getSellingSpecialities());
			return this.update(Shop.class, new ObjectId(shop.getCode()), p);
		} else {
			return false;
		}
	}

	@Override
	public List<Shop> getWaitToApproveShopList(int offset, int limit) {
		return this.getWaitList(Shop.class, offset, limit);
	}

	@Override
	public List<Shop> getShopListByCreatedBy(String createdBy) {
		Query<Shop> query = this.createQuery();
		query.filter(Constants.SHOP.CREATED_BY + " =", createdBy);
		query.order("-" + Constants.SPECIALITY.CREATED_TIME);
		return Lists.newArrayList(this.find(query));
	}

	@Override
	public boolean disable(String shopCode) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(Constants.SHOP.IS_DISABLED, true);
		return this.update(Shop.class, new ObjectId(shopCode), properties);
	}

	@Override
	public boolean enable(String shopCode) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(Constants.SHOP.IS_DISABLED, false);
		return this.update(Shop.class, new ObjectId(shopCode), properties);
	}

	@Override
	public Goods addGoods(String shopCode, Goods goods) {
		goods.setShopCode(shopCode);
		this.getDatastore().save(goods, WriteConcern.JOURNALED);
		return goods;
	}

	@Override
	public boolean updateGoods(Goods goods) {
		return false;
	}

	@Override
	public boolean markGoodsAsDelete(String goodsId) {
		if (ObjectId.isValid(goodsId)) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.put(Constants.GOODS.IS_DELETED, true);
			return this.update(Goods.class, new ObjectId(goodsId), p);
		} else {
			return false;
		}
	}

	@Override
	public List<Goods> getGoodsList(String shopCode) {
		Query<Goods> query = this.getDatastore().createQuery(Goods.class);
		query.filter(Constants.GOODS.SHOP_CODE + " =", shopCode);
		return query.asList();
	}

}

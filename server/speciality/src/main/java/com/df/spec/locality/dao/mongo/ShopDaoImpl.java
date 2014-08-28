package com.df.spec.locality.dao.mongo;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.df.spec.locality.dao.ShopDao;
import com.df.spec.locality.exception.ShopNameAlreadyExistException;
import com.df.spec.locality.model.Constants;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Shop;
import com.google.common.collect.ImmutableList;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;

public class ShopDaoImpl extends BasicDAO<Shop, String> implements ShopDao {

	public ShopDaoImpl(Datastore ds) {
		super(ds);
	}

	public ShopDaoImpl(MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
	}

	@Override
	public void addShop(Shop newShop, Region region) {
		Assert.notNull(newShop.getName());
		Assert.notNull(newShop.getAddress());
		Assert.notNull(region.getCode());
		if (StringUtils.isEmpty(newShop.getDisplayName())) {
			newShop.setDisplayName(newShop.getName());
		}
		try {
			this.save(newShop);
		} catch (DuplicateKeyException ex) {
			throw new ShopNameAlreadyExistException(ex, newShop.getName());
		}
	}

	@Override
	public void deleteShop(String shopCode) {
		this.deleteById(shopCode);
	}

	@Override
	public List<Shop> getShopByRegion(String regionCode) {
		Query<Shop> query = this.createQuery();
		query.filter(Constants.SHOP.REGION_CODE + " =", regionCode);
		QueryResults<Shop> result = this.find(query);
		return ImmutableList.copyOf(result.iterator());
	}

	@Override
	public void deleteShopByName(String shopName) {
		Query<Shop> query = this.createQuery();
		query.filter(Constants.SHOP.NAME + " =", shopName);
		this.deleteByQuery(query);
	}

	@Override
	public Shop getShopByName(String shopName) {
		Query<Shop> query = this.createQuery();
		query.filter(Constants.SHOP.NAME + " =", shopName);
		return this.findOne(query);
	}

}

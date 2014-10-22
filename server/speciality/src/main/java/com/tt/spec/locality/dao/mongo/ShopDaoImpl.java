package com.tt.spec.locality.dao.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.tt.blobstore.image.http.ImageDetails;
import com.tt.spec.locality.dao.ShopDao;
import com.tt.spec.locality.model.Constants;
import com.tt.spec.locality.model.Product;
import com.tt.spec.locality.model.Shop;
import com.tt.spec.locality.model.Approvable.Status;

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
	public List<String> getShopCodeListBySpeciality(String specialityCode, int offset, int limit) {
		Query<Product> query = this.getDatastore().createQuery(Product.class);
		query.filter(Constants.PRODUCT.SPECIALITY_CODE + " =", specialityCode);
		query.filter(Constants.PRODUCT.IS_DELETED + " =", false);
		if (offset >= 0) {
			query.offset(offset);
		}
		if (limit > 0) {
			query.limit(limit);
		}
		List<Key<Product>> keyList = query.asKeyList();
		ArrayList<String> keys = new ArrayList<String>();
		for (Key<Product> key : keyList) {
			keys.add(((ObjectId) key.getId()).toHexString());
		}
		return keys;
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
	public Product addProduct(String shopCode, Product product) {
		product.setShopCode(shopCode);
		this.getDatastore().save(product, WriteConcern.JOURNALED);
		return product;
	}

	@Override
	public boolean updateProduct(Product product) {
		return false;
	}

	@Override
	public boolean markProductAsDelete(String productId) {
		if (ObjectId.isValid(productId)) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.put(Constants.PRODUCT.IS_DELETED, true);
			return this.update(Product.class, new ObjectId(productId), p);
		} else {
			return false;
		}
	}

	@Override
	public List<Product> getProductList(String shopCode) {
		Query<Product> query = this.getDatastore().createQuery(Product.class);
		query.filter(Constants.PRODUCT.SHOP_CODE + " =", shopCode);
		query.filter(Constants.PRODUCT.IS_DELETED + " =", false);
		return query.asList();
	}

	@Override
	public boolean addImages(String shopCode, ImageDetails[] images) {
		Assert.notNull(images);
		Shop found = this.findById(Shop.class, shopCode);
		if (found != null) {
			for (ImageDetails image : images) {
				if (!found.getImageSet().hasImageWithId(image.getImageId())) {
					found.getImageSet().addImage(image);
				}
			}
			Query<Shop> query = this.createQuery();
			query.filter(Constants.SHOP.CODE, new ObjectId(shopCode));
			UpdateOperations<Shop> updateOperations = this.createUpdateOperations();
			updateOperations.set(Constants.SHOP.IMAGESET, found.getImageSet());
			return this.update(query, updateOperations).getUpdatedCount() >= 1;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteImages(String shopCode, String[] imageIds) {
		Assert.notNull(imageIds);
		Shop found = this.findById(Shop.class, shopCode);
		if (found != null) {
			for (String imageId : imageIds) {
				found.getImageSet().removeImage(imageId);
			}
			Query<Shop> query = this.createQuery();
			query.filter(Constants.SHOP.CODE, new ObjectId(shopCode));
			UpdateOperations<Shop> updateOperations = this.createUpdateOperations();
			updateOperations.set(Constants.SHOP.IMAGESET, found.getImageSet());
			return this.update(query, updateOperations).getUpdatedCount() >= 1;
		} else {
			return false;
		}
	}

	@Override
	public List<Shop> getShopList(List<String> shopCodeList, boolean filterDisabled) {
		if (shopCodeList == null || shopCodeList.size() == 0) {
			return new ArrayList<Shop>();
		}
		Query<Shop> query = this.createQuery();
		if (filterDisabled) {
			query.filter(Constants.SHOP.IS_DISABLED, false);
		}
		query = query.field(Constants.SHOP.CODE).in(shopCodeList);
		QueryResults<Shop> result = this.find(query);
		return result.asList();
	}

	@Override
	public Product getProduct(String shopCode, String specialityCode) {
		Query<Product> query = this.getDatastore().createQuery(Product.class);
		query.filter(Constants.PRODUCT.SHOP_CODE, shopCode);
		query.filter(Constants.PRODUCT.SPECIALITY_CODE, specialityCode);
		return query.get();
	}

}

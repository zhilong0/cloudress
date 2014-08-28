package com.df.spec.locality.dao;

import java.util.List;

import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Shop;

public interface ShopDao {

	void addShop(Shop newShop, Region region);

	void deleteShop(String shopCode);

	void deleteShopByName(String shopName);

	Shop getShopByName(String shopName);

	List<Shop> getShopByRegion(String regionCode);

}

package com.df.spec.locality.dao;

import java.util.List;

import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Shop;

public interface ShopDao {

	void addShop(Shop newShop, Region region);
	
	void update(Shop shop);

	void deleteShop(String shopCode);

	List<Shop> getShopByRegion(String regionCode);
	
	Shop getShopByCode(String shopCode); 
	
	List<Shop> getShopListBySpeciality(String specialityCode);
	
	Shop findShop(String shopName, String address);

}

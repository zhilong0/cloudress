package com.df.spec.locality.service;

import java.util.List;

import com.df.spec.locality.model.Shop;

public interface ShopService {

	Shop addShop(Shop newShop);

	Shop getShopByCode(String shopCode, boolean throwException);

	List<Shop> getShopListSellSpeciality(String specialityCode);

	List<Shop> getMyShops(String userCode);

	Shop findShop(String shopName, String address);

	void updateShop(Shop shop);

	String uploadShopImage(String shopCode, byte[] imageData, String imageName);

	void deleteShopImage(String shopCode, String imageId);

	List<Shop> getWaitList(int offset, int limit);

}

package com.df.spec.locality.service;

import java.util.List;

import com.df.spec.locality.model.Comment;
import com.df.spec.locality.model.Shop;

public interface ShopService {

	void addShop(Shop newShop, String regionCode);

	Shop getShopByCode(String shopCode, boolean throwException);

	List<Shop> getShopListSellSpeciality(String specialityCode);

	Shop findShop(String shopName, String address);

	void updateShop(Shop shop);

	String uploadShopImage(String shopCode, byte[] imageData, String imageName);

	void deleteShopImage(String shopCode, String imageId);

	List<Comment> getCommentList();

}

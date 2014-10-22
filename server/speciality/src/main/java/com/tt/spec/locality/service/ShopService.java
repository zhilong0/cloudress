package com.tt.spec.locality.service;

import java.util.List;

import com.tt.spec.locality.model.Product;
import com.tt.spec.locality.model.Shop;

public interface ShopService {

	Shop addShop(Shop newShop);

	Shop getShopByCode(String shopCode, boolean throwException);

	List<Shop> getShopListSellSpeciality(String specialityCode);

	List<Shop> getShopListInRegion(String regionCode, int offset, int limit);

	List<Shop> getMyShops(String userCode);

	Shop find(String shopName, String address);

	void update(Shop shop);
	
	boolean updateImageSet(String shopCode, String[] imageIds, boolean isAdd);

	void disable(String shopCode);

	void enable(String shopCode);

	List<Shop> getWaitToApproveShopList(int offset, int limit);

	Product addProduct(String shopCode, Product product);

	boolean deleteProduct(String shopCode, String productId);

	List<Product> getProductList(String shopCode);

}

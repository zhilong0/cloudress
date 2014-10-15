package com.df.spec.locality.service;

import java.util.List;

import com.df.spec.locality.model.Goods;
import com.df.spec.locality.model.Shop;

public interface ShopService {

	Shop addShop(Shop newShop);

	Shop getShopByCode(String shopCode, boolean throwException);

	List<Shop> getShopListSellSpeciality(String specialityCode);

	List<Shop> getShopListInRegion(String regionCode, int offset, int limit);

	List<Shop> getMyShops(String userCode);

	Shop find(String shopName, String address);

	void update(Shop shop);

	void disable(String shopCode);

	void enable(String shopCode);

	List<Shop> getWaitToApproveShopList(int offset, int limit);

	Goods addGoods(String shopCode, Goods goods);

	boolean deleteGoods(String shopCode, String goodsId);

	List<Goods> getGoodsList(String shopCode);

}

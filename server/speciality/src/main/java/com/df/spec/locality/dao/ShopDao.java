package com.df.spec.locality.dao;

import java.util.List;

import com.df.spec.locality.model.Goods;
import com.df.spec.locality.model.Shop;

public interface ShopDao extends DaoTemplate {

	boolean update(Shop shop);

	boolean disable(String shopCode);

	boolean enable(String shopCode);

	List<Shop> getShopListInRegion(String regionCode, int offset, int limit);

	List<Shop> getShopListByCreatedBy(String createdBy);

	List<Shop> getShopListBySpeciality(String specialityCode);

	Shop find(String shopName, String address);

	List<Shop> getWaitToApproveShopList(int offset, int limit);

	Goods addGoods(String shopCode, Goods goods);

	List<Goods> getGoodsList(String shopCode);

	boolean updateGoods(Goods goods);

	boolean markGoodsAsDelete(String goodsId);

}

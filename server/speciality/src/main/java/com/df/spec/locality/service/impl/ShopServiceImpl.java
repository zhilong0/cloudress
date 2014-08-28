package com.df.spec.locality.service.impl;

import com.df.spec.locality.dao.ShopDao;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Shop;
import com.df.spec.locality.service.RegionService;
import com.df.spec.locality.service.ShopService;

public class ShopServiceImpl implements ShopService {

	private ShopDao shopDao;

	private RegionService regionService;

	public ShopServiceImpl(ShopDao shopDao, RegionService regionService) {
		this.shopDao = shopDao;
		this.regionService = regionService;
	}

	public void setShopDao(ShopDao shopDao) {
		this.shopDao = shopDao;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	@Override
	public void addShop(Shop newShop, String regionCode) {
		Region region = regionService.getRegionByCode(regionCode, true);
		shopDao.addShop(newShop, region);
	}

}

package com.df.spec.locality.dao;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.df.spec.locality.exception.ShopNameAlreadyExistException;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Shop;

public class MongoShopDaoTest extends SpecialityBaseTest {

	@Autowired
	private ShopDao shopDao;

	@Autowired
	private RegionDao regionDao;

	protected Region createRegion() {
		Region region = new Region("上海市", "上海市", "黄浦区");
		regionDao.addRegion(region);
		return region;
	}

	protected void removeRegion(Region region) {
		regionDao.deleteRegion(region);
	}

	@Test
	public void testAddShop() {
		Region region = this.createRegion();
		Shop shop = new Shop();
		try {
			shop.setName("城隍庙第一百货");
			shop.setAddress("黄浦区安仁街218号");
			shopDao.addShop(shop, region);
			shopDao.deleteShop(shop.getCode());
			Shop shop2 = new Shop();
			shop2.setName(shop.getName());
			shop2.setAddress(shop.getAddress());
			try {
				shopDao.addShop(shop2, region);
			} catch (ShopNameAlreadyExistException ex) {
				return;
			}
			TestCase.fail();
		} finally {
			this.removeRegion(region);
			shopDao.deleteShopByName("城隍庙第一百货");
		}
	}

	@Test
	public void testGetShopByName() {
		Region region = this.createRegion();
		Shop shop = new Shop();
		try {
			shop.setName("城隍庙第一百货");
			shop.setAddress("黄浦区安仁街218号");
			shopDao.addShop(shop, region);
			TestCase.assertNotNull(shopDao.getShopByName(shop.getName()));
		} finally {
			this.removeRegion(region);
			shopDao.deleteShopByName(shop.getName());
		}
	}
}

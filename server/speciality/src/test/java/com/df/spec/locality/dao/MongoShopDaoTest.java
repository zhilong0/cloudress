package com.df.spec.locality.dao;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.df.spec.locality.exception.DuplicateShopException;
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

	protected void removeRegion() {
		regionDao.deleteRegion(new Region("上海市", "上海市", "黄浦区"));
	}

	@Test
	public void testAddShop() {
		Shop shop = new Shop("城隍庙第一百货测试", "黄浦区安仁街218号");
		try {
			Region region = this.createRegion();
			shopDao.addShop(shop, region);
			Shop shop2 = new Shop(shop.getName(), shop.getAddress());
			try {
				shopDao.addShop(shop2, region);
			} catch (DuplicateShopException ex) {
				return;
			}
			TestCase.fail();
		} finally {
			this.removeRegion();
			if (shop.getCode() != null) {
				shopDao.deleteShop(shop.getCode());
			}
		}
	}

	@Test
	public void testGetShopByCode() {
		Shop shop = new Shop("城隍庙第一百货测试", "黄浦区安仁街218号");
		try {
			Region region = this.createRegion();
			shopDao.addShop(shop, region);
			TestCase.assertNotNull(shopDao.getShopByCode(shop.getCode()));
		} finally {
			this.removeRegion();
			if (shop.getCode() != null) {
				shopDao.deleteShop(shop.getCode());
			}
		}
	}
}

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
		Region region = new Region("test","测试省", "测试市", "测试县");
		regionDao.addRegion(region);
		return region;
	}

	protected void removeRegion() {
		regionDao.deleteRegion(new Region("test","测试省", "测试市", "测试县"));
	}

	@Test
	public void testAddShop() {
		Shop shop = new Shop("商铺1", "address1");
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
		Shop shop = new Shop("商铺2", "address2");
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

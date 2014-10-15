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
		Region region = new Region("test", "测试省", "测试市", "测试县");
		regionDao.addRegion(region);
		return region;
	}

	protected void removeRegion() {
		regionDao.deleteRegion(new Region("test", "测试省", "测试市", "测试县"));
	}

	@Test
	public void testAddShop() {
		Shop shop = new Shop("商铺1", "address1");
		try {
			Region region = this.createRegion();
			shop.setRegionCode(region.getCode());
			shopDao.add(shop);
			Shop shop2 = new Shop(shop.getName(), shop.getAddress());
			shop2.setRegionCode(region.getCode());
			try {
				shopDao.add(shop2);
			} catch (DuplicateShopException ex) {
				return;
			}
			TestCase.fail();
		} finally {
			this.removeRegion();
			if (shop.getCode() != null) {
				shopDao.deleteById(Shop.class, shop.getCode());
			}
		}
	}

	@Test
	public void testGetShopByCode() {
		Shop shop = new Shop("商铺2", "address2");
		try {
			Region region = this.createRegion();
			shop.setRegionCode(region.getCode());
			shopDao.add(shop);
			TestCase.assertNotNull(shopDao.findById(Shop.class, shop.getCode()));
		} finally {
			this.removeRegion();
			if (shop.getCode() != null) {
				shopDao.deleteById(Shop.class, shop.getCode());
			}
		}
	}
}

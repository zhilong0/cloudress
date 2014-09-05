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
		Region region = new Region("�Ϻ���", "�Ϻ���", "������");
		regionDao.addRegion(region);
		return region;
	}

	protected void removeRegion(Region region) {
		regionDao.deleteRegion(region);
	}

	@Test
	public void testAddShop() {
		Region region = this.createRegion();
		Shop shop = new Shop("�������һ�ٻ�", "���������ʽ�218��");
		try {

			shopDao.addShop(shop, region);
			shopDao.deleteShop(shop.getCode());
			Shop shop2 = new Shop(shop.getName(), shop.getAddress());
			try {
				shopDao.addShop(shop2, region);
			} catch (DuplicateShopException ex) {
				return;
			}
			TestCase.fail();
		} finally {
			this.removeRegion(region);
			if (shop.getCode() != null) {
				shopDao.deleteShop(shop.getCode());
			}
		}
	}

	@Test
	public void testGetShopByCode() {
		Region region = this.createRegion();
		Shop shop = new Shop("�������һ�ٻ�", "���������ʽ�218��");
		try {
			shopDao.addShop(shop, region);
			TestCase.assertNotNull(shopDao.getShopByCode(shop.getCode()));
		} finally {
			this.removeRegion(region);
			shopDao.deleteShop(shop.getCode());
		}
	}
}

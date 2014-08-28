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
		Shop shop = new Shop();
		try {
			shop.setName("�������һ�ٻ�");
			shop.setAddress("���������ʽ�218��");
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
			shopDao.deleteShopByName("�������һ�ٻ�");
		}
	}

	@Test
	public void testGetShopByName() {
		Region region = this.createRegion();
		Shop shop = new Shop();
		try {
			shop.setName("�������һ�ٻ�");
			shop.setAddress("���������ʽ�218��");
			shopDao.addShop(shop, region);
			TestCase.assertNotNull(shopDao.getShopByName(shop.getName()));
		} finally {
			this.removeRegion(region);
			shopDao.deleteShopByName(shop.getName());
		}
	}
}

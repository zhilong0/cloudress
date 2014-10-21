package com.df.spec.locality.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.df.spec.locality.dao.ShopDao;
import com.df.spec.locality.dao.SpecialityBaseTest;
import com.df.spec.locality.model.Product;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Shop;
import com.df.spec.locality.service.impl.ShopServiceImpl;

public class ShopServiceTest extends SpecialityBaseTest {

	@Autowired
	private ShopService shopService;

	@Autowired
	private SpecialityService specialityService;

	@Autowired
	private ShopDao shopDao;

	private RegionService regionService;

	private OperationPermissionEvaluator permissionEvaluator;

	@Before
	public void beforeTest() {
		regionService = Mockito.mock(RegionService.class);
		permissionEvaluator = Mockito.mock(OperationPermissionEvaluator.class);
		Mockito.when(permissionEvaluator.canAddShop("sala223@msn.com")).thenReturn(true);
		Mockito.when(regionService.getRegionByCode("testRegion", true)).thenReturn(new Region("test province", "test city"));
		((ShopServiceImpl) shopService).setRegionService(regionService);
		((ShopServiceImpl) shopService).setServiceOperationPermissionEvaluator(permissionEvaluator);
	}

	@Test
	public void testAddShopWithProducts() {
		Shop shop = new Shop("testshop" + UUID.randomUUID().toString(), "晨晖路1001号");
		shop.setCreatedBy("sala223@msn.com");
		shop.setBusinessHour("早上8点到晚上6点");
		shop.setImage("testimageid");
		shop.setRegionCode("testRegion");
		Product p1 = new Product("spec1");
		Product p2 = new Product("spec2");
		Product p3 = new Product("spec3");
		shop.addProduct(p1);
		shop.addProduct(p2);
		shop.addProduct(p3);
		try {
			shopService.addShop(shop);
			List<Product> products = shopService.getProductList(shop.getCode());
			TestCase.assertEquals(3, products.size());
		} finally {
			if (shop.getCode() != null) {
				shopDao.deleteById(Shop.class, shop.getCode());
				List<Product> products = shopDao.getProductList(shop.getCode());
				List<String> productIds = new ArrayList<String>();
				for (Product product : products) {
					productIds.add(product.getId());
				}
				shopDao.deleteByIds(Product.class, productIds.toArray(new String[0]));
			}
		}
	}

	@Test
	public void testUpdateShopProduct() {
		Shop shop = new Shop("testshop" + UUID.randomUUID().toString(), "晨晖路1001号");
		shop.setCreatedBy("sala223@msn.com");
		shop.setBusinessHour("早上8点到晚上6点");
		shop.setImage("testimageid");
		shop.setRegionCode("testRegion");
		Product p1 = new Product("spec1");
		Product p2 = new Product("spec2");
		Product p3 = new Product("spec3");
		shop.addProduct(p1);
		shop.addProduct(p2);
		shop.addProduct(p3);
		try {
			shop = shopService.addShop(shop);
			shop.removeProduct(p1.getSpecialityCode());
			shop.removeProduct(p2.getSpecialityCode());
			shopService.update(shop);
			List<Product> products = shopService.getProductList(shop.getCode());
			TestCase.assertEquals(1, products.size());
		} finally {
			if (shop.getCode() != null) {
				shopDao.deleteById(Shop.class, shop.getCode());
				List<Product> products = shopDao.getProductList(shop.getCode());
				List<String> productIds = new ArrayList<String>();
				for (Product product : products) {
					productIds.add(product.getId());
				}
				shopDao.deleteByIds(Product.class, productIds.toArray(new String[0]));
			}
		}
	}
}

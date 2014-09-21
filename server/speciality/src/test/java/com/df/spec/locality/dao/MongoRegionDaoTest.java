package com.df.spec.locality.dao;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.df.spec.locality.dao.mongo.RegionDaoImpl;
import com.df.spec.locality.model.Region;

public class MongoRegionDaoTest extends SpecialityBaseTest {

	@Autowired
	private RegionDaoImpl regionDao;

	@Test
	public void testAddGetDeleteRegion() {
		Region newRegion = new Region("test", "测试省", "测试市", "测试县");
		try {
			regionDao.addRegion(newRegion);
			Assert.isTrue(regionDao.findRegion(newRegion) != null);
		} finally {
			Assert.isTrue(regionDao.deleteRegion(newRegion));
		}
	}

	@Test
	public void testGetRegionList() {
		Region newRegion = new Region("test", "测试省", "测试市", "测试县");
		try {
			regionDao.addRegion(newRegion);
			List<Region> regions = regionDao.getRegionList();
			TestCase.assertTrue(regions.size() > 0);
		} finally {
			Assert.isTrue(regionDao.deleteRegion(newRegion));
		}
	}
}

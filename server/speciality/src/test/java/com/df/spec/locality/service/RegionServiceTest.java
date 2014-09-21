package com.df.spec.locality.service;

import java.io.InputStream;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.df.spec.locality.dao.RegionDao;
import com.df.spec.locality.dao.SpecialityBaseTest;
import com.df.spec.locality.model.Region;

public class RegionServiceTest extends SpecialityBaseTest {

	@Autowired
	private RegionService regionService;

	@Autowired
	private RegionDao regionDao;

	@Test
	public void testImportRegion() {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("region_test.csv");
			regionService.importFromCSV(in, true);
			Region region = regionService.findRegion("测试省2", "测试市", "测试县");
			TestCase.assertNotNull(region);
		} finally {
			regionDao.deleteRegion(new Region("测试省2", "测试市", "测试县"));
			regionDao.deleteRegion(new Region("测试省1", "测试市"));
			regionDao.deleteRegion(new Region("北京市", "北京市"));
		}

	}
}

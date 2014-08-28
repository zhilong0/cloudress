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
			Region region = regionService.findRegion("����ʡ2", "������", "������");
			TestCase.assertNotNull(region);
		} finally {
			regionDao.deleteRegion(new Region("����ʡ2", "������", "������"));
			regionDao.deleteRegion(new Region("����ʡ1", "������"));
			regionDao.deleteRegion(new Region("������", "������"));
		}

	}
}

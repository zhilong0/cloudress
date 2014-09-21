package com.df.spec.locality.service;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.df.spec.locality.dao.RegionDao;
import com.df.spec.locality.dao.SpecialityBaseTest;
import com.df.spec.locality.dao.SpecialityDao;
import com.df.spec.locality.data.streamline.SpecialitySeasonalComparator;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Speciality;

public class SpecialityServiceTest extends SpecialityBaseTest {

	@Autowired
	private SpecialityService specialityService;

	@Autowired
	private RegionService regionService;

	@Autowired
	private RegionDao regionDao;

	@Autowired
	private SpecialityDao specialityDao;

	@Test
	public void testAddSpeciality() {
		Region region = new Region("测试省", "测试市");
		region.setCode("testre");
		Speciality spec = new Speciality();
		spec.setName("spec1");
		try {
			regionService.addRegion(region);
			specialityService.addSpeciality(spec, region.getCode());
			TestCase.assertNotNull(specialityService.getSpecialityListByRegionCode(region.getCode()));
		} finally {
			if (spec.getCode() != null) {
				specialityDao.delete(spec.getCode());
			}
			regionDao.deleteRegion(region);
		}
	}

	@Test
	public void testSeasonalSort() {
		Speciality spec1 = new Speciality();
		spec1.setName("spec1");
		spec1.setStartMonth(5);
		spec1.setEndMonth(8);
		spec1.setRank(5);
		Speciality spec2 = new Speciality();
		spec2.setName("spec2");
		spec2.setRank(4);
		SpecialitySeasonalComparator cmp = new SpecialitySeasonalComparator();
		TestCase.assertTrue(cmp.compare(spec1, spec2) > 0);
		spec2.setStartMonth(7);
		spec2.setEndMonth(11);
		TestCase.assertTrue(cmp.compare(spec1, spec2) > 0);
		spec2.setEndMonth(2);
		TestCase.assertTrue(cmp.compare(spec1, spec2) > 0);
		spec2.setEndMonth(8);
		TestCase.assertTrue(cmp.compare(spec1, spec2) < 0);

	}
}

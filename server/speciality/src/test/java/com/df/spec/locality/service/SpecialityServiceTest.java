package com.df.spec.locality.service;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.df.spec.locality.dao.RegionDao;
import com.df.spec.locality.dao.SpecialityBaseTest;
import com.df.spec.locality.dao.SpecialityDao;
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
		Region region = new Region("≤‚ ‘ °11", "≤‚ ‘ –");
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
}

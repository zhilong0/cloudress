package com.df.spec.locality.dao;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.df.spec.locality.exception.SpecialityAlreadyExistException;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Speciality;

public class MongoSpecialityDaoTest extends SpecialityBaseTest {

	@Autowired
	private RegionDao regionDao;

	@Autowired
	private SpecialityDao specialityDao;

	@Test
	public void testAddSpeciality() {
		Speciality speciality = new Speciality();
		speciality.setName("臭豆腐");
		speciality.addImage("123455");
		speciality.addImage("434343");
		Region region = new Region("湖南省", "长沙市", "");
		try {
			regionDao.addRegion(region);
			specialityDao.add(speciality, region);
		} finally {
			regionDao.deleteRegion(region);
			if (speciality.getCode() != null) {
				specialityDao.delete(speciality.getCode());
			}
		}
	}

	@Test
	public void testAddSpecialityWithSameNameInSameRegion() {
		Speciality speciality = new Speciality();
		speciality.setName("臭豆腐");
		speciality.addImage("123455");
		speciality.addImage("434343");
		Region region = new Region("湖南省", "长沙市", "");
		try {
			regionDao.addRegion(region);
			specialityDao.add(speciality, region);
			try {
				specialityDao.add(speciality, region);
				TestCase.fail();
			} catch (SpecialityAlreadyExistException ex) {
			}
		} finally {
			regionDao.deleteRegion(region);
			if (speciality.getCode() != null) {
				specialityDao.delete(speciality.getCode());
			}
		}
	}

}

package com.tt.spec.locality.dao;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tt.blobstore.image.ImageAttributes;
import com.tt.blobstore.image.ImageFormat;
import com.tt.spec.locality.dao.RegionDao;
import com.tt.spec.locality.dao.SpecialityDao;
import com.tt.spec.locality.exception.SpecialityAlreadyExistException;
import com.tt.spec.locality.model.Region;
import com.tt.spec.locality.model.Speciality;

public class MongoSpecialityDaoTest extends SpecialityBaseTest {

	@Autowired
	private RegionDao regionDao;

	@Autowired
	private SpecialityDao specialityDao;

	@Test
	public void testAddSpeciality() {
		Speciality speciality = new Speciality();
		speciality.setName("测试特产");
		ImageAttributes attributes = new ImageAttributes("p1", 1024, 768, ImageFormat.JPEG);
		speciality.getImageSet().addImage("123455", attributes);
		ImageAttributes attributes2 = new ImageAttributes("p2", 1024, 768, ImageFormat.JPEG);
		speciality.getImageSet().addImage("434343", attributes2);
		Region region = new Region("测试省", "测试市", "");
		region.setCode("testcs");
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
		speciality.setName("");
		Region region = new Region("测试省", "测试市", "");
		region.setCode("cs");
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

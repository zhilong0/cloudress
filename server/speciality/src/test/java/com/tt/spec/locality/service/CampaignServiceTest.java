package com.tt.spec.locality.service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tt.spec.locality.dao.CampaignDao;
import com.tt.spec.locality.dao.RegionDao;
import com.tt.spec.locality.dao.SpecialityBaseTest;
import com.tt.spec.locality.model.Campaign;
import com.tt.spec.locality.model.Constants;
import com.tt.spec.locality.model.Location;
import com.tt.spec.locality.model.Region;
import com.tt.spec.locality.model.SpecialityGroupPurcharse;
import com.tt.spec.locality.model.UserProfile;
import com.tt.spec.locality.service.CampaignService;

public class CampaignServiceTest extends SpecialityBaseTest {

	@Autowired
	private Validator validator;

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private RegionDao regionDao;

	@Autowired
	private CampaignDao campaignDao;

	@Test
	public void testValidateCampaign() {
		Campaign campaign = new SpecialityGroupPurcharse();
		campaign.setSponsor("Xia Pin");
		Set<ConstraintViolation<Campaign>> violations = validator.validate(campaign);
		Set<String> pathes = new HashSet<String>();
		for (ConstraintViolation<Campaign> violation : violations) {
			pathes.add(violation.getPropertyPath().toString());
		}
		TestCase.assertTrue(pathes.contains(Constants.CAMPAIGN.END_TIME));
		TestCase.assertTrue(pathes.contains(Constants.CAMPAIGN.TITLE));
		TestCase.assertTrue(pathes.contains(Constants.CAMPAIGN.REGION_CODE));
		TestCase.assertTrue(pathes.contains(Constants.CAMPAIGN.CONTENT));
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 5);
		campaign.setEndTime(c.getTime());
		campaign.setSubject("test title");
		campaign.setRegionCode("test region");
		campaign.setDesc("test content");
		violations = validator.validate(campaign);
		TestCase.assertTrue(violations.size() == 0);
		campaign.setRequireAssembly(true);
		violations = validator.validate(campaign);
		pathes = new HashSet<String>();
		for (ConstraintViolation<Campaign> violation : violations) {
			pathes.add(violation.getMessageTemplate());
		}
		TestCase.assertTrue(pathes.contains("{campaign.assembly.assemblyTime.NotNull}"));
		TestCase.assertTrue(pathes.contains("{campaign.assembly.assemblyLocation.NotNull}"));
		TestCase.assertTrue(pathes.contains("{campaign.assembly.contact.NotNull}"));
		TestCase.assertTrue(pathes.contains("{campaign.assembly.cellphone.NotNull}"));
		c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 8);
		campaign.setAssemblyTime(c.getTime());
		campaign.location(new Location("beijing", null));
		campaign.setContact("xia pin");
		campaign.setContactCellphone("13621992125");
		violations = validator.validate(campaign);
		TestCase.assertEquals(1, violations.size());
		c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 1);
		campaign.setAssemblyTime(c.getTime());
		violations = validator.validate(campaign);
		TestCase.assertEquals(violations.size(), 0);
	}

	@Test
	public void testCreateCampaign() {
		Region region = new Region("shanghai", "shanghai");
		UserProfile user = new UserProfile();
		user.setCode("i061134");
		user.setCellPhone("13621992125");
		user.setRealName("xia pin");
		Campaign campaign = new SpecialityGroupPurcharse();
		campaign.setSubject("nan hui peach group purcharse with 80% discount");
		campaign.setRequireAssembly(false);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, 2);
		campaign.setEndTime(now.getTime());
		now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, 3);
		campaign.setAssemblyTime(now.getTime());
		campaign.location(new Location("晨晖路1001号", null));
		campaign.setDesc("assembly at SAP gateway at 10am ");
		try {
			region.setCode("shanghai");
			regionDao.addRegion(region);
			campaign.setRegionCode(region.getCode());
			campaignService.createCampaign(campaign, user);
			List<Campaign> founds = campaignService.getPublishedCampaignList(user.getCode());
			TestCase.assertTrue(founds.size() == 1);
		} finally {
			regionDao.deleteRegion(region);
			if (campaign.getId() != null) {
				campaignDao.delete(campaign.getId());
			}
		}
	}

	@Test
	public void testUpdateCampaign() {
		UserProfile user = new UserProfile();
		user.setCode("i061134");
		user.setCellPhone("13621992125");
		user.setRealName("xia pin");
		Campaign campaign = new SpecialityGroupPurcharse();
		campaign.setSubject("nan hui peach group purcharse with 80% discount");
		campaign.setRequireAssembly(true);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, 3);
		campaign.setStartTime(now.getTime());
		now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, 5);
		campaign.setEndTime(now.getTime());
		now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, 2);
		campaign.setApplyDeadLine(now.getTime());
		now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, 4);
		campaign.setAssemblyTime(now.getTime());
		campaign.location(new Location("晨晖路1001号", null));
		campaign.setDesc("assembly at SAP gateway at 10am ");
		Region region = new Region("shanghai", "shanghai");
		region.setCode("shanghai");
		try {
			regionDao.addRegion(region);
			campaign.setRegionCode(region.getCode());
			campaignService.createCampaign(campaign, user);
			List<Campaign> founds = campaignService.getPublishedCampaignList(user.getCode());
			TestCase.assertTrue(founds.size() == 1);
			campaign.location(new Location("晨晖路1002号", null));
			campaignService.updateCampaign(campaign, user);
			founds = campaignService.getPublishedCampaignList(user.getCode());
			TestCase.assertTrue(founds.size() == 1);
			Campaign found = founds.get(0);
			TestCase.assertNotNull(found.getLastModified());
		} finally {
			regionDao.deleteRegion(region);
			if (campaign.getId() != null) {
				campaignDao.delete(campaign.getId());
			}
		}
	}
}

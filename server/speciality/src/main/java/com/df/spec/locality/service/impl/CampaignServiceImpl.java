package com.df.spec.locality.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.df.spec.locality.dao.CampaignDao;
import com.df.spec.locality.exception.CampaignErrorCode;
import com.df.spec.locality.exception.SpecialityBaseException;
import com.df.spec.locality.exception.validation.ValidationException;
import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.geo.GeoService;
import com.df.spec.locality.model.Campaign;
import com.df.spec.locality.model.CampaignType;
import com.df.spec.locality.model.Location;
import com.df.spec.locality.model.Participant;
import com.df.spec.locality.model.ParticipantJournal;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.UserProfile;
import com.df.spec.locality.service.CampaignService;
import com.df.spec.locality.service.RegionService;
import com.df.spec.locality.validation.group.CampaignConstraintGroup.CreateCampaign;

public class CampaignServiceImpl implements CampaignService {

	private Validator validator;

	private CampaignDao campaignDao;

	private GeoService geoService;

	private RegionService regionService;

	public CampaignServiceImpl(CampaignDao campaignDao, Validator validator, RegionService regionService, GeoService geoService) {
		this.validator = validator;
		this.campaignDao = campaignDao;
		this.geoService = geoService;
		this.regionService = regionService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public void setGeoService(GeoService geoService) {
		this.geoService = geoService;
	}

	public void setCampaignDao(CampaignDao campaignDao) {
		this.campaignDao = campaignDao;
	}

	@Override
	public Campaign createCampaign(Campaign campaign, UserProfile user) {
		Region region = regionService.getRegionByCode(campaign.getRegionCode(), true);
		campaign.setSponsor(user.getId());
		campaign.getParticipants().clear();
		if (campaign.isRequireAssembly()) {
			if (campaign.getContact() == null) {
				campaign.setContact(user.getRealName());
			}
			if (campaign.getContactCellphone() == null) {
				campaign.setContactCellphone(user.getCellPhone());
			}
		} else {
			if (campaign.getStartTime() == null) {
				campaign.setStartTime(new Date());
			}
			if (campaign.getApplyDeadline() == null) {
				campaign.setApplyDeadLine(campaign.getEndTime());
			}
		}
		Set<ConstraintViolation<Campaign>> violations = validator.validate(campaign, Default.class, CreateCampaign.class);
		if (violations.size() != 0) {
			throw new ValidationException(violations.toArray(new ConstraintViolation[0]));
		}
		campaign.setPublishDate(new Date());
		if (campaign.getStartTime() == null) {
			campaign.setStartTime(campaign.getPublishDate());
		}
		if (campaign.getAssemblyLocation() != null && campaign.getAssemblyLocation().getAddress() != null) {
			Coordinate coordinate = geoService.lookupCoordinate(campaign.getAssemblyLocation().getAddress(), region);
			campaign.getAssemblyLocation().setCoordinate(coordinate);
		}
		campaignDao.insert(campaign);
		return campaign;
	}

	@Override
	public boolean updateCampaign(Campaign campaign, UserProfile user) {
		Campaign found = this.getCampaignById(campaign.getId(), true);
		found.setAssemblyTime(campaign.getAssemblyTime());
		found.setContact(campaign.getContact());
		found.setContactCellphone(campaign.getContactCellphone());
		found.setDesc(campaign.getDesc());
		found.setParticipantLimit(campaign.getParticipantLimit());
		found.setRequireAssembly(campaign.isRequireAssembly());
		found.setShopCode(campaign.getShopCode());
		found.setSubject(campaign.getSubject());
		found.setStartTime(campaign.getStartTime());
		found.setEndTime(campaign.getEndTime());
		found.setLastModified(new Date());

		Set<ConstraintViolation<Campaign>> violations = validator.validate(found, Default.class, CreateCampaign.class);
		if (violations.size() != 0) {
			throw new ValidationException(violations.toArray(new ConstraintViolation[0]));
		}
		if (campaign.getAddress() != null) {
			if (!campaign.getAddress().equals(found.getAddress())) {
				Region region = regionService.getRegionByCode(campaign.getRegionCode(), true);
				Coordinate coordinate = geoService.lookupCoordinate(campaign.getAssemblyLocation().getAddress(), region);
				found.location(new Location(campaign.getAddress(), coordinate));
			}
		}
		return campaignDao.update(found);
	}

	@Override
	public boolean joinCampaign(String campaignId, UserProfile user) {
		Campaign campaign = this.getCampaignById(campaignId, true);
		if (campaign.isCancelled()) {
			String msg = "Cannot join a campaign which is cancelled";
			throw new SpecialityBaseException(CampaignErrorCode.CAMPAIGN_IS_CALCEELD, msg);
		}
		ParticipantJournal journal = campaignDao.getCampaignParticipantJournal(campaignId, user.getId());
		if (journal != null && journal.getDepartCount() >= 3) {
			String msg = "You have ever joined and departed the campaign for over three times";
			throw new SpecialityBaseException(CampaignErrorCode.CAMPAIGN_DEPART_EXCEED_LIMIT, msg);
		}
		if (campaign.getParticipant(user.getId()) == null) {
			int participantLimit = campaign.getParticipantLimit();
			if (participantLimit > 0 && campaign.getParticipants().size() >= participantLimit) {
				String msg = "Exceed participant limit";
				throw new SpecialityBaseException(CampaignErrorCode.EXCEED_PARTICIPANT_LIMIT, msg);
			}
			campaign.addParticipant(new Participant(user.getId()));
			return campaignDao.update(campaign);
		} else {
			return true;
		}
	}

	@Override
	public boolean departCampaign(String campaignId, UserProfile user) {
		Campaign campaign = this.getCampaignById(campaignId, true);
		if (campaign.getParticipant(user.getId()) != null) {
			campaign.removeParticipant(user.getId());
			if (campaignDao.update(campaign)) {
				ParticipantJournal journal = campaignDao.getParticipantJournal(campaignId, user.getId());
				if (journal == null) {
					journal = new ParticipantJournal();
					journal.setCampaignId(campaignId);
				}
				journal.increaseDepartCount();
				return campaignDao.createOrUpdate(journal);
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public void cancellCampaign(String campaignId, UserProfile user) {
		Campaign campaign = this.getCampaignById(campaignId, true);
		if (campaign.getSponsor().equals(user.getId())) {
			campaign.setCancelled(true);
			campaignDao.update(campaign);
		} else {
			String msg = "only sponser are authorized to cancel campaign";
			throw new SpecialityBaseException(CampaignErrorCode.UNAUTHORIZED_CANCEL_CAMPAIGN, msg);
		}
	}

	@Override
	public Campaign getCampaignById(String campaignId, boolean throwException) {
		Campaign campaign = campaignDao.getCampaign(campaignId);
		if (campaign == null && throwException) {
			throw new SpecialityBaseException(CampaignErrorCode.CAMPAIGN_NOT_FOUND, "campaign with id=%s is not found", campaignId);
		}
		return campaign;
	}

	@Override
	public List<Campaign> getParticipantCampaignList(String userId) {
		return campaignDao.getParticipantCampaignList(userId, true, false);
	}

	@Override
	public List<Campaign> getPublishedCampaignList(String userId) {
		return campaignDao.getPublishedCampaignList(userId, true, false);
	}

	@Override
	public List<Campaign> getValidCampaignInRegion(CampaignType type, String regionCode, int offset, int limit) {
		if (type != null) {
			return campaignDao.queryValidCampaignByType(regionCode, type, new Date(), offset, limit);
		} else {
			return campaignDao.queryValidCampaign(regionCode, new Date(), offset, limit);
		}
	}
}

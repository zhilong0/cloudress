package com.df.spec.locality.dao;

import java.util.Date;
import java.util.List;

import com.df.spec.locality.model.Campaign;
import com.df.spec.locality.model.CampaignType;
import com.df.spec.locality.model.ParticipantJournal;

public interface CampaignDao {

	void insert(Campaign campaign);

	boolean update(Campaign campaign);
	
	boolean delete(String campaignId);

	ParticipantJournal getParticipantJournal(String campaignId, String userId);

	List<Campaign> queryValidCampaignByType(String regionCode, CampaignType type, Date currentDate, int offset, int limit);

	List<Campaign> queryValidCampaign(String regionCode, Date currentDate, int offset, int limit);

	Campaign getCampaign(String campaignId);

	ParticipantJournal getCampaignParticipantJournal(String campaignId, String userId);

	boolean createOrUpdate(ParticipantJournal journal);

	boolean addParticipant(Campaign campaign, String userId);

	boolean removeParticipant(Campaign campaign, String userId);

	List<Campaign> getParticipantCampaignList(String userId, boolean includeCancelled, boolean includeExpired);

	List<Campaign> getPublishedCampaignList(String userId, boolean includeCancelled, boolean includeExpired);

}

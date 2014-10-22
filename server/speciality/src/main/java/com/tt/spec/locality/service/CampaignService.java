package com.tt.spec.locality.service;

import java.util.List;

import com.tt.spec.locality.model.Campaign;
import com.tt.spec.locality.model.CampaignType;
import com.tt.spec.locality.model.UserProfile;

public interface CampaignService {

	Campaign createCampaign(Campaign campaign, UserProfile user);

	boolean updateCampaign(Campaign campaign, UserProfile user);

	void cancellCampaign(String campaignId, UserProfile user);

	Campaign getCampaignById(String campaignId, boolean throwException);

	boolean joinCampaign(String campaignId, UserProfile user);

	boolean departCampaign(String campaignId, UserProfile user);

	List<Campaign> getParticipantCampaignList(String userId);

	List<Campaign> getPublishedCampaignList(String userId);

	List<Campaign> getValidCampaignInRegion(CampaignType type, String regionCode, int offset, int limit);

}

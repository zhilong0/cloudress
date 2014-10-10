package com.df.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.df.idm.authentication.UserPropertyAuthenticationToken;
import com.df.idm.authentication.adapter.UserObject;
import com.df.spec.locality.exception.SpecialityBaseException;
import com.df.spec.locality.http.RequestParameterError;
import com.df.spec.locality.model.Campaign;
import com.df.spec.locality.model.CampaignType;
import com.df.spec.locality.model.SpecialityGroupPurcharse;
import com.df.spec.locality.model.UserProfile;
import com.df.spec.locality.service.CampaignService;

@Path("/campaigns")
@Produces("application/json;charset=UTF-8")
@Component
public class CampaignResources {

	private CampaignService campaignService;

	private static final Logger logger = LoggerFactory.getLogger(CampaignResources.class);

	public CampaignResources(CampaignService campaignService) {
		this.campaignService = campaignService;
	}

	public void setCampaignService(CampaignService campaignService) {
		this.campaignService = campaignService;
	}

	@GET
	@Path("/{id}")
	public Campaign getCampaign(@PathParam("id") String campaignId) {
		return campaignService.getCampaignById(campaignId, true);
	}

	private CampaignType getCampainType(String strType) {
		try {
			int ordinal = Integer.parseInt(strType);
			CampaignType[] types = CampaignType.values();
			for (CampaignType type : types) {
				if (type.ordinal() == ordinal) {
					return type;
				}
			}
			throw new SpecialityBaseException(RequestParameterError.INVALID_CAMPAIGN_TYPE, "Invalid campaign type");
		} catch (NumberFormatException ex) {
			try {
				return Enum.valueOf(CampaignType.class, strType);
			} catch (IllegalArgumentException ex2) {
				throw new SpecialityBaseException(RequestParameterError.INVALID_CAMPAIGN_TYPE, "Invalid campaign type");
			}
		}
	}

	@GET
	@Path("/mine")
	public List<Campaign> getValidCampaignList(@QueryParam("offset") int offset, @QueryParam("limit") int limit) {
		if (offset < 0) {
			offset = 0;
		}
		if (limit <= 0) {
			limit = 20;
		}
		return campaignService.getParticipantCampaignList(loadUserProfile().getCode());
	}

	@GET
	@Path("/{regionCode}/{type}")
	public List<Campaign> getValidCampaignList(@PathParam("regionCode") String regionCode, @PathParam("type") String type, @QueryParam("offset") int offset,
			@QueryParam("limit") int limit) {
		CampaignType ct = getCampainType(type);
		if (offset < 0) {
			offset = 0;
		}
		if (limit <= 0) {
			limit = 20;
		}
		return campaignService.getValidCampaignInRegion(ct, regionCode, offset, limit);
	}

	@POST
	@Path("/{regionCode}/0")
	@PreAuthorize("isAuthenticated()")
	public Campaign createCampaign(@PathParam("regionCode") String regionCode, SpecialityGroupPurcharse campaign) {
		campaign.setRegionCode(regionCode);
		return campaignService.createCampaign(campaign, loadUserProfile());
	}

	protected UserProfile loadUserProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		} else if (!(authentication instanceof UserPropertyAuthenticationToken)) {
			logger.error("Unsupported authentication type:" + authentication.getClass().getName());
			return null;
		} else {
			UserPropertyAuthenticationToken uauth = (UserPropertyAuthenticationToken) authentication;
			UserObject details = (UserObject) uauth.getDetails();
			if (details != null) {
				UserProfile user = new UserProfile();
				user.setCode(details.getCode());
				user.setCellPhone(details.getCellphone());
				if (details.getFirstName() == null || details.getLastName() == null) {
					user.setRealName(details.getCode());
				} else {
					user.setRealName(details.getFirstName() + details.getLastName());
				}
				return user;
			} else {
				return null;
			}
		}
	}
}

package com.df.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

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
	public Campaign createCampaign(@PathParam("regionCode") String regionCode, SpecialityGroupPurcharse campaign) {
		return campaignService.createCampaign(campaign, loadUserProfile());
	}

	protected UserProfile loadUserProfile() {
		UserProfile user = new UserProfile();
		user.setId("i061134");
		user.setCellPhone("13621992125");
		user.setRealName("xia pin");
		return null;
	}
}

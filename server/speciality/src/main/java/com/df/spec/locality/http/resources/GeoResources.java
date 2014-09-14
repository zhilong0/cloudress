package com.df.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.df.spec.locality.geo.GeoService;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.service.RegionService;

@Path("/geo")
@Produces("application/json;charset=UTF-8")
@Component
public class GeoResources {

	private GeoService geoService;

	private RegionService regionService;

	public void setGeoService(GeoService geoService) {
		this.geoService = geoService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	@GET
	@Path("/suggestion")
	public List<String> getPlaceSuggestion(String address, String regionCode) {
		Region region = regionService.getRegionByCode(regionCode, true);
		return geoService.getPlaceSuggestion(address, region);
	}

}

package com.df.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.df.spec.locality.geo.CoordType;
import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.service.RegionService;

@Path("/regions")
@Produces("application/json;charset=UTF-8")
@Component
public class RegionResources {

	private RegionService regionService;

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	@GET
	@Path("/")
	public List<Region> getRegionList() {
		return regionService.getRegionList();
	}

	@GET
	@Path("/coordinate")
	public Region getRegionByCoordinate(@QueryParam(value = "latitide") double latitude, @QueryParam(value = "longitude") double longitude) {
		Coordinate coordinate = new Coordinate(CoordType.WGS84LL, latitude, longitude);
		return regionService.getRegionByCoordinate(coordinate);
	}

	@GET
	@Path("/code/{regionCode}")
	public Region getRegionByCode(@PathParam(value = "regionCode") String regionCode) {
		return regionService.getRegionByCode(regionCode, true);
	}

}

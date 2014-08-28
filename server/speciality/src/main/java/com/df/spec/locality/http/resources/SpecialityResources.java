package com.df.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.df.spec.locality.model.Speciality;
import com.df.spec.locality.service.SpecialityService;

@Path("/specialities")
@Produces("application/json;charset=UTF-8")
@Component
public class SpecialityResources {

	private SpecialityService specialityService;

	public void setSpecialityService(SpecialityService specialityService) {
		this.specialityService = specialityService;
	}

	@GET
	@Path("/region/{regionCode}")
	public List<Speciality> getSpecialitiesByRegion(@PathParam(value = "regionCode") String regionCode) {
		return specialityService.getSpecialityListByRegionCode(regionCode);
	}
}

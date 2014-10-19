package com.df.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.http.ImageDetails;
import com.df.blobstore.image.http.ImageLinkCreator;
import com.df.spec.locality.model.ImageSet;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Speciality;
import com.df.spec.locality.service.ApprovableService;
import com.df.spec.locality.service.RegionService;
import com.df.spec.locality.service.SpecialityService;

@Path("/specialities")
@Produces("application/json;charset=UTF-8")
public class SpecialityResources {

	@Autowired
	private SpecialityService specialityService;

	@Autowired
	private RegionService regionService;

	@Autowired
	private ApprovableService approvableService;

	@Autowired
	private ImageLinkCreator imageLinkCreator;

	public void setSpecialityService(SpecialityService specialityService) {
		this.specialityService = specialityService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setApprovableService(ApprovableService approvableService) {
		this.approvableService = approvableService;
	}

	public void setImageLinkCreator(ImageLinkCreator imageLinkCreator) {
		this.imageLinkCreator = imageLinkCreator;
	}

	@GET
	@Path("/region/{regionCode}")
	public List<Speciality> getSpecialitiesByRegion(@PathParam(value = "regionCode") String regionCode) {
		List<Speciality> specialities = specialityService.getSpecialityListByRegionCode(regionCode);
		for (Speciality speciality : specialities) {
			processImageLink(speciality);
		}
		return specialities;
	}

	@GET
	@Path("/mine")
	@PreAuthorize("isAuthenticated()")
	public List<Speciality> getMySpecialities() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return specialityService.getMySpecialities(authentication.getName());
	}

	@POST
	@Path("/code/{specialityCode}/images")
	@PreAuthorize("isAuthenticated() and hasPermission('SPECIALITY','MASTER_DATA_EDIT')")
	public boolean addImages(@PathParam("specialityCode") String specialityCode, String[] imageIds) {
		if (imageIds == null) {
			return true;
		} else {
			return specialityService.updateImageSet(specialityCode, imageIds, true);
		}
	}

	@DELETE
	@Path("/code/{specialityCode}/images")
	@PreAuthorize("isAuthenticated() and hasPermission('SPECIALITY','MASTER_DATA_EDIT')")
	public boolean deleteImages(@PathParam("specialityCode") String specialityCode, List<String> imageIds) {
		if (imageIds == null) {
			return true;
		}
		return specialityService.updateImageSet(specialityCode, imageIds.toArray(new String[0]), false);
	}

	@GET
	@Path("/to_be_approved")
	@PreAuthorize("isAuthenticated() and hasPermission('SPECIALITY','MASTER_DATA_APPROVAL')")
	public List<Speciality> getWaitToApproveList(@QueryParam("offset") int offset, @QueryParam("limit") int limit) {
		if (offset < 0) {
			offset = 0;
		}
		if (limit <= 0) {
			limit = 20;
		}
		List<Speciality> specialities = specialityService.getWaitToApproveList(offset, limit);
		for (Speciality speciality : specialities) {
			processImageLink(speciality);
		}
		return specialities;
	}

	@GET
	@Path("/code/{specialityCode}")
	public Speciality getSpecialitiesByCode(@PathParam("specialityCode") String specialityCode) {
		Speciality speciality = specialityService.getSpecialityByCode(specialityCode, true);
		if (speciality != null) {
			processImageLink(speciality);
		}
		return speciality;
	}

	@POST
	@PreAuthorize("isAuthenticated()")
	public Speciality addSpeciality(Speciality speciality) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		speciality.setCreatedBy(authentication.getName());
		String regionCode = speciality.getRegionCode();
		Region region = regionService.getRegionByCode(regionCode, true);
		specialityService.addSpeciality(speciality, region);
		processImageLink(speciality);
		return speciality;
	}

	@POST
	@Path("/{specialityCode}/reject")
	@PreAuthorize("isAuthenticated() and hasPermission('SPECIALITY','MASTER_DATA_APPROVAL') ")
	public boolean rejectSpeciality(@PathParam("specialityCode") String specialityCode, String rejectReason) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Speciality speciality = specialityService.getSpecialityByCode(specialityCode, true);
		return approvableService.reject(speciality, authentication.getName(), rejectReason);
	}

	@POST
	@Path("/{specialityCode}/approve")
	@PreAuthorize("isAuthenticated() and hasPermission('SPECIALITY','MASTER_DATA_APPROVAL')")
	public boolean approveSpeciality(@PathParam("specialityCode") String specialityCode) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Speciality speciality = specialityService.getSpecialityByCode(specialityCode, true);
		return approvableService.approve(speciality, authentication.getName());
	}

	protected void processImageLink(Speciality speciality) {
		ImageSet imageSet = speciality.getImageSet();
		// set the image from the first image in the image set
		speciality.setImage(null);
		for (ImageDetails image : imageSet.getImages()) {
			String link = imageLinkCreator.createImageLink(new ImageKey(image.getImageId()), image.getAttributes());
			if (speciality.getImage() == null) {
				speciality.setImage(link);
			}
			image.setImageLink(link);
		}
	}
}

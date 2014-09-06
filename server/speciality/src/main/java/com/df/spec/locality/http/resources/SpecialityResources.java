package com.df.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.http.ImageDetails;
import com.df.blobstore.image.http.ImageLinkCreator;
import com.df.spec.locality.model.ImageSet;
import com.df.spec.locality.model.Speciality;
import com.df.spec.locality.service.SpecialityService;

@Path("/specialities")
@Produces("application/json;charset=UTF-8")
@Component
public class SpecialityResources {

	private SpecialityService specialityService;

	private ImageLinkCreator imageLinkCreator;

	public void setSpecialityService(SpecialityService specialityService) {
		this.specialityService = specialityService;
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
	@Path("/code/{specialityCode}")
	public Speciality getSpecialitiesByCode(@PathParam(value = "specialityCode") String specialityCode) {
		Speciality speciality = specialityService.getSpecialityByCode(specialityCode);
		if (speciality != null) {
			processImageLink(speciality);
		}
		return speciality;
	}

	protected void processImageLink(Speciality speciality) {
		ImageSet imageSet = speciality.getImageSet();
		for (ImageDetails image : imageSet.getImages()) {
			String link = imageLinkCreator.createImageLink(new ImageKey(image.getImageId()), image.getAttributes());
			if (speciality.getImage()== null) {
				speciality.setImage(link);
			}
			image.setImageLink(link);
		}
	}
}

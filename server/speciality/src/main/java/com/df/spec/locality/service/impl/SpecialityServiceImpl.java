package com.df.spec.locality.service.impl;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.ImageService;
import com.df.blobstore.image.http.ImageDetails;
import com.df.spec.locality.dao.SpecialityDao;
import com.df.spec.locality.exception.SpecialityWithCodeNotFoundException;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Speciality;
import com.df.spec.locality.service.RegionService;
import com.df.spec.locality.service.SpecialityService;

public class SpecialityServiceImpl implements SpecialityService {

	private SpecialityDao specialityDao;

	private ImageService imageService;

	private RegionService regionService;

	public SpecialityServiceImpl(SpecialityDao specialityDao, RegionService regionService, ImageService imageService) {
		this.specialityDao = specialityDao;
		this.imageService = imageService;
		this.regionService = regionService;
	}

	public void setSpecialityDao(SpecialityDao specialityDao) {
		this.specialityDao = specialityDao;
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	@Override
	public void addSpeciality(Speciality speciality, String regionCode) {
		Region region = regionService.getRegionByCode(regionCode, true);
		specialityDao.add(speciality, region);
	}

	@Override
	public String uploadSpecialityImage(String specialityCode, byte[] imageData, String imageName) {
		ImageKey key = imageService.uploadImage(new ByteArrayInputStream(imageData), null, imageName);
		Speciality found = specialityDao.getSpecialityByCode(specialityCode);
		if (found == null) {
			throw new SpecialityWithCodeNotFoundException(null, specialityCode);
		}
		ImageAttributes imageAttributes = imageService.getImageAttributes(key);
		specialityDao.addImage(specialityCode, new ImageDetails(key.getKey(), imageAttributes));
		return key.getKey();
	}

	@Override
	public void deleteSpecialityImage(String specialityCode, String imageId) {
		specialityDao.removeImage(specialityCode, imageId);
		imageService.deleteImage(new ImageKey(imageId));
	}

	@Override
	public List<Speciality> getSpecialityListByRegionCode(String regionCode) {
		return specialityDao.getSpecialityListByRegionCode(regionCode);
	}

	@Override
	public Speciality getSpecialityByCode(String specialityCode) {
		return specialityDao.getSpecialityByCode(specialityCode);
	}

	public Speciality findSpeciality(String regionCode, String specialityName) {
		return specialityDao.findSpeciality(regionCode, specialityName);
	}

	@Override
	public void update(Speciality spec) {
		specialityDao.update(spec);
	}

}

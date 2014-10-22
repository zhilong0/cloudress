package com.tt.spec.locality.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.tt.blobstore.image.ImageAttributes;
import com.tt.blobstore.image.ImageKey;
import com.tt.blobstore.image.ImageService;
import com.tt.blobstore.image.http.ImageDetails;
import com.tt.spec.locality.dao.SpecialityDao;
import com.tt.spec.locality.data.streamline.SpecialitySeasonalComparator;
import com.tt.spec.locality.exception.SpecialityWithCodeNotFoundException;
import com.tt.spec.locality.exception.validation.ValidationException;
import com.tt.spec.locality.model.Region;
import com.tt.spec.locality.model.Speciality;
import com.tt.spec.locality.service.OperationPermissionEvaluator;
import com.tt.spec.locality.service.SpecialityService;

public class SpecialityServiceImpl implements SpecialityService {

	private SpecialityDao specialityDao;

	private ImageService imageService;

	private Validator validator;

	private OperationPermissionEvaluator permissionEvaluator;

	public SpecialityServiceImpl(SpecialityDao specialityDao, ImageService imageService, OperationPermissionEvaluator permissionEvaluator, Validator validator) {
		this.specialityDao = specialityDao;
		this.imageService = imageService;
		this.permissionEvaluator = permissionEvaluator;
		this.validator = validator;
	}

	public void setSpecialityDao(SpecialityDao specialityDao) {
		this.specialityDao = specialityDao;
	}

	public void setServiceOperationPermissionEvaluator(OperationPermissionEvaluator serviceOperationPermissionEvaluator) {
		this.permissionEvaluator = serviceOperationPermissionEvaluator;
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@Override
	public void addSpeciality(Speciality speciality, Region region) {
		speciality.setRegionCode(region.getCode());
		Set<ConstraintViolation<Speciality>> violations = validator.validate(speciality);
		if (violations.size() != 0) {
			throw new ValidationException(violations.toArray(new ConstraintViolation[0]));
		}
		speciality.setCreatedTime(new Date());
		speciality.setChangedTime(null);
		if (speciality.getImage() != null) {
			if (imageService != null) {
				ImageAttributes imageAttributes = imageService.getImageAttributes(new ImageKey(speciality.getImage()));
				if (imageAttributes != null) {
					speciality.getImageSet().addImage(0, speciality.getImage(), imageAttributes);
				}
			} else {
				speciality.getImageSet().addImage(0, speciality.getImage(), null);
			}
		}
		if (permissionEvaluator.canAddSpeciality(speciality.getCreatedBy())) {
			speciality.approved(speciality.getCreatedBy());
		} else {
			speciality.waitToApprove();
		}
		specialityDao.add(speciality, region);
	}

	@Override
	public List<Speciality> getSpecialityListByRegionCode(String regionCode) {
		SpecialitySeasonalComparator comparator = new SpecialitySeasonalComparator();
		List<Speciality> result = specialityDao.getSpecialityListByRegionCode(regionCode);
		Collections.sort(result, comparator);
		return result;
	}

	@Override
	public Speciality getSpecialityByCode(String specialityCode, boolean throwException) {
		Speciality found = specialityDao.findById(Speciality.class, specialityCode);
		if (found == null && throwException) {
			throw new SpecialityWithCodeNotFoundException(null, specialityCode);
		}
		return found;
	}

	public Speciality findSpeciality(String regionCode, String specialityName) {
		return specialityDao.findSpeciality(regionCode, specialityName);
	}

	@Override
	public boolean update(Speciality speciality) {
		Set<ConstraintViolation<Speciality>> violations = validator.validate(speciality);
		if (violations.size() != 0) {
			throw new ValidationException(violations.toArray(new ConstraintViolation[0]));
		}
		speciality.setChangedTime(new Date());
		return specialityDao.update(speciality);
	}

	@Override
	public List<Speciality> getMySpecialities(String userCode) {
		return specialityDao.getSpecialityListByCreatedBy(userCode);
	}

	@Override
	public List<Speciality> getWaitToApproveList(int offset, int limit) {
		return specialityDao.getWaitToApproveSpecialityList(offset, limit);
	}

	@Override
	public boolean updateImageSet(String specialityCode, String[] imageIds, boolean isAdd) {
		if (isAdd) {
			List<ImageDetails> images = new ArrayList<ImageDetails>();
			for (String imageId : imageIds) {
				ImageAttributes imageAttributes = null;
				if (imageService != null) {
					imageAttributes = imageService.getImageAttributes(new ImageKey(imageId));
				}
				images.add(new ImageDetails(imageId, imageAttributes));
			}
			return specialityDao.addImages(specialityCode, images.toArray(new ImageDetails[0]));
		} else {
			return specialityDao.removeImages(specialityCode, imageIds);
		}
	}

}

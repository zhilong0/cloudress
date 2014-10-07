package com.df.spec.locality.service.impl;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.ImageService;
import com.df.blobstore.image.http.ImageDetails;
import com.df.spec.locality.dao.SpecialityDao;
import com.df.spec.locality.data.streamline.SpecialitySeasonalComparator;
import com.df.spec.locality.exception.SpecialityBaseException;
import com.df.spec.locality.exception.SpecialityErrorCode;
import com.df.spec.locality.exception.SpecialityWithCodeNotFoundException;
import com.df.spec.locality.exception.validation.ValidationException;
import com.df.spec.locality.model.Constants;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Speciality;
import com.df.spec.locality.model.Approval.Status;
import com.df.spec.locality.service.RegionService;
import com.df.spec.locality.service.OperationPermissionEvaluator;
import com.df.spec.locality.service.SpecialityService;

public class SpecialityServiceImpl implements SpecialityService {

	private SpecialityDao specialityDao;

	private ImageService imageService;

	private RegionService regionService;

	private Validator validator;

	private OperationPermissionEvaluator permissionEvaluator;

	public SpecialityServiceImpl(SpecialityDao specialityDao, RegionService regionService, ImageService imageService,
			OperationPermissionEvaluator permissionEvaluator, Validator validator) {
		this.specialityDao = specialityDao;
		this.imageService = imageService;
		this.regionService = regionService;
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

	@Override
	public void addSpeciality(Speciality speciality) {
		Set<ConstraintViolation<Speciality>> violations = validator.validate(speciality);
		if (violations.size() != 0) {
			throw new ValidationException(violations.toArray(new ConstraintViolation[0]));
		}
		Region region = regionService.getRegionByCode(speciality.getRegionCode(), true);
		speciality.setCreatedTime(new Date());
		speciality.setChangedTime(null);
		if (speciality.getImage() != null) {
			ImageAttributes imageAttributes = imageService.getImageAttributes(new ImageKey(speciality.getImage()));
			if (imageAttributes != null) {
				speciality.getImageSet().addImage(speciality.getImage(), imageAttributes);
			}
		}
		if (permissionEvaluator.canAddShop(speciality.getCreatedBy())) {
			speciality.approve(speciality.getCreatedBy());
		} else if (speciality.getApproval() != null) {
			speciality.getApproval().setApprovedBy(null);
			speciality.getApproval().setApprovedTime(null);
			speciality.getApproval().setStatus(null);
			speciality.getApproval().setRejectReason(null);
		}
		specialityDao.add(speciality, region);
	}

	@Override
	public String uploadSpecialityImage(String specialityCode, byte[] imageData, String imageName) {
		Speciality found = specialityDao.getSpecialityByCode(specialityCode);
		if (found == null) {
			throw new SpecialityWithCodeNotFoundException(null, specialityCode);
		}
		ImageKey key = imageService.uploadImage(new ByteArrayInputStream(imageData), null, imageName);
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
		SpecialitySeasonalComparator comparator = new SpecialitySeasonalComparator();
		List<Speciality> result = specialityDao.getSpecialityListByRegionCode(regionCode);
		Collections.sort(result, comparator);
		return result;
	}

	@Override
	public Speciality getSpecialityByCode(String specialityCode) {
		return specialityDao.getSpecialityByCode(specialityCode);
	}

	public Speciality findSpeciality(String regionCode, String specialityName) {
		return specialityDao.findSpeciality(regionCode, specialityName);
	}

	@Override
	public boolean update(Speciality spec) {
		return specialityDao.update(spec);
	}

	@Override
	public boolean approveSpeciality(String specialityCode, String approver) {
		if (permissionEvaluator.canApproveSpeciality(approver)) {
			Properties p = new Properties();
			p.put(Constants.SPECIALITY.APPROVED_BY, approver);
			p.put(Constants.SPECIALITY.APPROVED_TIME, new Date());
			p.put(Constants.SPECIALITY.STATUS, Status.APPROVED);
			p.put(Constants.SPECIALITY.REJECT_REASON, null);
			return specialityDao.update(specialityCode, p);
		} else {
			throw new SpecialityBaseException(SpecialityErrorCode.NO_APPROVAL_PERMISSION);
		}
	}

	@Override
	public boolean rejectSpeciality(String specialityCode, String approver, String rejectReason) {
		if (permissionEvaluator.canApproveSpeciality(approver)) {
			Properties p = new Properties();
			p.put(Constants.SPECIALITY.APPROVED_BY, approver);
			p.put(Constants.SPECIALITY.APPROVED_TIME, new Date());
			p.put(Constants.SPECIALITY.STATUS, Status.REJECTED);
			p.put(Constants.SPECIALITY.REJECT_REASON, rejectReason);
			return specialityDao.update(specialityCode, p);
		} else {
			throw new SpecialityBaseException(SpecialityErrorCode.NO_APPROVAL_PERMISSION);
		}
	}

	@Override
	public List<Speciality> getMySpecialities(String userCode) {
		return specialityDao.getSpecialityListByCreatedBy(userCode);
	}

}

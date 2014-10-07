package com.df.spec.locality.exception;

import com.df.spec.locality.model.Region;

public class SpecialityAlreadyExistException extends SpecialityBaseException {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE_FMT = "Speciality %s in region %s already exist";

	private String specialityName;

	private Region region;

	public SpecialityAlreadyExistException(Throwable cause, String specialityName, Region region) {
		super(cause, MESSAGE_FMT, specialityName, region.toString());
		this.specialityName = specialityName;
		this.setErrorCode(SpecialityErrorCode.SPECIALITY_IN_REGION_ALREADY_EXIST);
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public Region getRegion() {
		return region;
	}
}

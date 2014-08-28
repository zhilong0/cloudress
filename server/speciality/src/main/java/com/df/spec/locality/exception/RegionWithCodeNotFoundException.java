package com.df.spec.locality.exception;

public class RegionWithCodeNotFoundException extends SpecialityBaseException {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE_FMT = "Region with code %s not found.";

	private String regionCode;

	public RegionWithCodeNotFoundException(Throwable cause, String regionCode) {
		super(cause, MESSAGE_FMT, regionCode);
		this.regionCode = regionCode;
		this.setErrorCode(RegionErrorCode.REGION_NOT_FOUND_ERROR);
	}

	public String getRegionCode() {
		return regionCode;
	}

}

package com.tt.spec.locality.exception;

import com.tt.spec.locality.model.Region;

public class DuplicateRegionException extends SpecialityBaseException {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE_FMT = "Duplicate region with province=%s, city=%s, district=%s";

	private Region region;

	public DuplicateRegionException(Throwable cause, Region region) {
		super(cause, MESSAGE_FMT, region.getProvince(), region.getCity(), region.getDistrict());
		this.region = region;
		this.setErrorCode(RegionErrorCode.DUPLICATE_REGION_ERROR);
	}

	public Region getRegion() {
		return region;
	}

}

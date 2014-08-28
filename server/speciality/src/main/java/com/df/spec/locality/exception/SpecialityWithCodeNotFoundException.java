package com.df.spec.locality.exception;

public class SpecialityWithCodeNotFoundException extends SpecialityBaseException {

	private static final long serialVersionUID = 1L;

	public static final String SPECIALITY_NOT_FOUND_ERROR = "S100001";

	private static final String MESSAGE_FMT = "Speciality with code %s not found.";

	private String specialityCode;

	public SpecialityWithCodeNotFoundException(Throwable cause, String specialityCode) {
		super(cause, MESSAGE_FMT, specialityCode);
		this.specialityCode = specialityCode;
	}

	public String getSpecialityCode() {
		return specialityCode;
	}

}

package com.df.spec.locality.model;

import java.io.Serializable;

public class Goods implements Serializable {

	private static final long serialVersionUID = 1L;

	private String specialityCode;

	Goods() {
	}

	public Goods(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	public String getSpecialityCode() {
		return specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

}

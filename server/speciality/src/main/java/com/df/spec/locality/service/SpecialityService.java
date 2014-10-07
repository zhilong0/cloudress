package com.df.spec.locality.service;

import java.util.List;

import com.df.spec.locality.model.Speciality;

public interface SpecialityService {

	void addSpeciality(Speciality speciality);

	boolean approveSpeciality(String specialityCode, String approver);

	boolean rejectSpeciality(String specialityCode, String approver, String rejectReason);

	List<Speciality> getSpecialityListByRegionCode(String regionCode);
	
	List<Speciality> getMySpecialities(String userCode);

	String uploadSpecialityImage(String specialityCode, byte[] imageData, String imageName);

	void deleteSpecialityImage(String specialityCode, String imageId);

	Speciality getSpecialityByCode(String specialityCode);

	Speciality findSpeciality(String regionCode, String specialityName);

	boolean update(Speciality spec);

}

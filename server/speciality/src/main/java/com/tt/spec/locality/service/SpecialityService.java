package com.tt.spec.locality.service;

import java.util.List;

import com.tt.spec.locality.model.Region;
import com.tt.spec.locality.model.Speciality;

public interface SpecialityService {

	void addSpeciality(Speciality speciality, Region region);

	List<Speciality> getSpecialityListByRegionCode(String regionCode);

	List<Speciality> getMySpecialities(String userCode);

	boolean updateImageSet(String specialityCode, String[] imageIds, boolean isAdd);

	Speciality getSpecialityByCode(String specialityCode, boolean throwException);

	Speciality findSpeciality(String regionCode, String specialityName);

	boolean update(Speciality spec);

	List<Speciality> getWaitToApproveList(int offset, int limit);

}

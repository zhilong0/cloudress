package com.df.spec.locality.dao;

import java.util.List;

import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Speciality;

public interface SpecialityDao {

	void add(Speciality spec, Region region);

	void update(Speciality speciality);

	void addImage(String specialityCode, String imageId);

	void removeImage(String specialityCode, String imageId);

	void delete(String specialityCode);

	Speciality getSpecialityByCode(String specialityCode);

	List<Speciality> getSpecialityListByRegionCode(String regionCode);

}

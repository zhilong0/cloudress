package com.df.spec.locality.dao;

import java.util.List;
import java.util.Properties;

import com.df.blobstore.image.http.ImageDetails;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Speciality;

public interface SpecialityDao {

	void add(Speciality spec, Region region);

	boolean update(String specialityCode, Properties properties);

	boolean update(Speciality speciality);

	void addImage(String specialityCode, ImageDetails image);

	void removeImage(String specialityCode, String imageId);

	void delete(String specialityCode);

	Speciality getSpecialityByCode(String specialityCode);
	
	List<Speciality> getSpecialityListByCreatedBy(String createdBy);

	List<Speciality> getSpecialityListByRegionCode(String regionCode);

	Speciality findSpeciality(String regionCode, String specialityName);

}

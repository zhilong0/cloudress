package com.df.spec.locality.dao;

import java.util.List;

import com.df.blobstore.image.http.ImageDetails;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Speciality;

public interface SpecialityDao {

	void add(Speciality spec, Region region);

	boolean update(Speciality speciality);

	void addImage(String specialityCode, ImageDetails image);

	void removeImage(String specialityCode, String imageId);

	void delete(String specialityCode);

	Speciality getSpecialityByCode(String specialityCode);

	List<Speciality> getSpecialityListByCreatedBy(String createdBy);

	List<Speciality> getSpecialityListByRegionCode(String regionCode);

	Speciality findSpeciality(String regionCode, String specialityName);

	List<Speciality> getWaitList(int offset, int limit);
}

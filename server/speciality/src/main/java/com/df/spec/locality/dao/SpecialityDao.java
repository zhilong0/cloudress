package com.df.spec.locality.dao;

import java.util.List;

import com.df.blobstore.image.http.ImageDetails;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.model.Speciality;

public interface SpecialityDao extends DaoTemplate {

	void add(Speciality spec, Region region);

	boolean update(Speciality speciality);

	boolean addImages(String specialityCode, ImageDetails[] images);

	boolean removeImages(String specialityCode, String[] imageIds);

	void delete(String specialityCode);

	List<Speciality> getSpecialityListByCreatedBy(String createdBy);

	List<Speciality> getSpecialityListByRegionCode(String regionCode);

	Speciality findSpeciality(String regionCode, String specialityName);

	List<Speciality> getWaitToApproveSpecialityList(int offset, int limit);
}

package com.df.spec.locality.dao;

import java.util.List;

import com.df.spec.locality.model.Region;

public interface RegionDao {

	Region addRegion(Region newRegion);

	Region findRegion(Region region);

	Region getRegionByCode(String regionCode);

	List<Region> getRegionList();

	boolean deleteRegion(Region region);

}

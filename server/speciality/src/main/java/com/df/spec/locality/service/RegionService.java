package com.df.spec.locality.service;

import java.io.InputStream;
import java.util.List;

import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.model.Region;

public interface RegionService {

	public void addRegion(Region region);

	List<Region> getRegionList();

	Region getRegionByCode(String regionCode, boolean throwException);

	Region getRegionByCoordinate(Coordinate coordinate);

	void importFromCSV(InputStream in,boolean continueOnError);
	
	Region findRegion(String province, String city, String district);
}

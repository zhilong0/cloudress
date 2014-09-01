package com.df.spec.locality.geo;

import java.util.List;

import com.df.spec.locality.model.Region;

public interface GeoService {

	Coordinate lookupCoordiate(String address, Region filter);

	Region lookupRegionWithCoordiate(Coordinate coordinate);

	List<String> getPlaceSuggestion(String address, Region filter);

}
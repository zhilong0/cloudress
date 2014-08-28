package com.df.spec.locality.service;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.df.spec.locality.dao.SpecialityBaseTest;
import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.geo.GeoService;
import com.df.spec.locality.model.Region;

public class GeoServiceTest extends SpecialityBaseTest {

	@Autowired
	private GeoService geoService;

	@Test
	public void testLookupCoordiate() {
		Coordinate coordiate = geoService.lookupCoordiate("汨罗市天恒超市", new Region("湖南省", "岳阳市", "汨罗市"));
		TestCase.assertNotNull(coordiate);
	}

	@Test
	public void testLookupRegionWithCoordiate() {
		Coordinate coordiate = geoService.lookupCoordiate("汨罗市天恒超市", new Region("湖南省", "岳阳市", "汨罗市"));
		Region region = geoService.lookupRegionWithCoordiate(coordiate);
		TestCase.assertEquals(region.getProvince(), "湖南省");
		TestCase.assertEquals(region.getCity(), "岳阳市");
		TestCase.assertEquals(region.getDistrict(), "汨罗市");

	}

	@Test
	public void testGetPlaceSuggestion() {
		List<String> suggestions = geoService.getPlaceSuggestion("天安门", new Region("北京市", "北京市"));
		TestCase.assertTrue(suggestions.size() > 0);
	}

}

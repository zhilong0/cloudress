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
		Coordinate coordiate = geoService.lookupCoordiate("��������㳬��", new Region("����ʡ", "������", "������"));
		TestCase.assertNotNull(coordiate);
	}

	@Test
	public void testLookupRegionWithCoordiate() {
		Coordinate coordiate = geoService.lookupCoordiate("��������㳬��", new Region("����ʡ", "������", "������"));
		Region region = geoService.lookupRegionWithCoordiate(coordiate);
		TestCase.assertEquals(region.getProvince(), "����ʡ");
		TestCase.assertEquals(region.getCity(), "������");
		TestCase.assertEquals(region.getDistrict(), "������");

	}

	@Test
	public void testGetPlaceSuggestion() {
		List<String> suggestions = geoService.getPlaceSuggestion("�찲��", new Region("������", "������"));
		TestCase.assertTrue(suggestions.size() > 0);
	}

}

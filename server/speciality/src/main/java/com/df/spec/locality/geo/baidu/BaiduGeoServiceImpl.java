package com.df.spec.locality.geo.baidu;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.df.spec.locality.geo.CoordType;
import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.geo.GeoService;
import com.df.spec.locality.geo.baidu.BaiduGeoServiceImpl.GeoResult.AddressComponent;
import com.df.spec.locality.geo.baidu.BaiduGeoServiceImpl.PlaceSuggestionResponse.Suggestion;
import com.df.spec.locality.model.Region;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaiduGeoServiceImpl implements GeoService {

	private RestTemplate restTemplate;

	private String ak = "pCPMbjiAzVcWs26VqGw9PUQr";

	private static final String GEO_URL = "http://api.map.baidu.com/geocoder/v2/";

	private static final String SUGGESTION_URL = "http://api.map.baidu.com/place/v2/suggestion/";

	private static final Logger logger = LoggerFactory.getLogger(BaiduGeoServiceImpl.class);

	public BaiduGeoServiceImpl(RestTemplate restTemplate, String ak) {
		this.restTemplate = restTemplate;
		this.ak = ak;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}

	@Override
	public Coordinate lookupCoordiate(String address, Region filter) {
		String url = GEO_URL + "?address={address}&output=json&ak={ak}&city={city}";
		String normalizedAddress = address;
		if (filter.getDistrict() == null && !address.startsWith(filter.getCity())) {
			normalizedAddress = filter.getCity() + address;
		} else if (filter.getDistrict() != null && !address.startsWith(filter.getDistrict())) {
			normalizedAddress = filter.getDistrict() + address;
		}
		ResponseEntity<BaiduGeoResponse> entity = restTemplate.getForEntity(url, BaiduGeoResponse.class, normalizedAddress, this.ak, filter.getCity());
		if (entity.getStatusCode() == HttpStatus.OK) {
			BaiduGeoResponse response = entity.getBody();
			if (response.status == 0) {
				return new Coordinate(CoordType.WGS84LL, response.result.location.lat, response.result.location.lng);
			} else {
				logger.debug("Get coordinate for address {} returns status {}", address, response.status);
			}
		}
		return null;
	}

	@Override
	public Region lookupRegionWithCoordiate(Coordinate coordinate) {
		String url = GEO_URL + "?location={lat},{lng}&ak={ak}&pois=0&output=json&coordtype=wgs84ll";
		ResponseEntity<BaiduGeoResponse> entity = restTemplate.getForEntity(url, BaiduGeoResponse.class, coordinate.getLatitude(), coordinate.getLongitude(),
				this.ak);
		if (entity.getStatusCode() == HttpStatus.OK) {
			BaiduGeoResponse response = entity.getBody();
			if (response.status == 0) {
				AddressComponent ac = response.result.addressComponent;
				return new Region(ac.province, ac.city, ac.district);
			}
		}
		return null;
	}

	@Override
	public List<String> getPlaceSuggestion(String address, Region filter) {
		String url = SUGGESTION_URL + "?query={address}&ak={ak}&region={city}&output=json";
		ResponseEntity<PlaceSuggestionResponse> entity = restTemplate.getForEntity(url, PlaceSuggestionResponse.class, address, this.ak, filter.getCity());
		ArrayList<String> places = new ArrayList<String>();
		if (entity.getStatusCode() == HttpStatus.OK) {
			PlaceSuggestionResponse response = entity.getBody();
			if (response.status == 0) {
				for (Suggestion suggestion : response.result) {
					places.add(suggestion.name);
				}
			}
		}
		return places;
	}

	static class BaiduGeoResponse {
		@JsonProperty
		int status;

		@JsonProperty
		String msg;

		@JsonProperty
		GeoResult result;
	}

	static class PlaceSuggestionResponse {

		@JsonProperty
		int status;

		@JsonProperty
		String msg;

		@JsonProperty
		Suggestion[] result;

		static class Suggestion {
			@JsonProperty
			String name;
		}
	}

	static class GeoResult {

		static class Location {
			@JsonProperty
			double lat;

			@JsonProperty
			double lng;
		}

		static class AddressComponent {
			@JsonProperty
			String province;

			@JsonProperty
			String city;

			@JsonProperty
			String district;
		}

		@JsonProperty
		Location location;

		@JsonProperty
		AddressComponent addressComponent;

		@JsonProperty
		int precise;

		@JsonProperty
		int confidence;

		@JsonProperty
		String level;
	}

}
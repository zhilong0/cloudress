package com.tt.spec.locality.geo.baidu;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tt.spec.locality.exception.GeoErrorCode;
import com.tt.spec.locality.exception.SpecialityBaseException;
import com.tt.spec.locality.geo.CoordType;
import com.tt.spec.locality.geo.Coordinate;
import com.tt.spec.locality.geo.GeoService;
import com.tt.spec.locality.geo.baidu.BaiduGeoServiceImpl.GeoConvertResult.Coord;
import com.tt.spec.locality.geo.baidu.BaiduGeoServiceImpl.GeoResult.AddressComponent;
import com.tt.spec.locality.geo.baidu.BaiduGeoServiceImpl.PlaceSuggestionResponse.Suggestion;
import com.tt.spec.locality.model.Region;

public class BaiduGeoServiceImpl implements GeoService {

	private RestTemplate restTemplate;

	private String ak = "pCPMbjiAzVcWs26VqGw9PUQr";

	private static final String GEO_URL = "http://api.map.baidu.com/geocoder/v2/";

	private static final String SUGGESTION_URL = "http://api.map.baidu.com/place/v2/suggestion/";

	private static final String GEO_CONVERT_URL = "http://api.map.baidu.com/geoconv/v1/";

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

	private SpecialityBaseException createHttpRequestError(ResponseEntity<?> response) {
		return new SpecialityBaseException(GeoErrorCode.GEO_HTTP_REQUEST_ERROR, "HTTP request error:%s", response.toString());
	}

	private SpecialityBaseException createBaiDuServiceError(int baiduServiceStatus, String message) {
		return new SpecialityBaseException(GeoErrorCode.GEO_HTTP_REQUEST_ERROR, "Baidu service status:%d, message:%s", baiduServiceStatus, message);
	}

	@Override
	public Coordinate lookupCoordinate(String address, Region filter) {
		String url = GEO_URL + "?address={address}&output=json&ak={ak}&city={city}";
		String normalizedAddress = address;
		if (filter != null) {
			if (filter.getDistrict() == null && !address.startsWith(filter.getCity())) {
				normalizedAddress = filter.getCity() + address;
			} else if (filter.getDistrict() != null && !address.startsWith(filter.getDistrict())) {
				normalizedAddress = filter.getDistrict() + address;
			}
		}
		String city = filter == null ? "" : filter.getCity();
		ResponseEntity<BaiduGeoResponse> entity = restTemplate.getForEntity(url, BaiduGeoResponse.class, normalizedAddress, this.ak, city);
		if (entity.getStatusCode() == HttpStatus.OK) {
			BaiduGeoResponse response = entity.getBody();
			if (response.status == 0) {
				return new Coordinate(CoordType.BD09LL, response.result.location.lat, response.result.location.lng);
			} else {
				logger.debug("Get coordinate for address {} returns status {}", address, response.status);
				throw this.createBaiDuServiceError(response.status, response.msg);
			}
		} else {
			throw this.createHttpRequestError(entity);
		}

	}

	@Override
	public Region lookupRegionWithCoordiate(Coordinate coordinate) {
		String coordinateType = "bd09ll";
		if (coordinate.getCoordType() == CoordType.WGS84LL) {
			coordinateType = CoordType.WGS84LL.name().toLowerCase();
		} else if (coordinate.getCoordType() == CoordType.GCJ02LL) {
			coordinateType = CoordType.GCJ02LL.name().toLowerCase();
		}
		String url = GEO_URL + "?location={lat},{lng}&ak={ak}&pois=0&output=json&coordtype={ct}";
		ResponseEntity<BaiduGeoResponse> entity = restTemplate.getForEntity(url, BaiduGeoResponse.class, coordinate.getLatitude(), coordinate.getLongitude(),
				this.ak, coordinateType);
		if (entity.getStatusCode() == HttpStatus.OK) {
			BaiduGeoResponse response = entity.getBody();
			if (response.status == 0) {
				AddressComponent ac = response.result.addressComponent;
				return new Region(ac.province, ac.city, ac.district);
			} else {
				throw this.createBaiDuServiceError(response.status, response.msg);
			}
		} else {
			throw this.createHttpRequestError(entity);
		}
	}

	@Override
	public Coordinate convertCoordinate(Coordinate coordinate, CoordType coordType) {
		if (coordType == coordinate.getCoordType()) {
			return coordinate;
		}
		if (coordinate.getCoordType() != CoordType.WGS84LL && coordType != CoordType.BD09LL) {
			String msg = "Only support convert coordinate type from %s to %s";
			throw new SpecialityBaseException(GeoErrorCode.GEO_COORDINATE_CONVERT_NOT_SUPPORTED, msg, CoordType.WGS84LL, CoordType.BD09LL);
		}
		String url = GEO_CONVERT_URL + "?coords={lat},{lng}&ak={ak}&from={from}&to={to}";
		ResponseEntity<GeoConvertResult> entity = restTemplate.getForEntity(url, GeoConvertResult.class, coordinate.getLatitude(), coordinate.getLongitude(),
				this.ak, 1, 5);
		if (entity.getStatusCode() == HttpStatus.OK) {
			GeoConvertResult response = entity.getBody();
			if (response.status == 0) {
				Coord[] c = response.result;
				if (c.length > 0) {
					return new Coordinate(CoordType.BD09LL, c[0].x, c[0].y);
				}
			} else {
				throw this.createBaiDuServiceError(response.status, response.msg);
			}
		} else {
			throw this.createHttpRequestError(entity);
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
			} else {
				throw this.createBaiDuServiceError(response.status, response.msg);
			}
		} else {
			throw this.createHttpRequestError(entity);
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

	static class GeoConvertResult {

		@JsonProperty
		int status;

		@JsonProperty
		String msg;

		@JsonProperty
		Coord[] result;

		static class Coord {
			@JsonProperty
			double x;

			@JsonProperty
			double y;
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
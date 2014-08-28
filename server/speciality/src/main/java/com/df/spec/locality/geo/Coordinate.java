package com.df.spec.locality.geo;

import java.io.Serializable;

public final class Coordinate implements Serializable {

	private static final long serialVersionUID = 1L;

	private double latitude;

	private double longitude;

	private CoordType coordType;

	public Coordinate(CoordType coordType, double latitude, double longitude) {
		this.coordType = coordType;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public CoordType getCoordType() {
		return coordType;
	}

	public void setCoordType(CoordType coordType) {
		this.coordType = coordType;
	}
}

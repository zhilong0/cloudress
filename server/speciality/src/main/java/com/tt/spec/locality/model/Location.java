package com.tt.spec.locality.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import com.tt.spec.locality.geo.Coordinate;

public class Location implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "{shop.location.address.NotEmpty}")
	private String address;

	private Coordinate coordinate;

	Location() {
	}

	public Location(String address, Coordinate coordinate) {
		this.address = address;
		this.coordinate = coordinate;
	}

	public String getAddress() {
		return address;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}

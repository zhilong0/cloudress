package com.df.spec.locality.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.df.spec.locality.geo.Coordinate;

@Entity(value = "shops", noClassnameStored = true)
public class Shop implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String shopCode;

	private String regionCode;

	private String address;

	private Coordinate coordinate;

	@Indexed(unique = true)
	private String name;

	private String displayName;

	private String descriptor;

	public String getCode() {
		return shopCode;
	}

	public void setCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

}

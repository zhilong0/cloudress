package com.df.spec.locality.model.view;

import java.io.Serializable;

import com.df.spec.locality.model.Location;
import com.df.spec.locality.model.Shop;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Supplier implements Serializable {

	private static final long serialVersionUID = 1L;

	private String shopCode;

	private String shopName;

	private float score;

	private int distance;

	private Location location;

	public Supplier(Shop shop) {
		this.shopCode = shop.getCode();
		this.location = shop.getLocation();
		this.shopName = shop.getName();
		this.score = shop.getScore();
	}

	public Supplier() {
	}

	@JsonProperty(value = "code")
	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	@JsonProperty(value = "name")
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@JsonProperty(value = "rank")
	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
}

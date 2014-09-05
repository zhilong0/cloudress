package com.df.spec.locality.model.view;

import java.io.Serializable;

import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.model.Shop;

public class Supplier implements Serializable {

	private static final long serialVersionUID = 1L;

	private String shopCode;

	private String shopName;

	private String shopAddress;

	private float score;

	private int distance;

	private Coordinate coordinate;

	public Supplier(Shop shop) {
		this.shopCode = shop.getCode();
		this.shopAddress = shop.getAddress();
		this.shopName = shop.getName();
		this.coordinate = shop.getCoordinate();
		this.score = shop.getScore();
	}

	public Supplier() {
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

}

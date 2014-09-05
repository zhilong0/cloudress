package com.df.spec.locality.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.springframework.util.Assert;

import com.df.spec.locality.geo.Coordinate;

@Entity(value = "shops", noClassnameStored = true)
@Indexes(@Index(value = "name,address", unique = true))
public class Shop implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private ObjectId shopCode;

	private String regionCode;

	private String address;

	private Coordinate coordinate;

	private String name;

	private String description;

	private String shopOwner;

	private float score;

	private String telephone;

	private String contact;

	private String businessHour;

	private ImageSet imageSet = new ImageSet();

	@Embedded
	private List<Goods> goodsList = new ArrayList<Goods>();

	Shop() {
	}

	public Shop(String name, String address) {
		this.address = address;
		this.name = name;
	}

	public String getCode() {
		if (shopCode != null) {
			return shopCode.toHexString();
		} else {
			return null;
		}
	}

	public void setCode(String shopCode) {
		this.shopCode = new ObjectId(shopCode);
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShopOwner() {
		return shopOwner;
	}

	public void setShopOwner(String shopOwner) {
		this.shopOwner = shopOwner;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public List<Goods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<Goods> goodsList) {
		this.goodsList = goodsList;
	}

	public ImageSet getImageSet() {
		return imageSet;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getContact() {
		return contact;
	}

	public String getBusinessHour() {
		return businessHour;
	}

	public void setBusinessHour(String businessHour) {
		this.businessHour = businessHour;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public boolean addGoods(Goods goods) {
		Assert.notNull(goods.getSpecialityCode());
		for (Goods gd : goodsList) {
			if (gd.getSpecialityCode().equals(goods.getSpecialityCode())) {
				return false;
			}
		}
		return this.goodsList.add(goods);
	}

}

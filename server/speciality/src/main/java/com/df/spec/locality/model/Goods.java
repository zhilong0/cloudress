package com.df.spec.locality.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(value = "goods", noClassnameStored = true)
@Indexes(@Index(value = "shopCode,specialityCode", unique = false, sparse = false))
public class Goods implements Serializable {

	private static final long serialVersionUID = 1L;

	private ObjectId id;

	private String specialityCode;

	@JsonIgnore
	private String shopCode;

	@JsonIgnore
	private boolean isDeleted;

	Goods() {
	}

	public Goods(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	public String getId() {
		if (id != null) {
			return id.toHexString();
		} else {
			return null;
		}
	}

	public String getSpecialityCode() {
		return specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}

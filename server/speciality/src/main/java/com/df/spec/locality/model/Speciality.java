package com.df.spec.locality.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value = "specialities", noClassnameStored = true)
@Indexes(@Index(value = "regionCode,name", unique = true))
public class Speciality implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	ObjectId code;

	private String name;

	private String description;

	private String regionCode;

	private int rank;

	private ImageSet imageSet = new ImageSet();

	public String getCode() {
		if (code != null) {
			return code.toHexString();
		} else {
			return null;
		}
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

	public ImageSet getImageSet() {
		return this.imageSet;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
}

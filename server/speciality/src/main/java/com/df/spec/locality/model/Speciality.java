package com.df.spec.locality.model;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(value = "specialities", noClassnameStored = true)
@Indexes({ @Index(value = "regionCode,name", unique = true), @Index(value = "approvedBy", unique = false, sparse = true) })
public class Speciality extends Approvable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	ObjectId code;

	@NotEmpty(message = "{speciality.name.NotEmpty}")
	private String name;

	@Length(min = 16, message = "{speciality.description.Length}")
	private String description;

	@NotEmpty(message = "{speciality.regionCode.NotEmpty}")
	private String regionCode;

	private int rank;

	@Transient
	private String image;

	private ImageSet imageSet = new ImageSet();

	private int startMonth;

	private int endMonth;

	private Date createdTime;

	private Date changedTime;

	private String createdBy;

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

	@JsonProperty(value = "desc")
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isSeasonal() {
		return startMonth != 0 && endMonth != 0;
	}

	public int getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}

	public int getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(int endMonth) {
		this.endMonth = endMonth;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getChangedTime() {
		return changedTime;
	}

	public void setChangedTime(Date changedTime) {
		this.changedTime = changedTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}

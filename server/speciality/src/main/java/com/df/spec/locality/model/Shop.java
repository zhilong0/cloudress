package com.df.spec.locality.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(value = "shops", noClassnameStored = true)
@Indexes(@Index(value = "name,location.address", unique = true))
public class Shop extends Approvable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private ObjectId code;

	@NotEmpty(message = "{shop.regionCode.NotEmpty}")
	private String regionCode;

	@NotNull(message = "{shop.location.NotNull}")
	private Location location;

	@NotEmpty(message = "{shop.name.NotEmpty}")
	private String name;

	private String description;

	@NotEmpty(message = "{shop.createdBy.NotEmpty}")
	private String createdBy;

	private float score;

	private String telephone;

	private String contact;

	private String businessHour;

	private ImageSet imageSet = new ImageSet();

	private Date createdTime;

	private Date changedTime;

	private boolean isDisabled;

	@Transient
	private String image;

	@JsonIgnore
	private List<String> sellingSpecialities = new ArrayList<String>();

	Shop() {
	}

	public Shop(String name, String address) {
		this.location = new Location(address, null);
		this.name = name;
	}

	public String getCode() {
		if (code != null) {
			return code.toHexString();
		} else {
			return null;
		}
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public void setCode(String shopCode) {
		this.code = new ObjectId(shopCode);
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@JsonProperty(value = "rank")
	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getChangedTime() {
		return changedTime;
	}

	public void setChangedTime(Date changedTime) {
		this.changedTime = changedTime;
	}

	@JsonIgnore
	public String getAddress() {
		if (this.location != null) {
			return this.getLocation().getAddress();
		} else {
			return null;
		}
	}

	public List<String> getSellingSpecialities() {
		return sellingSpecialities;
	}

	public boolean addSpeciality(String specialityCode) {
		return this.sellingSpecialities.add(specialityCode);
	}

	public boolean removeGoods(String specialityCode) {
		return this.sellingSpecialities.remove(specialityCode);
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

}

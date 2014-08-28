package com.df.spec.locality.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "specialities", noClassnameStored = true)
public class Speciality implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String code;

	private String name;

	private String descriptor;

	private String regionCode;

	private ImageSet imageSet = new ImageSet();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public void addImage(String imageId) {
		imageSet.addImage(imageId);
	}

	public void removeImage(String imageId) {
		imageSet.removeImage(imageId);
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

}

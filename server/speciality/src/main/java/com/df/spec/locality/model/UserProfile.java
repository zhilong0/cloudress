package com.df.spec.locality.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserProfile implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String code;

	private String realName;

	private String headThumbnail;

	@JsonIgnore
	private String cellPhone;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getHeadThumbnail() {
		return headThumbnail;
	}

	public void setHeadThumbnail(String headThumbnail) {
		this.headThumbnail = headThumbnail;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
}

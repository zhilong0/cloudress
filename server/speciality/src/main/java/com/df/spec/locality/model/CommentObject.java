package com.df.spec.locality.model;

import java.io.Serializable;

public class CommentObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private String objectId;

	private String objectType;

	public CommentObject() {
	}

	public CommentObject(String objectType, String objectId) {
		this.objectId = objectId;
		this.objectType = objectType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

}

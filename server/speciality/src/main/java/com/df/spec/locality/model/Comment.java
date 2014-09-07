package com.df.spec.locality.model;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Indexes(@Index(value = "commentObject.objectType,commentObject.objectId,-writeDate", unique = false))
public class Comment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private ObjectId id;

	@JsonIgnore
	private CommentObject commentObject;

	private String userId;

	private String userDisplayName;

	private Date writeDate;

	private String comment;

	public String getId() {
		if (this.id != null) {
			return id.toHexString();
		}
		return null;
	}

	public void setId(String id) {
		this.id = new ObjectId(id);
	}

	public CommentObject getCommentObject() {
		return commentObject;
	}

	public void setCommentObject(CommentObject commentObject) {
		this.commentObject = commentObject;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public Date getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}

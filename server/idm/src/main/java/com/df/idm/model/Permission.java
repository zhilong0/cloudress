package com.df.idm.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(value = "permissions", noClassnameStored = true)
@Indexes(@Index(value = "domain,name", unique = true, sparse = false))
public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@JsonIgnore
	ObjectId id;

	@NotEmpty(message = "{permission.domain.NotEmpty}")
	private String domain;

	@NotEmpty(message = "{permission.name.NotEmpty}")
	private String name;

	private String description;

	Permission() {
	}

	public Permission(String domain, String name) {
		this.domain = domain;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDomain() {
		return domain;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Permission) {
			Permission other = (Permission) obj;
			return this.getName().equals(other.getName()) && this.getDomain().equals(other.getDomain());
		}

		return false;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public String toString() {
		return "[name=" + this.getName() + ", domain=" + this.getDomain() + "]";
	}
}

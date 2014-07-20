package com.df.idm.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

@Entity
public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	ObjectId id;

	@Indexed
	private String domain;

	@Indexed
	private String name;

	private String description;

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

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Permission) {
			Permission other = (Permission) obj;
			return this.getName().equals(other.getName());
		}

		return false;
	}

	public int hashCode() {
		return this.getName().hashCode();
	}

	public String toString() {
		return "Permission [name=" + this.getName() + "]";
	}
}

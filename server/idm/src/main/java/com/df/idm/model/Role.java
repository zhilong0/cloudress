package com.df.idm.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(value = "roles", noClassnameStored = true)
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	ObjectId id;

	@Indexed(name = "role_name_idx", unique = true)
	private String name;

	private String description;

	@Indexed
	@Reference(value = "permissions", ignoreMissing = true, idOnly = true)
	private List<Permission> permissions = new ArrayList<Permission>();

	Role() {
	}

	public Role(String name) {
		this.name = name;
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

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public boolean removePermission(Permission permission) {
		return this.permissions.remove(permission);
	}

	@Override
	@JsonIgnore
	public String getAuthority() {
		return this.getName();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Role) {
			Role other = (Role) obj;
			return this.getName().equals(other.getName());
		}

		return false;
	}

	public int hashCode() {
		return (this.getName()).hashCode();
	}

	public String toString() {
		return this.getName();
	}
}

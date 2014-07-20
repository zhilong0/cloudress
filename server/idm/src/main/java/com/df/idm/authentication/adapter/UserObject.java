package com.df.idm.authentication.adapter;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.df.idm.model.ExternalUserReference;
import com.df.idm.model.Role;
import com.df.idm.model.User;

public class UserObject implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String userId;

	private String code;

	private String email;

	private String cellphone;

	private ExternalUserReference externalUserReference;

	private boolean isLocked;

	private String password;

	private List<Role> roles;

	public UserObject(User user) {
		this.code = user.getCode();
		this.email = user.getEmail();
		this.cellphone = user.getCellphone();
		this.externalUserReference = user.getExternalUserReference();
		this.isLocked = user.isLocked();
		this.password = user.getPassword();
		this.roles = user.getRoles();
		this.userId = user.getId();
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	public String getPassword() {
		return this.password;
	}

	public String getEmail() {
		return this.email;
	}

	public String getCellphone() {
		return this.cellphone;
	}

	public String getCode() {
		return code;
	}

	public ExternalUserReference getExternalUserReference() {
		return externalUserReference;
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return !this.isLocked;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	public String getUsername() {
		return this.getCode();
	}

	public String getUserId() {
		return this.userId;
	}

	public UserObject eraseCredential() {
		this.password = null;
		return this;
	}
}

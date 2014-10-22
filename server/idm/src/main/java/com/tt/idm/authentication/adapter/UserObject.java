package com.tt.idm.authentication.adapter;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tt.idm.model.ExternalUserReference;
import com.tt.idm.model.User;

public class UserObject implements UserDetails {

	private static final long serialVersionUID = 1L;

	private User user;

	public UserObject(User user) {
		this.user = user;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles();
	}

	public String getPassword() {
		return user.getPassword();
	}

	public String getEmail() {
		return user.getEmail();
	}

	public String getCellphone() {
		return user.getCellphone();
	}

	public String getCode() {
		return user.getCode();
	}

	public ExternalUserReference getExternalUserReference() {
		return user.getExternalUserReference();
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return !user.isLocked();
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return !user.isDisabled();
	}

	public String getUsername() {
		return user.getCode();
	}

	public String getUserId() {
		return user.getId();
	}

	public UserObject eraseCredential() {
		user.cleanPassword();
		return this;
	}

	public String getFirstName() {
		return user.getFirstName();
	}

	public String getLastName() {
		return user.getLastName();
	}
}

package com.df.idm.authentication;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.df.common.utils.StringUtils;

public class UserPropertyAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private Object principal;

	private Object credentials;

	public UserPropertyAuthenticationToken(Object principal, Object credentials) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
		this.setAuthenticated(false);
	}

	public UserPropertyAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		credentials = null;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public boolean isEmail() {
		return StringUtils.isValidEmail(principal.toString());
	}

}

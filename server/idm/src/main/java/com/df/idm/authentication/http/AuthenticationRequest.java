package com.df.idm.authentication.http;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private String account;

	private String password;

	AuthenticationRequest() {
	}

	public AuthenticationRequest(String account, String password) {
		this.account = account;
		this.password = password;
	}

	public String getAccount() {
		return account;
	}

	public String getPassword() {
		return password;
	}

}

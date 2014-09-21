package com.df.idm.authentication.http;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private String code;

	private String password;

	AuthenticationRequest() {
	}

	public AuthenticationRequest(String code, String password) {
		this.code = code;
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public String getPassword() {
		return password;
	}

}

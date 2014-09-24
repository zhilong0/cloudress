package com.df.idm.authentication.http;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private String code;

	private String nickName;

	private String password;

	AuthenticationRequest() {
	}

	public AuthenticationRequest(String code, String password, String nickName) {
		this.code = code;
		this.password = password;
		this.nickName = nickName;
	}

	public String getCode() {
		return code;
	}

	public String getPassword() {
		return password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}

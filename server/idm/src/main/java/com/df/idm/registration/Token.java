package com.df.idm.registration;

import java.io.Serializable;
import java.util.Date;

public class Token implements Serializable {

	private static final long serialVersionUID = 1L;

	private String token;

	private String stick;

	private long whenExpired;

	Token() {
	}

	public Token(String token, long whenExpired) {
		this.token = token;
		this.whenExpired = whenExpired;
	}

	public Token(String token, long whenExpired, String stick) {
		this(token, whenExpired);
		this.stick = stick;
	}

	public String getToken() {
		return token;
	}

	public boolean isExpired() {
		return new Date().getTime() > whenExpired;
	}

	public long getWhenExpired() {
		return whenExpired;
	}

	public String getStick() {
		return stick;
	}

	public void setStick(String stick) {
		this.stick = stick;
	}
}

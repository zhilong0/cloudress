package com.tt.idm.rs.resources;

import java.io.Serializable;

public class PasswordChange implements Serializable {

	private static final long serialVersionUID = 1L;

	private String oldPassword;

	private String newPassword;

	public PasswordChange() {
	}

	public PasswordChange(String oldPassword, String newPassword) {
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	public PasswordChange(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

}

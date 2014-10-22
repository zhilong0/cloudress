package com.tt.idm.rs.resources;

public class CellphoneVerifyParams {

	private String cellphone;

	private String token;

	CellphoneVerifyParams() {
	}

	public CellphoneVerifyParams(String cellphone, String token) {
		this.cellphone = cellphone;
		this.token = token;
	}

	public String getCellphone() {
		return cellphone;
	}

	public String getToken() {
		return token;
	}

}

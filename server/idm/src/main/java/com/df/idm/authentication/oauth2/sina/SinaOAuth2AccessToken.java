package com.df.idm.authentication.oauth2.sina;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class SinaOAuth2AccessToken extends DefaultOAuth2AccessToken {

	private static final long serialVersionUID = 1L;
	
	private String uid;

	public SinaOAuth2AccessToken(OAuth2AccessToken accessToken) {
		super(accessToken);
	}

	public SinaOAuth2AccessToken(String value) {
		super(value);
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}

package com.df.idm.authentication.oauth2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.df.idm.model.ExternalUserReference;
import com.df.idm.model.ExternalUserReference.Provider;

public class ExternalUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String accessToken;

	private Provider provider;

	private Map<String, String> attibutes = new HashMap<String, String>();

	public ExternalUser(ExternalUserReference externalUserReference) {
		this.id = externalUserReference.getExternalId();
		this.provider = externalUserReference.getProvider();
	}

	public ExternalUser(String id, Provider provider) {
		this.id = id;
		this.provider = provider;
	}

	public String getId() {
		return id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public Provider getProvider() {
		return provider;
	}

	public Map<String, String> getAttibutes() {
		return attibutes;
	}

	public void setAttribute(String attributeName, String attributeValue) {
		this.attibutes.put(attributeName, attributeValue);
	}

	public String getAttribute(String attributeName) {
		return this.attibutes.get(attributeName);
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}

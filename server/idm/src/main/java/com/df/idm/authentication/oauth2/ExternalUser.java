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

	private Map<String, Object> attibutes = new HashMap<String, Object>();

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

	public Map<String, Object> getAttibutes() {
		return attibutes;
	}

	public void setAttribute(String attributeName, String attributeValue) {
		this.attibutes.put(attributeName, attributeValue);
	}

	public Object getAttribute(String attributeName) {
		return this.attibutes.get(attributeName);
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String toString() {
		return "ExternalUser [id=" + id + ", provider=" + provider + "]";
	}

}

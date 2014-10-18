package com.df.idm.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Embedded
@Indexes(@Index(value = "externalId,provider", unique = true, sparse = true))
public class ExternalUserReference implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum Provider {
		SINA, QQ
	}

	private String externalId;

	private Provider provider;

	ExternalUserReference() {
	}

	public ExternalUserReference(Provider provider, String externalId) {
		this.externalId = externalId;
		this.provider = provider;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	@Override
	public String toString() {
		return "[externalId=" + externalId + ", provider=" + provider + "]";
	}

}

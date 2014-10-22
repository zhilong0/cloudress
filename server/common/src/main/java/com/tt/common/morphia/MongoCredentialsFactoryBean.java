package com.tt.common.morphia;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.MongoCredential;

public class MongoCredentialsFactoryBean extends AbstractFactoryBean<MongoCredential[]> {

	@Override
	public Class<?> getObjectType() {
		return MongoCredential[].class;
	}

	@Override
	protected MongoCredential[] createInstance() throws Exception {
		return null;
	}

}

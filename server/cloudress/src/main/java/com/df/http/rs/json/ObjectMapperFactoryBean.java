package com.df.http.rs.json;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class ObjectMapperFactoryBean implements FactoryBean<ObjectMapper>, InitializingBean {

	private ObjectMapper objectMapper;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.setDateFormat(SimpleDateSerializer.getDateFormat());
		this.objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@Override
	public ObjectMapper getObject() throws Exception {
		return objectMapper;
	}

	@Override
	public Class<?> getObjectType() {
		return ObjectMapper.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}

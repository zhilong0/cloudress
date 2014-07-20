package com.df.http.rs.provider;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.df.http.rs.json.SimpleDateSerializer;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {
	private ObjectMapper objectMapper;

	public JacksonContextResolver() throws Exception {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.setDateFormat(SimpleDateSerializer.getDateFormat());
		this.objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public ObjectMapper getContext(Class<?> objectType) {
		return objectMapper;
	}
}
package com.df.idm.authentication.oauth2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public abstract class Jackson2OAuth2AccessTokenMessageConverter extends AbstractHttpMessageConverter<OAuth2AccessToken> {

	private ObjectMapper objectMapper;

	public Jackson2OAuth2AccessTokenMessageConverter() {
		super(MediaType.APPLICATION_JSON);
		this.objectMapper = new ObjectMapper();
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	protected boolean supports(Class<?> type) {
		return (OAuth2AccessToken.class.isAssignableFrom(type));
	}

	@Override
	protected OAuth2AccessToken readInternal(Class<? extends OAuth2AccessToken> paramClass, HttpInputMessage message)
	        throws IOException, HttpMessageNotReadableException {
		HashMap<String, String> response = objectMapper.readValue(message.getBody(),
		        new TypeReference<HashMap<String, String>>() {
		        });
		return readFromMap(response);
	}

	protected abstract OAuth2AccessToken readFromMap(Map<String, String> response);

	@Override
	protected void writeInternal(OAuth2AccessToken token, HttpOutputMessage message) throws IOException,
	        HttpMessageNotWritableException {
		throw new UnsupportedOperationException(
		        "This converter is only used for converting from externally aqcuired form data");
	}

}

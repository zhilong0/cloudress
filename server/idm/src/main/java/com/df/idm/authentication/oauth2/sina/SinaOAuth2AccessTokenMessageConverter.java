package com.df.idm.authentication.oauth2.sina;

import java.util.Arrays;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.df.idm.authentication.oauth2.Jackson2OAuth2AccessTokenMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SinaOAuth2AccessTokenMessageConverter extends Jackson2OAuth2AccessTokenMessageConverter {

	public SinaOAuth2AccessTokenMessageConverter(ObjectMapper objectMapper) {
		super(objectMapper);
		this.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
	}

	public SinaOAuth2AccessTokenMessageConverter() {
		this.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
	}

	@Override
	protected OAuth2AccessToken readFromMap(Map<String, String> response) {
		OAuth2AccessToken token = DefaultOAuth2AccessToken.valueOf(response);
		SinaOAuth2AccessToken sinaToken = new SinaOAuth2AccessToken(token);
		sinaToken.setUid(response.get("uid"));
		return sinaToken;
	}

}

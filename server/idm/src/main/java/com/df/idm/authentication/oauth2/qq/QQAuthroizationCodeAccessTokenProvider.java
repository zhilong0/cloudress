package com.df.idm.authentication.oauth2.qq;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseExtractor;

public class QQAuthroizationCodeAccessTokenProvider extends AuthorizationCodeAccessTokenProvider {

	@Override
	protected ResponseExtractor<OAuth2AccessToken> getResponseExtractor() {
		return new ResponseExtractor<OAuth2AccessToken>() {

			@Override
			public OAuth2AccessToken extractData(ClientHttpResponse response) throws IOException {
				FormHttpMessageConverter converter = new FormHttpMessageConverter();
				MultiValueMap<String, String> map = converter.read(null, response);
				return DefaultOAuth2AccessToken.valueOf(map.toSingleValueMap());
			}

		};
	}

}

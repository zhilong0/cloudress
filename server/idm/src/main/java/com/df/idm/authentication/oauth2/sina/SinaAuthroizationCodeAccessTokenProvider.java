package com.df.idm.authentication.oauth2.sina;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.client.ResponseExtractor;

public class SinaAuthroizationCodeAccessTokenProvider extends AuthorizationCodeAccessTokenProvider {

	@Override
	public String obtainAuthorizationCode(OAuth2ProtectedResourceDetails details, AccessTokenRequest request) throws UserRedirectRequiredException,
			UserApprovalRequiredException, AccessDeniedException, OAuth2AccessDeniedException {
		String methodName = "getRedirectForAuthorization";
		Class<?> p1 = AuthorizationCodeResourceDetails.class;
		Class<?> p2 = AccessTokenRequest.class;
		Method method = null;
		try {
			method = AuthorizationCodeAccessTokenProvider.class.getDeclaredMethod(methodName, p1, p2);
			method.setAccessible(true);
			throw (UserRedirectRequiredException) (method.invoke(this, (AuthorizationCodeResourceDetails) details, request));
		} catch (UserRedirectRequiredException ex) {
			throw ex;
		} catch (Throwable ex) {
			throw new OAuth2Exception("server error", ex);
		}
	}

	@Override
	protected ResponseExtractor<OAuth2AccessToken> getResponseExtractor() {
		return new ResponseExtractor<OAuth2AccessToken>() {

			@Override
			public OAuth2AccessToken extractData(ClientHttpResponse response) throws IOException {
				MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) converter.read(HashMap.class, response);
				OAuth2AccessToken token = DefaultOAuth2AccessToken.valueOf(map);
				SinaOAuth2AccessToken sinaToken = new SinaOAuth2AccessToken(token);
				sinaToken.setUid(map.get("uid"));
				return sinaToken;
			}

		};
	}
}

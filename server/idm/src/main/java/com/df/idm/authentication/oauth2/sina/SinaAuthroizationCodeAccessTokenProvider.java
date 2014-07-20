package com.df.idm.authentication.oauth2.sina;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class SinaAuthroizationCodeAccessTokenProvider extends AuthorizationCodeAccessTokenProvider {

	@Override
	public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
		super.setMessageConverters(messageConverters);
		messageConverters.add(new SinaOAuth2AccessTokenMessageConverter());
	}

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

}

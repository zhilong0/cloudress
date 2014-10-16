package com.df.idm.authentication.oauth2.sina;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.OAuth2AccessTokenSupport;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

import com.df.idm.authentication.oauth2.ExternalUser;
import com.df.idm.authentication.oauth2.OAuth2ProtectedResourceInterface;
import com.df.idm.model.ExternalUserReference;
import com.df.idm.model.ExternalUserReference.Provider;

public class SinaOAuth2ResourceInterface extends OAuth2RestTemplate implements OAuth2ProtectedResourceInterface {

	private String userDetailsUri;

	private static final Logger logger = LoggerFactory.getLogger(SinaOAuth2ResourceInterface.class);

	public SinaOAuth2ResourceInterface(OAuth2ProtectedResourceDetails resource) {
		this(resource, new DefaultOAuth2ClientContext());
	}

	public SinaOAuth2ResourceInterface(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
		super(resource, context);
		this.setAccessTokenProvider(new SinaAuthroizationCodeAccessTokenProvider());
	}

	public SinaOAuth2ResourceInterface(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context, ClientHttpRequestFactory requestFactory) {
		super(resource, context);
		this.setRequestFactory(requestFactory);
		this.setAccessTokenProvider(new SinaAuthroizationCodeAccessTokenProvider());
	}

	public void setUserDetailsUri(String userDetailsUri) {
		this.userDetailsUri = userDetailsUri;
	}

	@Override
	public void setAccessTokenProvider(AccessTokenProvider accessTokenProvider) {
		super.setAccessTokenProvider(accessTokenProvider);
		if (accessTokenProvider instanceof OAuth2AccessTokenSupport) {
			((OAuth2AccessTokenSupport) accessTokenProvider).setRequestFactory(this.getRequestFactory());
		}
	}

	@Override
	public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
		super.setRequestFactory(requestFactory);
	}

	@Override
	public ExternalUser getUserDetails() {
		URI uri = null;
		try {
			uri = new URI(this.userDetailsUri);
		} catch (URISyntaxException ex) {
			throw new RestClientException("Invalid uri syntax", ex);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		OAuth2AccessToken accessToken = this.getAccessToken();
		uri = this.appendQueryParameter(uri, accessToken);
		String uid = null;
		if (accessToken instanceof SinaOAuth2AccessToken) {
			uid = ((SinaOAuth2AccessToken) accessToken).getUid();
		} else {
			uid = (String) accessToken.getAdditionalInformation().get("uid");
		}
		if (uid != null) {
			logger.debug("begin to get sina user {} details", uid);
		}
		parameters.put("uid", uid);
		uri = this.appendQueryParameter(uri, parameters);

		return this.execute(uri, HttpMethod.GET, null, new ResponseExtractor<ExternalUser>() {

			@Override
			public ExternalUser extractData(ClientHttpResponse response) throws IOException {
				if (response.getStatusCode() == HttpStatus.OK) {
					MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
					@SuppressWarnings("unchecked")
					Map<String, ?> map = (Map<String, ?>) converter.read(Map.class, response);
					ExternalUserReference ref = new ExternalUserReference(Provider.SINA, map.get("id").toString());
					ExternalUser user = new ExternalUser(ref);
					user.setAttribute("name", (String) map.get("name"));
					user.setAttribute("screenName", (String) map.get("screenName"));
					user.setAttribute("profile_image_url", (String) map.get("profile_image_url"));
					user.setAttribute("gender", (String) map.get("gender"));
					return user;
				} else {
					logger.error("error status code {} when getting user details", response.getStatusCode());
					return null;
				}
			}
		});
	}

	@Override
	public byte[] getUserProfileImage(ExternalUser user) {
		if (user == null || user.getAttribute("profile_image_url") == null) {
			logger.debug("user profile image is empty for user {}, ignore", user);
			return null;
		}
		final String url = (String) user.getAttribute("profile_image_url");
		return this.execute(url, HttpMethod.GET, null, new ResponseExtractor<byte[]>() {

			@Override
			public byte[] extractData(ClientHttpResponse response) throws IOException {
				if (response.getStatusCode() == HttpStatus.OK) {
					ByteArrayHttpMessageConverter converter = new ByteArrayHttpMessageConverter();
					return converter.readInternal(byte[].class, response);
				} else {
					logger.debug("user profile image cannot be loaded from {}", url);
					return null;
				}
			}
		});
	}

	protected URI appendQueryParameter(URI uri, Map<String, String> parameters) {
		try {
			String query = uri.getRawQuery();
			if (parameters == null || parameters.size() == 0) {
				return uri;
			}
			String queryFragment = null;
			for (String key : parameters.keySet()) {
				if (queryFragment == null) {
					queryFragment = key + "=" + URLEncoder.encode(parameters.get(key), "UTF-8");
				} else {
					queryFragment = "&" + key + "=" + URLEncoder.encode(parameters.get(key), "UTF-8");
				}
			}
			if (query == null) {
				query = queryFragment;
			} else {
				query = query + "&" + queryFragment;
			}

			URI update = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), null, null);

			StringBuffer sb = new StringBuffer(update.toString());
			sb.append("?");
			sb.append(query);
			if (uri.getFragment() != null) {
				sb.append("#");
				sb.append(uri.getFragment());
			}

			return new URI(sb.toString());
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Could not parse URI", e);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Could not encode URI", e);
		}
	}

}

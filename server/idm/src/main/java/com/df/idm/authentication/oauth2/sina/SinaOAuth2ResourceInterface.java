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
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

import com.df.idm.authentication.oauth2.ExternalUser;
import com.df.idm.authentication.oauth2.OAuth2ProtectedResourceInterface;

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

	public void setUserDetailsUri(String userDetailsUri) {
		this.userDetailsUri = userDetailsUri;
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
		String uid = (String) accessToken.getAdditionalInformation().get("uid");
		if (uid != null) {
			logger.debug("begin to get sina user {} details", uid);
		}
		parameters.put("uid", uid);
		uri = this.appendQueryParameter(uri, parameters);
		uri = this.appendQueryParameter(uri, accessToken);
		return this.doExecute(uri, HttpMethod.GET, null, new ResponseExtractor<ExternalUser>() {

			@Override
			public ExternalUser extractData(ClientHttpResponse paramClientHttpResponse) throws IOException {
				return null;
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

			URI update = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), null,
			        null);

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

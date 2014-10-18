package com.df.idm.authentication.oauth2.qq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.ResponseExtractor;

import com.df.idm.authentication.oauth2.ExternalUser;
import com.df.idm.authentication.oauth2.OAuth2ProtectedResourceInterface;
import com.df.idm.model.ExternalUserReference;
import com.df.idm.model.ExternalUserReference.Provider;

public class QQOAuth2ResourceInterface extends OAuth2RestTemplate implements OAuth2ProtectedResourceInterface {

	private String userDetailsUri;

	private String openIdUri;

	private static final Charset UTF8 = Charset.forName("utf-8");

	private static final Logger logger = LoggerFactory.getLogger(QQOAuth2ResourceInterface.class);

	public QQOAuth2ResourceInterface(OAuth2ProtectedResourceDetails resource) {
		this(resource, new DefaultOAuth2ClientContext());
	}

	public QQOAuth2ResourceInterface(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
		super(resource, context);
		this.setAccessTokenProvider(new QQAuthroizationCodeAccessTokenProvider());
	}

	public QQOAuth2ResourceInterface(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context, ClientHttpRequestFactory requestFactory) {
		super(resource, context);
		this.setRequestFactory(requestFactory);
		this.setAccessTokenProvider(new QQAuthroizationCodeAccessTokenProvider());
	}

	public void setUserDetailsUri(String userDetailsUri) {
		this.userDetailsUri = userDetailsUri;
	}

	public void setOpenIdUri(String openIdUri) {
		this.openIdUri = openIdUri;
	}

	@Override
	public ExternalUser getUserDetails() {
		String url = userDetailsUri + "?access_token={0}&oauth_consumer_key={1}&&openid={2}";
		OAuth2AccessToken accessToken = this.getAccessToken();
		final String openId = this.getOpenId();
		return this.execute(url, HttpMethod.GET, null, new ResponseExtractor<ExternalUser>() {

			@Override
			public ExternalUser extractData(ClientHttpResponse response) throws IOException {
				if (response.getStatusCode() == HttpStatus.OK) {
					MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
					@SuppressWarnings("unchecked")
					Map<String, ?> map = (Map<String, ?>) converter.read(Map.class, response);
					ExternalUserReference ref = new ExternalUserReference(Provider.QQ, openId);
					ExternalUser user = new ExternalUser(ref);
					user.setAttribute("nickname", (String) map.get("nickname"));
					user.setAttribute("gender", (String) map.get("gender"));
					return user;
				} else {

					logger.error("error status code {} when getting user details", response.getStatusCode());
					return null;
				}
			}
		}, accessToken.getValue(), this.getResource().getClientId(), openId);
	}

	@Override
	public byte[] getUserProfileImage(ExternalUser user) {
		return null;
	}

	public String getOpenId() {
		String url = openIdUri + "?access_token={0}";
		return this.execute(url, HttpMethod.GET, null, new ResponseExtractor<String>() {

			@Override
			public String extractData(ClientHttpResponse response) throws IOException {
				if (response.getStatusCode() == HttpStatus.OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					InputStream in = response.getBody();
					byte[] buf = new byte[1024];
					while (true) {
						int r = in.read(buf);
						if (r == -1) {
							break;
						}
						out.write(buf, 0, r);
					}

					String resp = new String(out.toByteArray(), UTF8);
					String[] keyValuePairs = resp.substring(resp.indexOf("{") + 1, resp.indexOf("}")).split(",");
					for (String keyValuePair : keyValuePairs) {
						String[] pair = keyValuePair.split(":");
						if (pair.length == 2) {
							if ("\"openid\"".equals(pair[0].trim())) {
								return pair[1].trim().replace("\"", "");
							}
						}
					}
					String msg = "cannot get QQ openId, bad response";
					throw new QQResourceException(msg, resp);
				} else {
					throw new QQResourceException("Cannot get QQ openId");
				}
			}
		}, this.getAccessToken().getValue());
	}
}

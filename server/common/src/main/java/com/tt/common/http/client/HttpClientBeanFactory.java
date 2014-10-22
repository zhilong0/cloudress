package com.tt.common.http.client;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.util.Assert;

public class HttpClientBeanFactory extends AbstractFactoryBean<HttpClient> {

	private HttpClientConnectionManager httpClientConnectionManager;

	private HttpHost proxy;

	private RequestConfig requestConfig;

	@Override
	public Class<?> getObjectType() {
		return HttpClient.class;
	}

	@Override
	protected HttpClient createInstance() throws Exception {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setConnectionManager(httpClientConnectionManager);
		if (requestConfig != null) {
			httpClientBuilder.setDefaultRequestConfig(requestConfig);
		}
		if (proxy != null) {
			httpClientBuilder.setProxy(proxy);
		}
		return httpClientBuilder.build();
	}

	public void setHttpClientConnectionManager(HttpClientConnectionManager httpClientConnectionManager) {
		this.httpClientConnectionManager = httpClientConnectionManager;
	}

	public void setProxy(HttpHost proxy) {
		this.proxy = proxy;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		Assert.notNull(httpClientConnectionManager);
	}

}

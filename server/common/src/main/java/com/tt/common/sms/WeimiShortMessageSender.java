package com.tt.common.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WeimiShortMessageSender implements ShortMessageSender {

	private HttpClient httpClient;

	private ObjectMapper objectMapper;

	private static final String SEND_URL = "http://api.weimi.cc/2/sms/send.html";

	private String weimiUid;

	private String weimiPassword;

	private static final Logger logger = LoggerFactory.getLogger(WeimiShortMessageSender.class);

	public WeimiShortMessageSender(HttpClient httpClient, ObjectMapper objectMapper) {
		this.httpClient = httpClient;
		this.objectMapper = objectMapper;
	}

	@Override
	public void send(Object templateId, String[] receivers, Map<String, Object> variables) {
		HttpPost httpPost = new HttpPost(SEND_URL);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("uid", weimiUid));
		nvps.add(new BasicNameValuePair("pas", weimiPassword));
		String destination = StringUtils.arrayToCommaDelimitedString(receivers);
		nvps.add(new BasicNameValuePair("mob", destination));
		nvps.add(new BasicNameValuePair("type", "json"));
		nvps.add(new BasicNameValuePair("cid", templateId.toString()));
		int i = 1;
		for (String varaibleName : variables.keySet()) {
			nvps.add(new BasicNameValuePair("p" + i, variables.get(varaibleName).toString()));
			i++;
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				logger.debug("send message to {} succeully", destination);
				HttpEntity entity = response.getEntity();
				SendResponse resp = objectMapper.readValue(entity.getContent(), SendResponse.class);
				if (resp.code == 0) {
					logger.info("send short message to {} successfully", destination);
				} else {
					logger.error("response with code={}, msg={}", resp.code, resp.msg);
				}
			}
		} catch (ClientProtocolException ex) {
			logger.error("failed to send short message to " + destination, ex);
		} catch (IOException ex) {
			logger.error("failed to send short message to " + destination, ex);
		}
	}

	public void setWeimiUid(String weimiUid) {
		this.weimiUid = weimiUid;
	}

	public void setWeimiPassword(String weimiPassword) {
		this.weimiPassword = weimiPassword;
	}

	static class SendResponse {

		public int code;

		public String msg;
	}

}

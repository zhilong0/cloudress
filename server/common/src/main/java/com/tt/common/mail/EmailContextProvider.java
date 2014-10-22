package com.tt.common.mail;

import java.util.Map;

public interface EmailContextProvider {

	public String getHyperLink(boolean secure, String path, Map<String, String> getParameters);

	public String getSentFrom();
}

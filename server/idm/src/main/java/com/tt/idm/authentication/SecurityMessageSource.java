package com.tt.idm.authentication;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.core.SpringSecurityMessageSource;

public class SecurityMessageSource extends ResourceBundleMessageSource {

	private static final String SECURITY_DEFAULT_BASE_NAME = "messages.security_messages";

	public SecurityMessageSource() {
		this.setBasename(SECURITY_DEFAULT_BASE_NAME);
		this.setDefaultEncoding("utf-8");
		this.setParentMessageSource(new SpringSecurityMessageSource());
	}

	public static MessageSourceAccessor getAccessor() {
		return new MessageSourceAccessor(new SecurityMessageSource());
	}
}

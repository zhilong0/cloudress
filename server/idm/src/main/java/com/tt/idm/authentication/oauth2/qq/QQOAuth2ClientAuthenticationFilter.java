package com.tt.idm.authentication.oauth2.qq;

import com.tt.idm.authentication.oauth2.DefaultOAuth2ClientAuthenticationFilter;
import com.tt.idm.authentication.oauth2.ExternalUser;
import com.tt.idm.model.User;
import com.tt.idm.model.User.Gender;

public class QQOAuth2ClientAuthenticationFilter extends DefaultOAuth2ClientAuthenticationFilter {

	public QQOAuth2ClientAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	protected void mappingUserDetails(ExternalUser externalUser, User mappingUser) {
		mappingUser.setNickName((String) externalUser.getAttribute("nickName"));
		if ("男".equals(externalUser.getAttribute("gender"))) {
			mappingUser.setGender(Gender.MALE);
		} else if ("女".equals(externalUser.getAttribute("gender"))) {
			mappingUser.setGender(Gender.FEMALE);
		}
	}
}

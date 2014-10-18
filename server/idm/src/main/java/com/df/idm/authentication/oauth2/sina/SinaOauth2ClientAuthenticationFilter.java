package com.df.idm.authentication.oauth2.sina;

import com.df.idm.authentication.oauth2.DefaultOAuth2ClientAuthenticationFilter;
import com.df.idm.authentication.oauth2.ExternalUser;
import com.df.idm.model.User;
import com.df.idm.model.User.Gender;

public class SinaOauth2ClientAuthenticationFilter extends DefaultOAuth2ClientAuthenticationFilter {

	public SinaOauth2ClientAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	protected void mappingUserDetails(ExternalUser externalUser, User mappingUser) {
		mappingUser.setNickName((String) externalUser.getAttribute("name"));
		if ("m".equals(externalUser.getAttribute("gender"))) {
			mappingUser.setGender(Gender.MALE);
		} else if ("f".equals(externalUser.getAttribute("gender"))) {
			mappingUser.setGender(Gender.FEMALE);
		}
	}

}

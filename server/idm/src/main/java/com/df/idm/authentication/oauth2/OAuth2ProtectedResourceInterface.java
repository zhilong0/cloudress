package com.df.idm.authentication.oauth2;

import org.springframework.security.oauth2.client.OAuth2RestOperations;

public interface OAuth2ProtectedResourceInterface extends OAuth2RestOperations {

	public ExternalUser getUserDetails();
}

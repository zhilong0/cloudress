package com.df.idm.authentication;

import java.util.Collection;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class SinaOAuth2AccessTokenStore implements TokenStore {

	@Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String paramString) {
	    // TODO Auto-generated method stub
	    return null;
    }

    public Collection<OAuth2AccessToken> findTokensByUserName(String paramString) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication paramOAuth2Authentication) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public OAuth2AccessToken readAccessToken(String paramString) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken paramOAuth2AccessToken) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public OAuth2Authentication readAuthentication(String paramString) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken paramOAuth2RefreshToken) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public OAuth2RefreshToken readRefreshToken(String paramString) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public void removeAccessToken(OAuth2AccessToken paramOAuth2AccessToken) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken paramOAuth2RefreshToken) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void removeRefreshToken(OAuth2RefreshToken paramOAuth2RefreshToken) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void storeAccessToken(OAuth2AccessToken paramOAuth2AccessToken,
            OAuth2Authentication paramOAuth2Authentication) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void storeRefreshToken(OAuth2RefreshToken paramOAuth2RefreshToken,
            OAuth2Authentication paramOAuth2Authentication) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
		// TODO Auto-generated method stub
		return null;
	}

}

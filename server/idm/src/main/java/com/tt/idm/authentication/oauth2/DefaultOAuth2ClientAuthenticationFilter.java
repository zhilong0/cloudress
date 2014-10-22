package com.tt.idm.authentication.oauth2;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.tt.idm.authentication.UserPropertyAuthenticationToken;
import com.tt.idm.authentication.adapter.UserObject;
import com.tt.idm.model.ExternalUserReference;
import com.tt.idm.model.User;
import com.tt.idm.service.contract.UserManagementService;

public class DefaultOAuth2ClientAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private UserManagementService userManagementService;

	private OAuth2ProtectedResourceInterface resourceInterface;

	private String codeParameter = "code";

	private String stateParameter = "state";

	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	public DefaultOAuth2ClientAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
		setAuthenticationManager(new OAuth2AuthenticationManager());
	}

	public void setUserManagementService(UserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}

	public void setResourceInterface(OAuth2ProtectedResourceInterface resourceInterface) {
		this.resourceInterface = resourceInterface;
	}

	public void afterPropertiesSet() {
		Assert.state(this.userManagementService != null, "Supply a user management service");
		Assert.state(this.resourceInterface != null, "Supply a resource interface");
		super.afterPropertiesSet();
	}

	protected void getAuthorizationCodeFromRequest(HttpServletRequest request) {
		String code = request.getParameter(codeParameter);
		String state = request.getParameter(stateParameter);
		if (StringUtils.hasText(state)) {
			resourceInterface.getOAuth2ClientContext().getAccessTokenRequest().setStateKey(state);
		}
		if (StringUtils.hasText(code)) {
			resourceInterface.getOAuth2ClientContext().getAccessTokenRequest().setAuthorizationCode(code);
		}
	}

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException,
			ServletException {
		try {
			this.getAuthorizationCodeFromRequest(request);
			OAuth2AccessToken accessToken = this.resourceInterface.getAccessToken();
			ExternalUser external = resourceInterface.getUserDetails();
			external.setAccessToken(accessToken.getValue());
			ExternalUserReference reference = new ExternalUserReference(external.getProvider(), external.getId());
			User mappingUser = userManagementService.getUserByExternalId(reference.getExternalId(), reference.getProvider());
			if (mappingUser == null) {
				mappingUser = userManagementService.createUserByExternalUser(reference);
				mappingUserDetails(external, mappingUser); 
			}
			mappingUser.setLastLogin(new Date());
			userManagementService.updateUser(mappingUser);
			UserObject uo = new UserObject(mappingUser);
			Collection<? extends GrantedAuthority> authorities = authoritiesMapper.mapAuthorities(uo.getAuthorities());
			UserPropertyAuthenticationToken result = new UserPropertyAuthenticationToken(uo, authorities);
			result.setDetails(uo);
			return result;
		} catch (InvalidTokenException e) {
			throw new BadCredentialsException("Could not obtain user details from token", e);
		}
	}

	protected void mappingUserDetails(ExternalUser externalUser, User mappingUser) {
	}

	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException,
			ServletException {
		if (failed instanceof AccessTokenRequiredException) {
			throw failed;
		}
		super.unsuccessfulAuthentication(request, response, failed);
	}
}

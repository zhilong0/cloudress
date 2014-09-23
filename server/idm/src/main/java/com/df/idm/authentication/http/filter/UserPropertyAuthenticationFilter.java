package com.df.idm.authentication.http.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.df.idm.authentication.SecurityMessageSource;
import com.df.idm.authentication.UserPropertyAuthenticationToken;
import com.df.idm.model.User;
import com.df.idm.service.contract.UserManagementService;

public class UserPropertyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String DEFAULT_FILTER_PROCESS_URL = "/auth/login";

	public static final String SECURITY_AUTH_ACCOUNT_KEY = "code";

	public static final String SECURITY_AUTH_PASSWORD_KEY = "password";

	private boolean postOnly = true;

	private UserManagementService userManagementService;

	public UserPropertyAuthenticationFilter() {
		this(DEFAULT_FILTER_PROCESS_URL);
	}

	public UserPropertyAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
		this.messages = SecurityMessageSource.getAccessor();
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public void setUserManagementService(UserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException,
			ServletException {
		if ((this.postOnly) && (!(request.getMethod().equals("POST")))) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		String account = obtainAccount(request);
		String password = obtainUserPassword(request);
		if (account == null) {
			account = "";
		}
		if (password == null) {
			password = "";
		}
		account = account.trim();
		UserPropertyAuthenticationToken authRequest = new UserPropertyAuthenticationToken(account, password);
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	protected String obtainAccount(HttpServletRequest request) {
		return request.getParameter(SECURITY_AUTH_ACCOUNT_KEY);
	}

	protected String obtainUserPassword(HttpServletRequest request) {
		return request.getParameter(SECURITY_AUTH_PASSWORD_KEY);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
			throws IOException, ServletException {
		if (authResult instanceof UserPropertyAuthenticationToken && userManagementService != null) {
			UserPropertyAuthenticationToken token = (UserPropertyAuthenticationToken) authResult;
			User user = userManagementService.getUserByCode(token.getName());
			if (user != null) {
				userManagementService.updateUserLastLogin(user.getId(), new Date());
			}
		}
		super.successfulAuthentication(request, response, chain, authResult);
	}

}

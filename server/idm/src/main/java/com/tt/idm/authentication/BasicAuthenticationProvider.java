package com.tt.idm.authentication;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.tt.idm.authentication.adapter.UserObject;
import com.tt.idm.authentication.adapter.UserObjectService;
import com.tt.idm.exception.UserNotFoundException;

public class BasicAuthenticationProvider extends UserObjectAuthenticationProvider {

	private PasswordEncoder passwordEncoder = new StandardPasswordEncoder();

	private UserObjectService userObjectService;

	public BasicAuthenticationProvider(UserObjectService userObjectService) {
		this.userObjectService = userObjectService;
	}

	@Override
	protected UserObject retrieveUserByCode(String code, UserPropertyAuthenticationToken authentication) throws AuthenticationException {
		UserObject found;
		try {
			found = (UserObject) this.getUserObjectService().loadUserByUsername(code);
			if (found == null || (found.getExternalUserReference() != null && found.getExternalUserReference().getExternalId() != null)) {
				throw new UserNotFoundException("Unknown user " + code);
			}
		} catch (UserNotFoundException userNotFound) {
			throw userNotFound;
		} catch (Exception repositoryProblem) {
			throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}
		return found;
	}

	protected void additionalAuthenticationChecks(UserObject userObject, UserPropertyAuthenticationToken authentication) throws AuthenticationException {
		String msg = messages.getMessage("authentication.badCredentials", "Bad Credential");
		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");
			throw new BadCredentialsException(msg);
		}
		String presentedPassword = authentication.getCredentials().toString();
		if (!passwordEncoder.matches(presentedPassword, userObject.getPassword())) {
			logger.debug("Authentication failed: password does not match stored value");
			throw new BadCredentialsException(msg);
		}
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	protected PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	protected UserObjectService getUserObjectService() {
		return userObjectService;
	}

	public void setUserObjectService(UserObjectService userObjectService) {
		this.userObjectService = userObjectService;
	}

}

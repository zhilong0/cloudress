package com.df.idm.test;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import com.df.idm.authentication.UserPropertyAuthenticationToken;
import com.df.idm.authentication.adapter.UserObject;
import com.df.idm.dao.UserDao;
import com.df.idm.model.User;
import com.df.idm.service.contract.UserManagementService;

public class AuthenticationServiceTest extends IdmBaseTest {

	@Autowired
	private AuthenticationProvider authProvider;

	@Autowired
	private UserManagementService userManagementService;

	@Autowired
	private UserDao userDao;

	@Test
	public void testAuthenticationProviderWithEmailAccount() {
		String email = "sala223@msn.com";
		String password = "Oba.2247d";
		User newUser = null;
		try {
			newUser = userManagementService.createUserByEmail(email, password);
			UserPropertyAuthenticationToken authToken = new UserPropertyAuthenticationToken(email, password);
			Authentication authentication = authProvider.authenticate(authToken);
			TestCase.assertTrue(authentication instanceof UserPropertyAuthenticationToken);
			UserPropertyAuthenticationToken token = (UserPropertyAuthenticationToken) authentication;
			TestCase.assertEquals(token.getDetails().getClass(), UserObject.class);
			UserObject userObject = (UserObject) token.getDetails();
			TestCase.assertEquals(userObject.getEmail(), email);
		} finally {
			if (newUser != null && newUser.getId() != null) {
				userDao.deleteUserById(newUser.getId());
			}
		}
	}

	@Test
	public void testAuthenticationProviderWithIncorrectAccount() {
		String code = "sala223";
		String password = "Oba.2247d";
		User newUser = null;
		try {
			newUser = userManagementService.createUserByCode(code, password);
			UserPropertyAuthenticationToken authToken = new UserPropertyAuthenticationToken("errorAccount", password);
			try {
				authProvider.authenticate(authToken);
				TestCase.fail();
			} catch (BadCredentialsException ex) {
			}
		} finally {
			if (newUser != null && newUser.getId() != null) {
				userDao.deleteUserById(newUser.getId());
			}
		}

	}
}

package com.df.idm.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.df.idm.model.User;
import com.df.idm.registration.UserRegistrationTokenVerfier;

public class MailTest extends IdmBaseTest {

	@Autowired
	private UserRegistrationTokenVerfier verifier;

	@Test
	public void testSendUserVerificationMail() {
		User user = new User();
		user.setEmail("sala223@msn.com");
		String token = verifier.generateEmailRegistrationToken(user);
		verifier.verifyEmailRegistrationToken(token);
	}
}

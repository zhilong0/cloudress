package com.tt.idm.authentication;

import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import com.tt.idm.authentication.adapter.UserObject;

public class AccountStatusUserObjectChecker implements UserObjectChecker {

	private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();

	@Override
	public void check(UserObject user) {
		userDetailsChecker.check(user);
	}

}

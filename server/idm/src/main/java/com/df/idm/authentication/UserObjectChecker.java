package com.df.idm.authentication;

import com.df.idm.authentication.adapter.UserObject;

public interface UserObjectChecker {

	void check(UserObject user);
}

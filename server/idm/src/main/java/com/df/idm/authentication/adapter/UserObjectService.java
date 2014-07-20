package com.df.idm.authentication.adapter;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserObjectService extends UserDetailsService {

	UserObject loadUserByEmail(String email);

	UserObject loadUserByCellphone(String cellphone);
}

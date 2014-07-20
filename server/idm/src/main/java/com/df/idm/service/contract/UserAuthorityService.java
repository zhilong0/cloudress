package com.df.idm.service.contract;

import com.df.idm.model.Role;
import com.df.idm.model.User;

public interface UserAuthorityService {

	public void assign(User user, Role role);

	public void revoke(User user, Role role);
}

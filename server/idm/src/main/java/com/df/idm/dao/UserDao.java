package com.df.idm.dao;

import com.df.idm.model.Constants;
import com.df.idm.model.User;
import com.df.idm.model.ExternalUserReference.Provider;

public interface UserDao extends Constants {

	User getUserById(String id);

	User getUserByCode(String code);

	User getUserByEmail(String email);

	User getUserByCellphone(String cellphone);

	User getUserByExternalId(String externalId, Provider provider);

	int updateUserPassword(String id, String newEncodedPassword);

	void insertUser(User user);

	int updateUser(User user);

	int updateUserProperties(String userId, Property<?>... properties);

}

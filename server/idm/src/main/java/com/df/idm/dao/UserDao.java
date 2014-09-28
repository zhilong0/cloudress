package com.df.idm.dao;

import java.util.List;

import com.df.idm.model.Constants;
import com.df.idm.model.User;
import com.df.idm.model.ExternalUserReference.Provider;

public interface UserDao extends Constants {

	User getUserById(String userId);

	User getUserByCode(String code);

	User getUserByEmail(String email);

	User getUserByCellphone(String cellphone);

	User getUserByExternalId(String externalId, Provider provider);

	int updateUserPassword(String userCode, String newEncodedPassword);

	void insertUser(User user);

	int updateUser(User user);

	int updateUserProperties(String userId, Property<?>... properties);

	boolean deleteUserById(String userId);

	boolean deleteUserByCode(String code);
	
	List<User> getUserList(int offset, int limit);

}

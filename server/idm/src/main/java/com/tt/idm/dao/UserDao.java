package com.tt.idm.dao;

import java.util.List;

import com.tt.idm.model.Constants;
import com.tt.idm.model.User;
import com.tt.idm.model.ExternalUserReference.Provider;

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

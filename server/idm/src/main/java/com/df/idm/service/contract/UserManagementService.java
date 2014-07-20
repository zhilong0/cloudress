package com.df.idm.service.contract;

import com.df.idm.model.ExternalUserReference;
import com.df.idm.model.User;
import com.df.idm.model.ExternalUserReference.Provider;

public interface UserManagementService {

	User createUserByEmail(String email, String password);

	User createUserByCellphone(String cellphone, String password);

	User createUserByCode(String userCode, String password);

	User createUserByExternalUser(ExternalUserReference externalUser);

	User updateUser(User user);

	User verifyEmailRegistrationToken(String token);

	boolean verifyCellphoneRegistrationToken(String cellphone, String token);

	void updatePassword(String userId, String oldPassword, String newPassword);

	User getUserByExternalId(String externalId, Provider provider);

	User getUserById(String userId);

	User getUserByEmail(String mail);

	User getUserByCode(String userCode);

	User getUserByCellphone(String cellphone);

	void disableUser(String userId);

}

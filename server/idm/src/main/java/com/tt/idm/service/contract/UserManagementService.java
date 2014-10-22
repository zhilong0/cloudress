package com.tt.idm.service.contract;

import java.util.Date;
import java.util.List;

import com.tt.idm.model.ExternalUserReference;
import com.tt.idm.model.User;
import com.tt.idm.model.ExternalUserReference.Provider;

public interface UserManagementService {

	User createUserByEmail(String email, String password);

	User createUserByCellphone(String cellphone, String password, String registrationToken);

	User createUserByCode(String userCode, String password);

	User createUserByExternalUser(ExternalUserReference externalUser);

	User updateUser(User user);

	User verifyEmailRegistrationToken(String token);

	void sendCellphoneRegistrationToken(String cellphone);

	void updatePassword(String userCode, String oldPassword, String newPassword);

	void updatePassword(String userCode, String newPassword);

	User getUserByExternalId(String externalId, Provider provider);

	User getUserById(String userId);

	User getUserByEmail(String mail);

	User getUserByCode(String userCode);

	User getUserByCellphone(String cellphone);

	void disableUser(String userCode);

	void enableUser(String userCode);

	void unLockUser(String userCode);

	void lockUser(String userCode);

	void updateUserLastLogin(String userCode, Date lastLoginDate);

	List<User> getUserList(int offset, int limit);

}

package com.tt.idm.registration;

import com.tt.idm.model.User;

public interface UserRegistrationTokenVerfier {

	String generateEmailRegistrationToken(User newUser);

	String generateCellphoneRegistrationToken(User newUser);

	String verifyEmailRegistrationToken(String token);

	boolean verifyCellphoneRegistrationToken(String cellphone, String token);

}

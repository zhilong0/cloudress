package com.df.idm.registration.message;

import com.df.idm.model.User;

public interface RegistrationMessageNotifier {

	void sendVerificationEmail(User newUser, String token);

	void sendVerificationShortMessage(User newUser, String token);

}

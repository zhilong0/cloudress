package com.tt.idm.registration.message;

import com.tt.idm.model.User;

public interface RegistrationMessageNotifier {

	void sendVerificationEmail(User newUser, String token);

	void sendVerificationShortMessage(User newUser, String token);

}

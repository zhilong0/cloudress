package com.tt.idm.registration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface EmailVerificationResultHandler {

	public void onVerificationSucceed(String email, HttpServletRequest request, HttpServletResponse response) throws Exception;

	public void onVerificationFailure(HttpServletRequest request, HttpServletResponse response) throws Exception;

}

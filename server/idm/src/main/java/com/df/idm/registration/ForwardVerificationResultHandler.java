package com.df.idm.registration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardVerificationResultHandler implements EmailVerificationResultHandler {

	private String succeedUrl = "verification_success.html";

	private String failedUrl = "verification_failure.html";

	public static String REGISTRATION_VERIFICATION_EMAIL_ACCOUNT = "com.df.idm.registration.verification.email";

	public ForwardVerificationResultHandler() {
	}

	public ForwardVerificationResultHandler(String succeedUrl, String failedUrl) {
		this.succeedUrl = succeedUrl;
		this.failedUrl = failedUrl;
	}

	@Override
	public void onVerificationSucceed(String email, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute(REGISTRATION_VERIFICATION_EMAIL_ACCOUNT, email);
		request.getRequestDispatcher(succeedUrl).forward(request, response);

	}

	@Override
	public void onVerificationFailure(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getRequestDispatcher(failedUrl).forward(request, response);
	}

	public void setSucceedUrl(String succeedUrl) {
		this.succeedUrl = succeedUrl;
	}

	public void setFailedUrl(String failedUrl) {
		this.failedUrl = failedUrl;
	}

}

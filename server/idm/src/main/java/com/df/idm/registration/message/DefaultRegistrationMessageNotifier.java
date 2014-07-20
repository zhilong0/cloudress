package com.df.idm.registration.message;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.mail.SimpleMailMessage;

import com.df.common.mail.EmailContextProvider;
import com.df.common.mail.RichMailSender;
import com.df.idm.model.User;

public class DefaultRegistrationMessageNotifier implements RegistrationMessageNotifier {

	public static String DEFAULT_TOKEN_PARAMETER = "token";

	public static String DEFAULT_VERIFY_URL_PREFIX = "/registration/verify";

	public static String DEFAULT_EMAIL_VERIFICATION_TPL = "registration_verify.ftl";

	private RichMailSender sender;

	private AsyncTaskExecutor taskExecutor;

	private EmailContextProvider emailContextProvider;

	private String tokenParameter = DEFAULT_TOKEN_PARAMETER;

	private String verifyUrlPrefix = DEFAULT_VERIFY_URL_PREFIX;

	private String emailVerificationTemplate = DEFAULT_EMAIL_VERIFICATION_TPL;

	private static final Logger logger = LoggerFactory.getLogger(DefaultRegistrationMessageNotifier.class);

	@Override
	public void sendVerificationEmail(User newUser, String token) {
		final String userEmail = newUser.getEmail();
		final SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(newUser.getEmail());
		message.setFrom(emailContextProvider.getSentFrom());
		message.setSubject("New User Email verification");
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(tokenParameter, token);
		String link = emailContextProvider.getHyperLink(false, verifyUrlPrefix, parameters);
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("user", newUser);
		variables.put("link", link);
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					sender.send(emailVerificationTemplate, message, variables);
				} catch (Throwable ex) {
					logger.error("Failed to sent verification email for user " + userEmail, ex);
				}
			}
		});
	}

	@Override
	public void sendVerificationShortMessage(User newUser, String token) {
	}

	public void setRichMailSender(RichMailSender sender) {
		this.sender = sender;
	}

	public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setEmailContextProvider(EmailContextProvider emailContextProvider) {
		this.emailContextProvider = emailContextProvider;
	}

	public void setVerifyUrlPrefix(String verifyUrlPrefix) {
		this.verifyUrlPrefix = verifyUrlPrefix;
	}

	public void setTokenParameter(String tokenParameter) {
		this.tokenParameter = tokenParameter;
	}

	public void setEmailVerificationTemplate(String emailVerificationTemplate) {
		this.emailVerificationTemplate = emailVerificationTemplate;
	}

}

package com.tt.idm.exception;

public class UserException extends IdmException {

	private static final long serialVersionUID = 1L;

	public static final String USER_EMAIL_NOT_FOUND = "U100001";

	public static final String USER_CELL_PHONE_NOT_FOUND = "U100002";

	public static final String USER_EMAIL_ALREADY_EXIST = "U100003";

	public static final String USER_CELLPHONE_ALREADY_EXIST = "U100004";

	public static final String USER_PASSWORD_NOT_MATCH = "U100006";

	public static final String USER_ID_NOT_FOUND = "U100007";

	public static final String USER_CODE_NOT_FOUND = "U100008";

	public static final String USER_CODE_ALREADY_EXIST = "U100009";

	public static final String INVALID_CELLPHONE_VERIFICATION_TOKEN = "U100010";

	public UserException(Throwable cause, String errorCode) {
		super(cause, errorCode);
	}

	public UserException(Throwable cause, String errorCode, String messageFormat, Object... args) {
		super(cause, errorCode, messageFormat, args);
	}

	public UserException(String errorCode, String messageFormat, Object... args) {
		super(null, errorCode, messageFormat, args);
	}

	public static UserException userEmailNotFound(String email) {
		String msg = "User wtih email %s is not found.";
		return new UserException(USER_EMAIL_NOT_FOUND, msg, email);
	}

	public static UserException userIdNotFound(String userId) {
		String msg = "User wtih id %s is not found.";
		return new UserException(USER_ID_NOT_FOUND, msg, userId);
	}

	public static UserException userTelephoneNotFound(String telephone) {
		String msg = "User wtih telephone %s is not found";
		return new UserException(USER_CELL_PHONE_NOT_FOUND, msg, telephone);
	}

	public static UserException userEmailAlreadyExist(String email) {
		String msg = "User wtih email %s is already exist";
		return new UserException(USER_EMAIL_ALREADY_EXIST, msg, email);
	}

	public static UserException userCellphoneAlreadyExist(String telephone) {
		String msg = "User wtih cellphone %s is already exist";
		return new UserException(USER_CELLPHONE_ALREADY_EXIST, msg, telephone);
	}

	public static UserException userPasswordNotMatch(String account) {
		String msg = "Old password does not match";
		return new UserException(USER_PASSWORD_NOT_MATCH, msg, account);
	}

	public static UserException userCodeNotFound(String userCode) {
		String msg = "User wtih code %s is not found";
		return new UserException(USER_CODE_NOT_FOUND, msg, userCode);
	}

	public static UserException userCodeAlreadyExist(String userCode) {
		String msg = "User wtih code %s is already exist";
		return new UserException(USER_CODE_ALREADY_EXIST, msg, userCode);
	}

	public static UserException invalidCellphoneVerificationToken() {
		String msg = "Invalid cellphone registration verification token";
		return new UserException(INVALID_CELLPHONE_VERIFICATION_TOKEN, msg);
	}
}

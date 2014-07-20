package com.df.common.utils;

import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public abstract class StringUtils extends org.springframework.util.StringUtils {

	private static final String EMAIL_REGULAR_EXPRESSION = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

	public static boolean isValidEmail(String email) {
		if (email != null) {
			return Pattern.matches(EMAIL_REGULAR_EXPRESSION, email);
		}
		return false;
	}

	public static String normalizeEmail(String email) {
		if (email != null) {
			return email.trim();
		} else {
			return email;
		}
	}

	public static String randomString(int length) {
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ0123456789+@";

		Random random = new Random(new Date().getTime());
		String str = "";
		for (int i = 0; i < length; i++) {
			int index = (int) (random.nextDouble() * letters.length());
			str += letters.substring(index, index + 1);
		}
		return str;
	}

	public static String UUID() {
		String s = java.util.UUID.randomUUID().toString();
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
	}

}

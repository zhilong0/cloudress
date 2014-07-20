package com.df.idm.registration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.df.common.secure.DataMarshaller;
import com.df.common.secure.EncryptionDataMarshaller;
import com.df.idm.exception.UserRegistrationException;
import com.df.idm.model.User;

public class UserRegistrationTokenVerifierImpl implements UserRegistrationTokenVerfier {

	private DataMarshaller dataMarshaller = new EncryptionDataMarshaller();

	private static final Charset utf8 = Charset.forName("utf-8");

	private int validDuration = 60 * 24 * 3;

	private static final int CELLPHONE_REGISTRATION_RANDOM_LENGTH = 4;

	private static final Logger logger = LoggerFactory.getLogger(UserRegistrationTokenVerifierImpl.class);

	public UserRegistrationTokenVerifierImpl() {
	}

	public UserRegistrationTokenVerifierImpl(DataMarshaller dataMarshaller) {
		this.dataMarshaller = dataMarshaller;
	}

	public void setValidDuration(int duration) {
		this.validDuration = duration;
	}

	@Override
	public String verifyEmailRegistrationToken(String token) {
		try {
			byte[] securedData = Base64.decodeBase64(token);
			byte[] rawData = dataMarshaller.disclose(securedData);
			ByteArrayInputStream arrayInput = new ByteArrayInputStream(rawData);
			DataInputStream inputStream = new DataInputStream(arrayInput);
			long validUntil = inputStream.readLong();
			if (new Date().getTime() <= validUntil) {
				return new String(rawData, 8, rawData.length - 8, utf8);
			} else {
				return null;
			}
		} catch (Throwable ex) {
			logger.info("Unmarshalling token " + token + " error", ex);
			return null;
		}
	}

	@Override
	public boolean verifyCellphoneRegistrationToken(String cellphone, String token) {
		return false;
	}

	@Override
	public String generateEmailRegistrationToken(User newUser) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			byte[] email = newUser.getEmail().getBytes(utf8);
			Calendar calendar = Calendar.getInstance();
			Date validFrom = new Date();
			calendar.setTime(validFrom);
			calendar.add(Calendar.MINUTE, validDuration);
			long validUntil = calendar.getTime().getTime();
			dos.writeLong(validUntil);
			dos.write(email);
			byte[] securedData = dataMarshaller.seal(bos.toByteArray());
			return Base64.encodeBase64URLSafeString(securedData);
		} catch (Throwable ex) {
			throw new UserRegistrationException(ex);
		}
	}

	@Override
	public String generateCellphoneRegistrationToken(User newUser) {
		String letters = "0123456789";
		Random random = new Random(new Date().getTime());
		String str = "";
		for (int i = 0; i < CELLPHONE_REGISTRATION_RANDOM_LENGTH; i++) {
			int index = Math.abs(random.nextInt() % letters.length());
			str += letters.substring(index, index + 1);
		}
		return str;
	}
}

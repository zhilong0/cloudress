package com.tt.idm.registration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tt.common.secure.DataMarshaller;
import com.tt.common.secure.EncryptionDataMarshaller;
import com.tt.idm.exception.UserRegistrationException;
import com.tt.idm.model.User;

public class UserRegistrationTokenVerifierImpl implements UserRegistrationTokenVerfier {

	private DataMarshaller dataMarshaller = new EncryptionDataMarshaller();

	private static final Charset utf8 = Charset.forName("utf-8");

	private int emailTokenValidDuration = 60 * 24 * 3;

	private TokenStore tokenStore;

	private static final int CELLPHONE_REGISTRATION_TOKEN_LENGTH = 6;

	private int cellphoneTokenValidDuration = 5;

	private static final Logger logger = LoggerFactory.getLogger(UserRegistrationTokenVerifierImpl.class);

	public UserRegistrationTokenVerifierImpl(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	public UserRegistrationTokenVerifierImpl(TokenStore tokenStore, DataMarshaller dataMarshaller) {
		this(tokenStore);
		this.dataMarshaller = dataMarshaller;
	}

	public void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	public void setEmailTokenValidDuration(int duration) {
		this.emailTokenValidDuration = duration;
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
		Token t = tokenStore.getToken(token);
		if (t == null) {
			return false;
		} else {
			if (cellphone.equals(t.getStick())) {
				tokenStore.expire(token);
				return !t.isExpired();
			}
		}
		return false;
	}

	@Override
	public String generateEmailRegistrationToken(User newUser) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			byte[] email = newUser.getEmail().getBytes(utf8);
			long duration = TimeUnit.MINUTES.toMillis(emailTokenValidDuration);
			dos.writeLong(new Date().getTime() + duration);
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
		for (int i = 0; i < CELLPHONE_REGISTRATION_TOKEN_LENGTH; i++) {
			int index = Math.abs(random.nextInt() % letters.length());
			str += letters.substring(index, index + 1);
		}
		long duration = TimeUnit.MINUTES.toMillis(cellphoneTokenValidDuration);
		Token token = new Token(str, new Date().getTime() + duration);
		token.setStick(newUser.getCellphone());
		this.tokenStore.save(token);
		return str;
	}
}

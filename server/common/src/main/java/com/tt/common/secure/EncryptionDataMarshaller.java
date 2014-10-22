package com.tt.common.secure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.Assert;

public class EncryptionDataMarshaller implements DataMarshaller {

	private String algorithm;

	private static final int ENCRYPTION_ALGORITHM_LENGTH = 128;

	public static final String SECRET_ALGORITHM_AES = "AES";

	public EncryptionDataMarshaller(String algorithm) {
		this.algorithm = algorithm;
	}

	public EncryptionDataMarshaller() {
		this.algorithm = SECRET_ALGORITHM_AES;
	}

	public static SecretKey createSecretKey(String algorithm, int bit) throws NoSuchAlgorithmException {
		KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
		kgen.init(bit);
		return kgen.generateKey();
	}

	public static byte[] encrypt(SecretKey key, byte[] content) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
			Cipher cipher = Cipher.getInstance(key.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			return cipher.doFinal(content);
		} catch (Throwable e) {
			throw new SecurityException(e);
		}
	}

	public static byte[] decrypt(String algorithm, byte[] key, byte[] source) {
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(key, algorithm);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			return cipher.doFinal(source);
		} catch (Exception ex) {
			throw new SecurityException(ex);
		}
	}

	public byte[] seal(byte[] data) {
		try {
			SecretKey key = createSecretKey(algorithm, ENCRYPTION_ALGORITHM_LENGTH);
			byte[] keyBytes = key.getEncoded();
			byte[] securedData = encrypt(key, data);
			SecuredDataObject sdo = new SecuredDataObject();
			sdo.setAlgorithm(key.getAlgorithm());
			sdo.setData(securedData);
			sdo.setKey(keyBytes);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			sdo.write(dos);
			return bos.toByteArray();
		} catch (NoSuchAlgorithmException ex) {
			throw new SecurityException(ex);
		}
	}

	public byte[] disclose(byte[] securedData) {
		SecuredDataObject sdo = new SecuredDataObject();
		ByteArrayInputStream bis = new ByteArrayInputStream(securedData);
		DataInputStream dis = new DataInputStream(bis);
		sdo.read(dis);
		return decrypt(sdo.getAlgorithm(), sdo.getKey(), sdo.getData());
	}

	private static final class SecuredDataObject {

		private String algorithm;

		private byte[] key;

		private byte[] data;

		public void write(DataOutputStream out) {
			Assert.notNull(algorithm);
			Assert.notNull(key);
			Assert.notNull(data);
			try {
				byte[] ab = algorithm.getBytes();
				out.writeInt(ab.length);
				out.write(ab);
				out.writeInt(key.length);
				out.write(key);
				out.writeInt(data.length);
				out.write(data);
			} catch (IOException e) {
				throw new SecurityException(e, "Output secured data error.");
			}
		}

		public void read(DataInputStream in) {
			try {
				int length = in.readInt();
				byte[] aBytes = new byte[length];
				in.readFully(aBytes);
				length = in.readInt();
				byte[] keyBytes = new byte[length];
				in.readFully(keyBytes);
				length = in.readInt();
				byte[] edBytes = new byte[length];
				in.readFully(edBytes);
				if (in.read() != -1) {
					throw new SecurityException("Expect EOF for the inputstream");
				}
				this.algorithm = new String(aBytes);
				this.data = edBytes;
				this.key = keyBytes;
			} catch (IOException e) {
				throw new SecurityException(e);
			}
		}

		String getAlgorithm() {
			return algorithm;
		}

		void setAlgorithm(String algorithm) {
			this.algorithm = algorithm;
		}

		byte[] getKey() {
			return key;
		}

		void setKey(byte[] key) {
			this.key = key;
		}

		byte[] getData() {
			return data;
		}

		void setData(byte[] data) {
			this.data = data;
		}
	}
}

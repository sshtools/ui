/* HEADER */
package com.sshtools.ui.swing;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class StringEncrypter {

	private Cipher ecipher;
	private Cipher dcipher;
	private byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
			(byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };
	private int iterationCount = 19;

	StringEncrypter(char[] passPhrase) throws Exception {
		KeySpec keySpec = new PBEKeySpec(passPhrase, salt, iterationCount);
		SecretKey key = SecretKeyFactory.getInstance("PBEWithSHA1AndDESede")
				.generateSecret(keySpec);
		ecipher = Cipher.getInstance(key.getAlgorithm());
		dcipher = Cipher.getInstance(key.getAlgorithm());
		AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
				iterationCount);
		ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
	}

	public String encrypt(String str) throws Exception {
		byte[] utf8 = str.getBytes("UTF8");
		byte[] enc = ecipher.doFinal(utf8);
		return Base64.encodeBase64String(enc);
	}

	public String decrypt(String str) throws Exception {
		byte[] dec = Base64.decodeBase64(str);
		byte[] utf8 = dcipher.doFinal(dec);
		return new String(utf8, "UTF8");
	}

	public static String encryptString(String unencryptedString,
			char[] encryptionKey) throws Exception {
		return new StringEncrypter(encryptionKey).encrypt(unencryptedString);
	}

	public static String decryptString(String encryptedString,
			char[] encryptionKey) throws Exception {
		return new StringEncrypter(encryptionKey).decrypt(encryptedString);
	}
}

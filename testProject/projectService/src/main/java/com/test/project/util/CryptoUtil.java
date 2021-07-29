package com.test.project.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.redjframework.security.Base64;

@Component
public class CryptoUtil {

	/**
	 * Sha512.
	 *
	 * @param msg the msg
	 * @param charsetName the charset name
	 * @return the string
	 */
	public static String sha512(String msg, String charsetName) {
		return md(msg, charsetName, "SHA-512");
	}

	/**
	 * Md.
	 *
	 * @param msg the msg
	 * @param charsetName the charset name
	 * @param algorithmStr the algorithm str
	 * @return the string
	 */
	private static String md(String msg, String charsetName, String algorithmStr){
		try {
			return md(msg.getBytes(charsetName), algorithmStr);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Md.
	 *
	 * @param bs the bs
	 * @param algorithmStr the algorithm str
	 * @return the string
	 */
	private static String md(byte[] bs, String algorithmStr){
		try {
			MessageDigest algorithm = MessageDigest.getInstance(algorithmStr);
			byte messageDigest[] = algorithm.digest(bs);

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]);
				if (hex.length() == 1)
					hexString.append('0');

				hexString.append(hex);
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String base64Encode(String value) {
		String encodedString = Base64.encodeToString(value.getBytes(), "UTF-8");
		return encodedString;
	}
	
	public static String base64Decode(String value) throws UnsupportedEncodingException {
		String decodedString =  new String(Base64.decode(value, "UTF-8"));
		return decodedString;
	}
	
	String transformation = null;
	
	String algorithm = null;

	String charset = "UTF-8";

	public CryptoUtil(@Value("${crypto.transformation}")String algorithm, @Value("${crypto.algorithm}")String transformation) {
		super();
		this.algorithm = algorithm;
		this.transformation = transformation;
	}

	public String enc(String key, String value) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchProviderException {
		Cipher cipher = cipher();
		byte[] raw = key.getBytes(charset);
		SecretKeySpec keySpec = new SecretKeySpec(raw, algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);

		return Base64.encodeToString(cipher.doFinal(value.getBytes(charset)), charset);
	}

	public String dec(String key, String value) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchProviderException {
		byte[] dec = Base64.decode(value, charset);

		Cipher cipher = cipher();
		byte[] raw = key.getBytes(charset);
		SecretKeySpec keySpec = new SecretKeySpec(raw, algorithm);
		cipher.init(Cipher.DECRYPT_MODE, keySpec);

		return new String(cipher.doFinal(dec), charset);
	}

	private Cipher cipher() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
		return Cipher.getInstance(transformation);
	}
}

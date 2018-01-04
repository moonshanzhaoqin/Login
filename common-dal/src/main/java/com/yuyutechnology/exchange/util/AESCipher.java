package com.yuyutechnology.exchange.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESCipher {

	private static final String IV_STRING = "anytime_exchange";
	
	public static String encryptAES(String content, String key) 
			throws InvalidKeyException, NoSuchAlgorithmException, 
			NoSuchPaddingException, UnsupportedEncodingException, 
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		byte[] byteContent = content.getBytes("UTF-8");

		byte[] enCodeFormat = key.getBytes();
	    SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			
	    byte[] initParam = IV_STRING.getBytes();
	    IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
			
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		
	    byte[] encryptedBytes = cipher.doFinal(byteContent);
		
//	    Encoder encoder = Base64.getEncoder();
//	    return encoder.encodeToString(encryptedBytes);
	    return Base64.encodeBase64String(encryptedBytes);
	}

	public static String decryptAES(String content, String key) 
			throws InvalidKeyException, NoSuchAlgorithmException, 
			NoSuchPaddingException, InvalidAlgorithmParameterException, 
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
			
//	    Decoder decoder = Base64.getDecoder();
//	    byte[] encryptedBytes = decoder.decode(content);
		byte[] encryptedBytes = Base64.decodeBase64(content);
		
	    byte[] enCodeFormat = key.getBytes();
	    SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
		
	    byte[] initParam = IV_STRING.getBytes();
	    IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

	    byte[] result = cipher.doFinal(encryptedBytes);
		
	    return new String(result, "UTF-8");
	}
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//		String string = AESCipher.encryptAES("{\"name\": \"Silent\",\"age\": 30}", "anytime_exchange");
//		System.out.println(string);
//		System.out.println(AESCipher.decryptAES(string, "anytime_exchange"));
		String string = AESCipher.encryptAES("123456789012345123456789012345", "anytime_exchange");
		System.out.println(string.length());
		System.out.println(AESCipher.decryptAES(string, "anytime_exchange"));
	}
}

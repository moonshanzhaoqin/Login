package com.yuyutechnology.exchange.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class PasswordUtils {
	/**
	 * 加密
	 * 
	 * @param password(包括userPassword,UserPayPwd,明码)
	 * @param passwordSalt(盐值) = user.getPasswordSalt()
	 * @return 密文
	 */
	public static String encrypt(String password, String passwordSalt) {
		return DigestUtils.md5Hex(DigestUtils.md5Hex(password) + passwordSalt);
	}

	/**
	 * 校验
	 * 
	 * @param plaintext 明文
	 * @param ciphertext 
	 * @param passwordSalt
	 * @return
	 */
	public static boolean check(String plaintext, String ciphertext, String passwordSalt) {
		if (StringUtils.equals(ciphertext, encrypt(plaintext, passwordSalt))) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args){
		String str = encrypt("123456", "dffd2d6600e0c4436809b3839de2d8cd");
		System.out.println(str);
	}
}

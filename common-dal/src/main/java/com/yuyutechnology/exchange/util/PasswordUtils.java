package com.yuyutechnology.exchange.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 密码加密算法
 * 
 * @author suzan.wu
 *
 */
public class PasswordUtils {
	/**
	 * 加密
	 * 
	 * @param password
	 *            包括userPassword,UserPayPwd,明码
	 * @param passwordSalt
	 *            盐值 user.getPasswordSalt()
	 * @return 密文
	 */
	public static String encrypt(String password, String passwordSalt) {
		return DigestUtils.md5Hex(DigestUtils.md5Hex(password) + passwordSalt);
	}

	/**
	 * 校验
	 * 
	 * @param plaintext
	 *            明文
	 * @param ciphertext
	 * @param passwordSalt
	 * @return
	 */
	public static boolean check(String plaintext, String ciphertext, String passwordSalt) {
		return StringUtils.equals(ciphertext, encrypt(plaintext, passwordSalt));
	}

	public static void main(String[] args) {
		String str = encrypt("12345678", "ddaf1514c283018d522ffee56bb5cccc");
		System.out.println(str);
	}
}

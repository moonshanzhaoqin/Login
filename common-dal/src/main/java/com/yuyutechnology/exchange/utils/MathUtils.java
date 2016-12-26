package com.yuyutechnology.exchange.utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
/**
 * 
 * @author sunwei
 * 
 */
public class MathUtils
{
	/**
	 * 随机固定数的str
	 * 
	 * @param length
	 * @return
	 */
	public static String randomFixedLengthStr(int length)
	{
		double pross = (RandomUtils.nextDouble() + 1) * Math.pow(10, length);
		String prossStr = String.valueOf(pross);
		return prossStr.substring(1, length + 1);
	}
	/**
	 * 随机生成openID
	 * @param length
	 * @return
	 */
	public static String randomOpenID(int length)
	{
		long random = RandomUtils.nextLong();
		String randomStr = String.valueOf(random);
		return randomStr.substring(0, length-1);
	}
	/**
	 * 根据用户名,ID生成openID
	 * @param username
	 * @return
	 */
	public static String openID(String username,long userId)
	{
		StringBuilder sb = new StringBuilder();
		for (char ch : username.toCharArray())
		{
			sb.append((int)ch);
		}
		return userId+sb.toString();
	}
	
	public static String hideString(String string) {
		if (StringUtils.isBlank(string) || string.length() <= 2) {
			return "**";
		}
		int index = Double.valueOf(Math.floor(Double.valueOf(string.length()) / 4.0)).intValue();
		int hideLengh = string.length() - index * 2;
		String hideString = "";
		for (int i = 0; i < hideLengh; i++) {
			hideString += "*";
		}
		return string.substring(0, index) + hideString + string.substring(string.length()-index);
	}
}

package com.yuyutechnology.exchange.utils;
import java.util.UUID;
/**
 * 
 * @author silent.sun
 * 
 */
public class UidUtils
{
	/**
	 * @param request
	 * @return
	 */
	public static String genUid()
	{
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		StringBuffer sb = new StringBuffer();
		sb.append(str.subSequence(0, 8)).append(str.substring(9, 13))
				.append(str.substring(14, 18)).append(str.substring(19, 23))
				.append(str.substring(24));
		return sb.toString();
	}
}

package com.yuyutechnology.exchange.server.session;
/**
 * 
 * @author sunwei
 * 
 */
public class SessionDataHolder
{
	private static ThreadLocal<SessionData> threadLocalData = new ThreadLocal<SessionData>();
	/**
	 * 
	 * @param data
	 */
	public static void setSessionData(SessionData data)
	{
		threadLocalData.set(data);
	}
	/**
	 * 
	 */
	public static void clear()
	{
		threadLocalData.remove();
	}
	/**
	 * 
	 * @return
	 */
	public static SessionData getSessionData()
	{
		return threadLocalData.get();
	}
}
/**
 * @(#)ServerContext.java ,Created on Nov 12, 2012
 * 
 * Copyright (c) aspectgaming
 * 
 * MODIFY MEMO:
 * kevin.sun/modify time/modify reason
 * 
 */
package com.yuyutechnology.exchange.startup;
import org.springframework.context.ApplicationContext;
/**
 * Server's Context for holding Spring context and getting bean from Spring .
 * 
 * @version Nov 12, 2012 6:26:08 PM
 * @author kevin.sun
 * @since JDK1.6
 */
public class ServerContext
{
	static String appHome;
	static ApplicationContext applicationContext;
	/**
	 * set spring's application context for something is not under spring
	 * manager
	 * 
	 * @param applicationContext
	 *            spring's application context
	 */
	public static void setApplicationContext(
			ApplicationContext applicationContext)
	{
		ServerContext.applicationContext = applicationContext;
	}
	/**
	 * get business bean from spring IOC container
	 * 
	 * @param <T>
	 * 
	 * @param requiredType
	 *            class Type
	 * @return instance that got from spring IOC container
	 */
	public static <T> T getBean(Class<T> requiredType)
	{
		return applicationContext.getBean(requiredType);
	}
	public static String getAppHome()
	{
		return appHome;
	}
}

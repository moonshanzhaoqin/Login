/**
 * @(#)Bootstrap.java ,Created on Nov 9, 2012
 * 
 * Copyright (c) aspectgaming
 * 
 * MODIFY MEMO:
 * kevin.sun/modify time/modify reason
 * 
 */
package com.yuyutechnology.exchange.startup;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;
/**
 * 系统启动入口类
 * 
 * @version Nov 9, 2012 12:03:57 PM
 * @author kevin.sun
 * @since JDK1.6
 */
public class Bootstrap
{
	/** spring application context abstract system path */
	private String contextCfgPath;
	/** abstract system path for slf4j's configuration */
	private String logCfgPath;
	private String appHome;
	/**
	 * construct method
	 * 
	 * @param contextCfgPath
	 *            spring application context abstract system path
	 * @param logCfgPath
	 *            abstract system path for slf4j's configuration
	 */
	public Bootstrap(String contextCfgPath, String logCfgPath)
	{
		this.contextCfgPath = contextCfgPath;
		this.logCfgPath = logCfgPath;
	}
	/**
	 * construct method
	 * 
	 * @param contextCfgPath
	 *            spring application context abstract system path
	 * @param logCfgPath
	 *            abstract system path for slf4j's configuration
	 */
	public Bootstrap(String appHome, String contextCfgPath, String logCfgPath)
	{
		this.appHome = appHome;
		this.contextCfgPath = contextCfgPath;
		this.logCfgPath = logCfgPath;
	}
	/**
	 * system initialize and start entry
	 */
	public void startup()
	{
		try
		{
			// initialize log
//			Log4jConfigurer.initLogging("file:" + logCfgPath);
			
			System.out.println("init log ok......");
			// initialize spring
			ApplicationContext applicationContext = new FileSystemXmlApplicationContext(
					"file:" + contextCfgPath);
			ServerContext.applicationContext = applicationContext;
			ServerContext.appHome = this.appHome;
			System.out.println("init spring IOC container ok......");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	/**
	 * system stop entry ,you can add a hook for system shutdown
	 */
	public void stop()
	{
	}
	public String getContextCfgPath()
	{
		return contextCfgPath;
	}
	public void setContextCfgPath(String contextCfgPath)
	{
		this.contextCfgPath = contextCfgPath;
	}
	public String getLogCfgPath()
	{
		return logCfgPath;
	}
	public void setLogCfgPath(String logCfgPath)
	{
		this.logCfgPath = logCfgPath;
	}
	public String getAppHome()
	{
		return appHome;
	}
	public void setAppHome(String appHome)
	{
		this.appHome = appHome;
	}
}

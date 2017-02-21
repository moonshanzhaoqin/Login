/**
 * @(#)GameServerInit.java ,Created on Nov 12, 2012
 * 
 * Copyright (c) aspectgaming
 * 
 * MODIFY MEMO:
 * kevin.sun/modify time/modify reason
 * 
 */
package com.yuyutechnology.exchange.init.listener;
import javax.servlet.ServletContextEvent;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yuyutechnology.exchange.startup.ServerContext;
/**
 * game server's initialize class.
 * 
 *@version Nov 12, 2012 6:38:33 PM
 *@author kevin.sun
 *@since JDK1.6
 */
public class ServerInit extends ContextLoaderListener
{
	public static Logger logger = LogManager.getLogger(ServerInit.class);
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		super.contextInitialized(sce);
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		ServerContext.setApplicationContext(webApplicationContext);
	}
}

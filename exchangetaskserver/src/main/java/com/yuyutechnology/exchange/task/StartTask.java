package com.yuyutechnology.exchange.task;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuyutechnology.exchange.startup.Bootstrap;
/**
 * Hello world!
 * 
 */
public class StartTask
{
	public static Logger logger = LoggerFactory.getLogger(StartTask.class);
	public static void main(String[] args)
	{
		String home = System.getProperty("task.home");
		if (StringUtils.isEmpty(home))
		{
			home = System.getenv().get("task.home");
		}
		if (home == null)
		{
			System.out
					.println("[task.home] is not set,please set like this :java -Dtask.home=D:/kevin/taskhome"
							+ new Date());
		}
		else
		{
			String logCfgPath = home + "/conf/log4j.properties";
			String contextCfgPath = home + "/conf/task-context.xml";
			System.out.println("home=" + home);
			System.out.println("contextCfgPath=" + contextCfgPath);
			Bootstrap bootstrap = new Bootstrap(contextCfgPath, logCfgPath);
			bootstrap.startup();
		}
	}
}

package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuyutechnology.exchange.utils.LanguageUtils;

/**
 * 
 * @author suzan.wu
 *
 */
public class LanguageTest extends BaseSpringJunit4{
	public static Logger logger = LoggerFactory.getLogger(LanguageTest.class);
	@Test	
	public void enumTEST(){
		
//		System.out.println(LanguageUtils.standard("en"));
//		System.out.println(LanguageUtils.standard("zh"));
//		System.out.println(LanguageUtils.standard("cn"));
//		System.out.println(LanguageUtils.standard("CN"));
//		System.out.println(LanguageUtils.standard("tw"));
//		System.out.println(LanguageUtils.standard("ZH"));
//		System.out.println(LanguageUtils.standard("ZH_CN"));
//		System.out.println(LanguageUtils.standard("zh_CN"));
//		logger.info("{}",LanguageUtils.standard("zh_CN"));
		logger.info("{}",LanguageUtils.standard("zh_CN"));
	}
}

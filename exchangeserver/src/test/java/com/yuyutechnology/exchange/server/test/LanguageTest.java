package com.yuyutechnology.exchange.server.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.ConfigKeyEnum;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.utils.LanguageUtils;
import com.yuyutechnology.exchange.utils.LanguageUtils.Language;

/**
 * 
 * @author suzan.wu
 *
 */
public class LanguageTest extends BaseSpringJunit4{
	public static Logger logger = LoggerFactory.getLogger(LanguageTest.class);
	
	@Autowired
	RedisDAO redisDAO;
	@Autowired
	ConfigManager configManager;
	
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Test	
	public void enumTEST() throws ParseException{
		
//		System.out.println(Language.standard("en").toString());
//		System.out.println(Language.standard("zh").name());
//		System.out.println(Language.standard("cn"));
//		System.out.println(LanguageUtils.standard("CN"));
//		System.out.println(LanguageUtils.standard("tw"));
//		System.out.println(LanguageUtils.standard("ZH"));
//		System.out.println(LanguageUtils.standard("ZH_CN"));
//		System.out.println(Language.values());
//		System.out.println(LanguageUtils.standard("zh"));
//		logger.info("{}",LanguageUtils.standard("zh_CN"));
//		logger.info("{},{},{}",Language.values());
//		logger.info("{}",Language.values()[2]);
//		
		
//		redisDAO.saveData("123",simpleDateFormat.format(new Date()), 3);
//		System.out.println(simpleDateFormat.parse(redisDAO.getValueByKey("123")).getTime() - new Date().getTime());
		
		
//		Calendar   cal1   =   Calendar.getInstance();
//		System.out.println(cal1.getTime());
//        cal1.add(Calendar.DATE,2);
//        System.out.println(cal1.getTime());
//		System.out.println(new BigDecimal(12.00));
//		System.out.println(new BigDecimal("12.00"));
//		System.out.println(new BigDecimal("12.00").intValue());
//		logger.info(new BigDecimal(12.00).toString());
//		logger.info(new BigDecimal("12.00").toString());
//		logger.info(new BigDecimal(new BigDecimal("12.00").intValue()).toString());
//		logger.info(new BigDecimal(new BigDecimal("12.12").intValue()).toString());
		System.out.println(BigDecimal.ZERO);
		
//		System.out.println(configManager.getConfigStringValue(ConfigKeyEnum.VERIFYTIME, "10"));
//		System.out.println(configManager.getConfigStringValue(ConfigKeyEnum.REFUNTIME, "7"));
		
	}
}

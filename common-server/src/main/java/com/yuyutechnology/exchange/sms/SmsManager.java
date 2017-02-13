/**
 * 
 */
package com.yuyutechnology.exchange.sms;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.ResourceUtils;

/**
 * @author suzan.wu
 *
 */
@Service
public class SmsManager {
	public static Logger logger = LoggerFactory.getLogger(SmsManager.class);
	
	@Autowired
	ConfigManager configManager;
	@Autowired
	CommonManager commonManager;

	private final String SMS_REPLACE_PIN = "[PIN]";
	private final String SMS_REPLACE_TIME = "[TIME]";
	private final String SMS_REPLACE_FROM = "[FROM]";
	private final String SMS_REPLACE_CURRENCY = "[CURRENCY]";
	private final String SMS_REPLACE_AMOUNT = "[AMOUNT]";
	private final String SMS_REPLACE_LINK = "[LINK]";
	
	private final String SMS_REPLACE_DIFFERENCE = "[DIFFERENCE]";
	private final String SMS_REPLACE_LOWERLIMIT = "[LOWERLIMIT]";
//	private final String SMS_REPLACE_GRADE = "[GRADE]";
	

	// en
	private StringBuffer phoneVerify_en = new StringBuffer();
	// zh_CN
	private StringBuffer phoneVerify_cn = new StringBuffer();
	// zh_HK
	private StringBuffer phoneVerify_hk = new StringBuffer();

	// en
	private StringBuffer transfer_en = new StringBuffer();
	// zh_CN
	private StringBuffer transfer_cn = new StringBuffer();
	// zh_HK
	private StringBuffer transfer_hk = new StringBuffer();
	
	// zh_CN
	private StringBuffer criticalAlarm_cn = new StringBuffer();

	@PostConstruct
	@Scheduled(cron = "0 1/2 * * * ?")
	public void init() {
		readTemplate("template/sms/en_US/phoneVerify.template", phoneVerify_en);
		readTemplate("template/sms/zh_CN/phoneVerify.template", phoneVerify_cn);
		readTemplate("template/sms/zh_HK/phoneVerify.template", phoneVerify_hk);
		
		readTemplate("template/sms/en_US/transfer.template", transfer_en);
		readTemplate("template/sms/zh_CN/transfer.template", transfer_cn);
		readTemplate("template/sms/zh_HK/transfer.template", transfer_hk);
		
		readTemplate("template/sms/zh_CN/criticalAlarm.template", criticalAlarm_cn);
	}
	
	private void readTemplate(String filePath, StringBuffer content) {
		try {
			content.setLength(0);
			Resource resource = new ClassPathResource(filePath);
			content.append(IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", ""));
		} catch (Exception e) {
			logger.warn("SMS template ({}) read error , can't send this msg : {} ", new Object[]{filePath, e.getMessage()});
		}
	}

	/**
	 * 发送验证码
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param code
	 */
	@Async
	public void sendSMS4PhoneVerify(String areaCode, String userPhone, String code) {
		String phoneVerifyContent = templateChoose("phoneVerify", areaCode);
		String content = phoneVerifyContent.replace(SMS_REPLACE_PIN, code).replace(SMS_REPLACE_TIME,
				configManager.getConfigStringValue(ConfigKeyEnum.VERIFYTIME, "10"));
		sendSMS(areaCode + userPhone, content);
	}

	/**
	 * 转账短信
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param from
	 * @param currency
	 * @param amount
	 */
	@Async
	public void sendSMS4Transfer(String areaCode, String userPhone, User from, String currency, BigDecimal amount) {
		String transferContent = templateChoose("transfer", areaCode);
		String content = transferContent.replace(SMS_REPLACE_FROM, from.getAreaCode() + from.getUserPhone())
				.replace(SMS_REPLACE_CURRENCY, commonManager.getCurreny(currency).getCurrencyUnit())
				.replace(SMS_REPLACE_AMOUNT,
						currency.equals(ServerConsts.CURRENCY_OF_GOLDPAY) ? new BigDecimal(amount.intValue()).toString() : amount.toString())
				.replace(SMS_REPLACE_LINK, configManager.getConfigStringValue(ConfigKeyEnum.DOWNLOADLINK, ""))
				.replace(SMS_REPLACE_TIME, configManager.getConfigStringValue(ConfigKeyEnum.REFUNTIME, "7"));
		sendSMS(areaCode + userPhone, content);
	}
	
	@Async
	public void sendSMS4CriticalAlarm(String phone,BigDecimal difference,BigDecimal lowerLimit,String dateTime){
		String transferContent = criticalAlarm_cn.toString();
		String content = transferContent
				.replace(SMS_REPLACE_DIFFERENCE,difference.toString())
				.replace(SMS_REPLACE_LOWERLIMIT,lowerLimit.toString())
				.replace(SMS_REPLACE_TIME,dateTime);
		sendSMS(phone, content);
	}

	/**
	 * 根据功能和国家码选择模板
	 * 
	 * @param func
	 * @param areaCode
	 * @return
	 */
	private String templateChoose(String func, String areaCode) {
		StringBuffer content;
		switch (func) {
		case "phoneVerify":
			if ("+86".equals(areaCode)) {
				content = phoneVerify_cn;
			} else if ("+852".equals(areaCode) || ("+853".equals(areaCode) || "+886".equals(areaCode))) {
				content = phoneVerify_hk;
			} else {
				content = phoneVerify_en;
			}
			break;
		case "transfer":
			if ("+86".equals(areaCode)) {
				content = transfer_cn;
			} else if ("+852".equals(areaCode) || ("+853".equals(areaCode) || "+886".equals(areaCode))) {
				content = transfer_hk;
			} else {
				content = transfer_en;
			}
			break;
		default:
			content = new StringBuffer();
		}
		return content.toString();
	}

	private void sendSMS(String phoneNum, String content) {
		logger.info("sendSMS , phoneNum : {} , content : {}", phoneNum, content);
		if (ResourceUtils.getBundleValue4Boolean("sms.switch") && StringUtils.isNotBlank(phoneNum) && StringUtils.isNotBlank(content)) {
			SendMessageRequest sendMessageRequest = new SendMessageRequest();
			sendMessageRequest.setTo(phoneNum);
			sendMessageRequest.setContent(content);
			sendMessageRequest.setAppId(ResourceUtils.getBundleValue4String("appId"));
			String param = JsonBinder.getInstance().toJson(sendMessageRequest);
			logger.info("sendMessageRequest : {}", param);
			HttpTookit.sendPost(ResourceUtils.getBundleValue4String("sendSMS.serverUrl"), param);
		}
	}

}

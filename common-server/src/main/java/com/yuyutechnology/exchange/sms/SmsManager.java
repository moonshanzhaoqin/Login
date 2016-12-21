/**
 * 
 */
package com.yuyutechnology.exchange.sms;

import java.io.IOException;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.utils.*;

/**
 * @author suzan.wu
 *
 */
@Service
public class SmsManager {
	public static Logger logger = LoggerFactory.getLogger(SmsManager.class);

	private final String SMS_REPLACE_PIN = "[PIN]";
	private final String SMS_REPLACE_TIME = "[TIME]";
	private final String SMS_REPLACE_FROM = "[FROM]";
	private final String SMS_REPLACE_CURRENCY = "[CURRENCY]";
	private final String SMS_REPLACE_AMOUNT = "[AMOUNT]";
	private final String SMS_REPLACE_LINK = "[LINK]";

	// en
	private String phoneVerify_en = "";
	// zh_CN
	private String phoneVerify_CN = "";
	// zh_HK
	private String phoneVerify_HK = "";

	// en
	private String transfer_en = "";
	// zh_CN
	private String transfer_CN = "";
	// zh_HK
	private String transfer_HK = "";

	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void init() throws IOException {
		// 加载模板
		// 验证码
		Resource resource = new ClassPathResource("sms/en_US/phoneVerify.template");
		phoneVerify_en = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("sms/zh_CN/phoneVerify.template");
		phoneVerify_CN = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("sms/zh_HK/phoneVerify.template");
		phoneVerify_HK = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		// 转账
		resource = new ClassPathResource("sms/en_US/transfer.template");
		transfer_en = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("sms/zh_CN/transfer.template");
		transfer_CN = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("sms/zh_HK/transfer.template");
		transfer_HK = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
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
				ResourceUtils.getBundleValue("verify.time"));
		sendSMS(areaCode + userPhone, content);
	}

	/**
	 * 转账短信
	 * 
	 * @param areaCode
	 * @param userPhone
	 * @param user
	 * @param currency
	 * @param amount
	 */
	@Async
	public void sendSMS4Transfer(String areaCode, String userPhone, User user, String currency, BigDecimal amount) {
		String transferContent = templateChoose("transfer", areaCode);
		String content = transferContent.replace(SMS_REPLACE_FROM, user.getUserName())
				.replace(SMS_REPLACE_CURRENCY, currency).replace(SMS_REPLACE_AMOUNT, amount.toString())
				.replace(SMS_REPLACE_LINK, ResourceUtils.getBundleValue("download.link"));
		sendSMS(areaCode + userPhone, content);
	}

	/**
	 * 根据功能和国家码选择模板
	 * 
	 * @param func
	 * @param areaCode
	 * @return
	 */
	private String templateChoose(String func, String areaCode) {
		String content;
		switch (func) {
		case "phoneVerify":
			if ("+86".equals(areaCode)) {
				content = phoneVerify_CN;
			} else if ("+852".equals(areaCode) || ("+853".equals(areaCode) || "+886".equals(areaCode))) {
				content = phoneVerify_HK;
			} else {
				content = phoneVerify_en;
			}
			break;
		case "transfer":
			if ("+86".equals(areaCode)) {
				content = transfer_CN;
			} else if ("+852".equals(areaCode) || ("+853".equals(areaCode) || "+886".equals(areaCode))) {
				content = transfer_HK;
			} else {
				content = transfer_en;
			}
			break;
		default:
			content = null;
		}
		return content;
	}

	@Async
	private void sendSMS(String phoneNum, String Content) {
		SendMessageRequest sendMessageRequest = new SendMessageRequest();
		sendMessageRequest.setTo(phoneNum);
		sendMessageRequest.setContent(Content);
		sendMessageRequest.setAppId(ResourceUtils.getBundleValue("appId"));
		String param = JsonBinder.getInstance().toJson(sendMessageRequest);
		logger.info("sendMessageRequest : {}", param);
		HttpTookit.sendPost(ResourceUtils.getBundleValue("sendSMS.serverUrl"), param);
	}

}

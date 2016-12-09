/**
 * 
 */
package com.yuyutechnology.exchange.sms;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.utils.*;

/**
 * @author suzan.wu
 *
 */
@Service
public class SmsManager {
	public static Logger logger = LoggerFactory.getLogger(SmsManager.class);

	private final String SMS_REPLACE_PIN = "[PIN]";

	// en
	private String phoneVerify_en = "";
	// zh_CN
	private String phoneVerify_CN = "";
	// zh_HK
	private String phoneVerify_HK = "";

	private String appId = "";
	private String sendURL = "";

	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void init() throws IOException {
		appId = ResourceUtils.getBundleValue("appId");
		sendURL = ResourceUtils.getBundleValue("sendSMS.serverUrl");
		// 加载模板
		Resource resource = new ClassPathResource("sms/en/phoneVerify.template");
		phoneVerify_en = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("sms/zh_CN/phoneVerify.template");
		phoneVerify_CN = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("sms/zh_HK/phoneVerify.template");
		phoneVerify_HK = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
	}

	@Async
	public void sendSMS4PhoneVerify(String areaCode, String userPhone, String code) {
		String phoneVerifyContent = templateChoose("phoneVerify", areaCode);
		String content = phoneVerifyContent.replace(SMS_REPLACE_PIN, code);
		sendSMS(areaCode+userPhone, content);
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
		sendMessageRequest.setAppId(appId);
		String param = JsonBinder.getInstance().toJson(sendMessageRequest);
		logger.info("sendMessageRequest : {}", param);
		HttpTookit.sendPost(sendURL, param);
	}

}

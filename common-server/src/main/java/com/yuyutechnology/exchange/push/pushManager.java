package com.yuyutechnology.exchange.push;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.LanguageUtils.Language;
import com.yuyutechnology.exchange.utils.ResourceUtils;

/**
 * 
 * @author suzan.wu
 *
 */
@Service
public class pushManager {
	public static Logger logger = LoggerFactory.getLogger(pushManager.class);
	private String appName = "";
	private String pushToAllURL = "";
	private String pushToCustomURL = "";
	private String pushToTagURL = "";
	// en
	private String transfer_en = "";
	// zh_CN
	private String transfer_CN = "";
	// zh_HK
	private String transfer_HK = "";

	private final String PUSH_REPLACE_FROM = "[FROM]";
	private final String PUSH_REPLACE_CURRENCY = "[CURRENCY]";
	private final String PUSH_REPLACE_AMOUNT = "[AMOUNT]";

	// @PostConstruct
	// @Scheduled(cron = "0 1/10 * * * ?")
	public void init() throws IOException {
		appName = ResourceUtils.getBundleValue("appName");
		pushToAllURL = ResourceUtils.getBundleValue("push.all.url");
		pushToCustomURL = ResourceUtils.getBundleValue("push.custom.url");
		pushToTagURL = ResourceUtils.getBundleValue("push.tag.url");

		// 加载模板
		Resource resource = new ClassPathResource("push/en_US/transfer.template");
		transfer_en = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_CN/transfer.template");
		transfer_CN = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_HK/transfer.template");
		transfer_HK = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
	}

	public void push(User userFrom, User userTo, Currency currency, BigDecimal amount) {
		String title = "转账提醒";
		String transferBody = templateChoose("transfer", userTo.getPushTag());
		String body = transferBody.replace(PUSH_REPLACE_FROM, userFrom.getUserName())
				.replace(PUSH_REPLACE_CURRENCY, currency.getCurrency()).replace(PUSH_REPLACE_AMOUNT, amount.toString());
		pushToCustom(userTo.getUserId(), userTo.getPushId(), title, body);
	}

	private String templateChoose(String func, Language pushTag) {
		String body = null;
		switch (func) {
		case "transfer":
			switch (pushTag) {
			case en_US:
				body = transfer_en;
				break;
			case zh_CN:
				body = transfer_CN;
				break;
			case zh_HK:
				body = transfer_HK;
				break;
			case zh_TW:
				body = transfer_HK;
				break;
			default:
				break;
			}
			break;
		default:
			body = null;
		}
		return body;
	}

	@Async
	private void pushToCustom(Integer userId, String deviceID, String title, String body) {
		PushToCustom pushToCustom = new PushToCustom();
		pushToCustom.setAppName(appName);
		pushToCustom.setBody(body);
		pushToCustom.setTitle(title);
		pushToCustom.setDeviceID(deviceID);
		pushToCustom.setUserId(userId.toString());
		String param = JsonBinder.getInstance().toJson(pushToCustom);
		logger.info("pushRequest : {}", param);
		HttpTookit.sendPost(pushToCustomURL, param);
	}

	@Async
	private void pushToTag() {
		// TODO Auto-generated method stub

	}

	@Async
	private void pushToAll() {
		// TODO Auto-generated method stub

	}

}

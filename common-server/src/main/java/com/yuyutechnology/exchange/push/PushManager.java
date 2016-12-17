package com.yuyutechnology.exchange.push;

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
public class PushManager {
	public static Logger logger = LoggerFactory.getLogger(PushManager.class);

	public enum Func {
		bindTag, unbindTag
	}

	private String appName = "";
	private String pushToAllURL = "";
	private String pushToCustomURL = "";
	private String pushToTagURL = "";
	private String pushBindTagURL = "";
	private String pushUnbindTagURL = "";
	private String day = "";

	// 到账提醒
	// en
	private String transfer_en = "";
	// zh_CN
	private String transfer_CN = "";
	// zh_HK
	private String transfer_HK = "";

	// 请求转账
	// en
	private String transfer_request_en = "";
	// zh_CN
	private String transfer_request_CN = "";
	// zh_HK
	private String transfer_request_HK = "";

	// 退款refund
	// en
	private String refund_en = "";
	// zh_CN
	private String refund_CN = "";
	// zh_HK
	private String refund_HK = "";

	// 退款offline
	// en
	private String offline_en = "";
	// zh_CN
	private String offline_CN = "";
	// zh_HK
	private String offline_HK = "";

	private final String PUSH_REPLACE_FROM = "[FROM]";
	private final String PUSH_REPLACE_TO = "[TO]";
	private final String PUSH_REPLACE_CURRENCY = "[CURRENCY]";
	private final String PUSH_REPLACE_AMOUNT = "[AMOUNT]";
	private final String PUSH_REPLACE_DAY = "[DAY]";

	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void init() throws IOException {
		appName = ResourceUtils.getBundleValue("appName");
		pushToAllURL = ResourceUtils.getBundleValue("push.all.url");
		pushToCustomURL = ResourceUtils.getBundleValue("push.custom.url");
		pushToTagURL = ResourceUtils.getBundleValue("push.tag.url");
		pushBindTagURL = ResourceUtils.getBundleValue("push.bind.tag.url");
		pushUnbindTagURL = ResourceUtils.getBundleValue("push.unbind.tag.url");

		day = ResourceUtils.getBundleValue("refund.time");

		// 加载模板
		// 到账提醒
		Resource resource = new ClassPathResource("push/en_US/transfer.template");
		transfer_en = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_CN/transfer.template");
		transfer_CN = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_HK/transfer.template");
		transfer_HK = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		// 请求转账
		resource = new ClassPathResource("push/en_US/transfer_request.template");
		transfer_request_en = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_CN/transfer_request.template");
		transfer_request_CN = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_HK/transfer_request.template");
		transfer_request_HK = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		// 退款refund
		resource = new ClassPathResource("push/en_US/refund.template");
		refund_en = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_CN/refund.template");
		refund_CN = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_HK/refund.template");
		refund_HK = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		// 退款offline
		resource = new ClassPathResource("push/en_US/offline.template");
		offline_en = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_CN/offline.template");
		offline_CN = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_HK/offline.template");
		offline_HK = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
	}

	/**
	 * 到账通知
	 * 
	 * @param userFrom
	 * @param userTo
	 * @param currency
	 * @param amount
	 */
	public void push4Transfer(User userFrom, User userTo, String currency, BigDecimal amount) {
		String title = "到账通知";
		String transferBody = templateChoose("transfer", userTo.getPushTag());
		String body = transferBody.replace(PUSH_REPLACE_FROM, userFrom.getUserName())
				.replace(PUSH_REPLACE_CURRENCY, currency).replace(PUSH_REPLACE_AMOUNT, amount.toString());
		pushToCustom(userTo.getUserId(), userTo.getPushId(), title, body);
	}

	/**
	 * 转账请求通知
	 * 
	 * @param userFrom
	 * @param userTo
	 * @param currency
	 * @param amount
	 */
	public void push4TransferRuquest(User userFrom, User userTo, String currency, BigDecimal amount) {
		String title = "转账请求";
		String transferBody = templateChoose("transfer_request", userFrom.getPushTag());
		String body = transferBody.replace(PUSH_REPLACE_TO, userTo.getUserName())
				.replace(PUSH_REPLACE_CURRENCY, currency).replace(PUSH_REPLACE_AMOUNT, amount.toString());
		pushToCustom(userFrom.getUserId(), userFrom.getPushId(), title, body);
	}

	/**
	 * 退款refund
	 * 
	 * @param userFrom
	 * @param userTo
	 * @param currency
	 * @param amount
	 */
	public void push4Refund(User userFrom, String areaCode, String phone, String currency, BigDecimal amount) {
		String title = "退款通知";
		String refundBody = templateChoose("refund", userFrom.getPushTag());
		String body = refundBody.replace(PUSH_REPLACE_TO, areaCode + phone).replace(PUSH_REPLACE_CURRENCY, currency)
				.replace(PUSH_REPLACE_AMOUNT, amount.toString()).replace(PUSH_REPLACE_DAY, day);
		pushToCustom(userFrom.getUserId(), userFrom.getPushId(), title, body);
	}

	// Offline 下线消息
	public void push4Offline(User user) {
		String title = "下线消息";
		String offlineBody = templateChoose("offline", user.getPushTag());
		String body = offlineBody;
		pushToCustom(user.getUserId(), user.getPushId(), title, body);
	}

	/**
	 * 绑定Tag
	 */
	public void bindPushTag(User user) {
		tag(Func.bindTag, user.getPushId(), user.getPushTag().toString());
	}

	/**
	 * 解绑 Tag
	 * 
	 * @param user
	 */
	public void unbindPushTag(User user) {
		tag(Func.unbindTag, user.getPushId(), user.getPushTag().toString());
	}

	/**
	 * 根据功能和语言选择模板
	 * 
	 * @param func
	 * @param pushTag
	 * @return
	 */

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
		case "transfer_request":
			switch (pushTag) {
			case en_US:
				body = transfer_request_en;
				break;
			case zh_CN:
				body = transfer_request_CN;
				break;
			case zh_HK:
				body = transfer_request_HK;
				break;
			case zh_TW:
				body = transfer_request_HK;
				break;
			default:
				break;
			}
			break;
		case "refund":
			switch (pushTag) {
			case en_US:
				body = refund_en;
				break;
			case zh_CN:
				body = refund_CN;
				break;
			case zh_HK:
				body = refund_HK;
				break;
			case zh_TW:
				body = refund_HK;
				break;
			default:
				break;
			}
			break;
		case "offline":
			switch (pushTag) {
			case en_US:
				body = offline_en;
				break;
			case zh_CN:
				body = offline_CN;
				break;
			case zh_HK:
				body = offline_HK;
				break;
			case zh_TW:
				body = offline_HK;
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
	private void tag(Func func, String deviceID, String pushTag) {
		TagRequest tagRequest = new TagRequest();
		tagRequest.setAppName(appName);
		tagRequest.setDeviceIds(deviceID);
		tagRequest.setTagName(pushTag);
		String param = JsonBinder.getInstance().toJson(tagRequest);
		logger.info("TagRequest : {}", param);
		switch (func) {
		case bindTag:
			HttpTookit.sendPost(pushBindTagURL, param);
			break;
		case unbindTag:
			HttpTookit.sendPost(pushUnbindTagURL, param);
			break;
		default:
			break;
		}

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
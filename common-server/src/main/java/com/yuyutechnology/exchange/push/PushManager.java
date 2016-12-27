package com.yuyutechnology.exchange.push;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

import com.yuyutechnology.exchange.manager.CommonManager;
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

	@Autowired
	CommonManager commonManager;

	public enum Func {
		bindTag, unbindTag
	}

	// 到账提醒
	// en_US
	private String transfer_title_en = "";
	// zh_CN
	private String transfer_title_CN = "";
	// zh_HK
	private String transfer_title_HK = "";
	// en_US
	private String transfer_en = "";
	// zh_CN
	private String transfer_CN = "";
	// zh_HK
	private String transfer_HK = "";

	// 请求转账
	// en_US
	private String transfer_request_title_en = "";
	// zh_CN
	private String transfer_request_title_CN = "";
	// zh_HK
	private String transfer_request_title_HK = "";
	// en_US
	private String transfer_request_en = "";
	// zh_CN
	private String transfer_request_CN = "";
	// zh_HK
	private String transfer_request_HK = "";

	// 退款refund
	// en_US
	private String refund_title_en = "";
	// zh_CN
	private String refund_title_CN = "";
	// zh_HK
	private String refund_title_HK = "";
	// en_US
	private String refund_en = "";
	// zh_CN
	private String refund_CN = "";
	// zh_HK
	private String refund_HK = "";

	// 下线offline
	// en_US
	private String offline_title_en = "";
	// zh_CN
	private String offline_title_CN = "";
	// zh_HK
	private String offline_title_HK = "";
	// en_US
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
		logger.info("==========init PushManager==========");
		// 加载模板
		// 到账提醒
		Resource resource = new ClassPathResource("push/en_US/transfer.template");
		String string = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
		transfer_title_en = string.substring(0, string.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", "");
		transfer_en = string.substring(string.indexOf("\n")).replaceAll("\n", "").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_CN/transfer.template");
		string = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
		transfer_title_CN = string.substring(0, string.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", "");
		transfer_CN = string.substring(string.indexOf("\n")).replaceAll("\n", "").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_HK/transfer.template");
		string = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
		transfer_title_HK = string.substring(0, string.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", "");
		transfer_HK = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		// 请求转账
		resource = new ClassPathResource("push/en_US/transfer_request.template");
		string = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
		transfer_request_title_en = string.substring(0, string.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r",
				"");
		transfer_request_en = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_CN/transfer_request.template");
		string = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
		transfer_request_title_CN = string.substring(0, string.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r",
				"");
		transfer_request_CN = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_HK/transfer_request.template");
		string = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
		transfer_request_title_HK = string.substring(0, string.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r",
				"");
		transfer_request_HK = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		// 退款refund
		resource = new ClassPathResource("push/en_US/refund.template");
		string = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
		refund_title_en = string.substring(0, string.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", "");
		refund_en = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_CN/refund.template");
		refund_title_CN = string.substring(0, string.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", "");
		refund_CN = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_HK/refund.template");
		refund_title_HK = string.substring(0, string.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", "");
		refund_HK = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		// 下线offline
		resource = new ClassPathResource("push/en_US/offline.template");
		string = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
		offline_title_en = string.substring(0, string.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", "");
		offline_en = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_CN/offline.template");
		string = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
		offline_title_CN = string.substring(0, string.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", "");
		offline_CN = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_HK/offline.template");
		string = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
		offline_title_HK = string.substring(0, string.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", "");
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
	@Async
	public void push4Transfer(User userFrom, User userTo, String currency, BigDecimal amount) {
		String title = titleChoose("transfer", userTo.getPushTag());
		String transferBody = templateChoose("transfer", userTo.getPushTag());
		String body = transferBody.replace(PUSH_REPLACE_FROM, userFrom.getUserName())
				.replace(PUSH_REPLACE_CURRENCY, currency).replace(PUSH_REPLACE_AMOUNT, amount.toString());
		Map<String, String> ext = new HashMap<>();
		ext.put("type", "transfer");
		pushToCustom(userTo.getUserId(), userTo.getPushId(), title, body, JsonBinder.getInstance().toJson(ext));

		// 新请求转账标记
		commonManager.addMsgFlag(userTo.getUserId(), 1);
	}

	/**
	 * 转账请求通知
	 * 
	 * @param userFrom
	 * @param userTo
	 * @param currency
	 * @param amount
	 */
	@Async
	public void push4TransferRuquest(User userFrom, User userTo, String currency, BigDecimal amount) {
		String title = titleChoose("transfer_request", userTo.getPushTag());
		String transferRuquestBody = templateChoose("transfer_request", userFrom.getPushTag());
		String body = transferRuquestBody.replace(PUSH_REPLACE_TO, userTo.getUserName());
		Map<String, String> ext = new HashMap<>();
		ext.put("type", "transfer_request");
		pushToCustom(userFrom.getUserId(), userFrom.getPushId(), title, body, JsonBinder.getInstance().toJson(ext));

		// 新请求转账标记
		commonManager.addMsgFlag(userFrom.getUserId(), 0);
	}

	/**
	 * 退款refund
	 * 
	 * @param userFrom
	 * @param userTo
	 * @param currency
	 * @param amount
	 */
	@Async
	public void push4Refund(User userFrom, String areaCode, String phone, String currency, BigDecimal amount) {
		String title = titleChoose("refund", userFrom.getPushTag());
		String refundBody = templateChoose("refund", userFrom.getPushTag());
		String body = refundBody.replace(PUSH_REPLACE_TO, areaCode + phone).replace(PUSH_REPLACE_CURRENCY, currency)
				.replace(PUSH_REPLACE_AMOUNT, amount.toString())
				.replace(PUSH_REPLACE_DAY, ResourceUtils.getBundleValue4String("refund.time"));
		Map<String, String> ext = new HashMap<>();
		ext.put("type", "refund");
		pushToCustom(userFrom.getUserId(), userFrom.getPushId(), title, body, JsonBinder.getInstance().toJson(ext));
	}

	/**
	 * offline 下线消息
	 * 
	 * @param user
	 */
	@Async
	public void push4Offline(User user, String date) {
		String title = titleChoose("offline", user.getPushTag());
		String offlineBody = templateChoose("offline", user.getPushTag());
		String body = offlineBody;
		Map<String, String> ext = new HashMap<>();
		ext.put("type", "offline");
		ext.put("time", date);
		pushToCustom(user.getUserId(), user.getPushId(), title, body, JsonBinder.getInstance().toJson(ext));
	}

	/**
	 * 绑定Tag
	 * 
	 * @param user
	 */
	@Async
	public void bindPushTag(User user) {
		tag(Func.bindTag, user.getPushId(), user.getPushTag().toString());
	}

	/**
	 * 解绑 Tag
	 * 
	 * @param user
	 */
	@Async
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

	/**
	 * 根据功能和语言选择标题
	 * 
	 * @param func
	 * @param pushTag
	 * @return
	 */
	private String titleChoose(String func, Language pushTag) {
		String title = null;
		switch (func) {
		case "transfer":
			switch (pushTag) {
			case en_US:
				title = transfer_title_en;
				break;
			case zh_CN:
				title = transfer_title_CN;
				break;
			case zh_TW:
				title = transfer_title_HK;
				break;
			default:
				break;
			}
			break;
		case "transfer_request":
			switch (pushTag) {
			case en_US:
				title = transfer_request_title_en;
				break;
			case zh_CN:
				title = transfer_request_title_CN;
				break;
			case zh_TW:
				title = transfer_request_title_HK;
				break;
			default:
				break;
			}
			break;
		case "refund":
			switch (pushTag) {
			case en_US:
				title = refund_title_en;
				break;
			case zh_CN:
				title = refund_title_CN;
				break;
			case zh_TW:
				title = refund_title_HK;
				break;
			default:
				break;
			}
			break;
		case "offline":
			switch (pushTag) {
			case en_US:
				title = offline_title_en;
				break;
			case zh_CN:
				title = offline_title_CN;
				break;
			case zh_TW:
				title = offline_title_HK;
				break;
			default:
				break;
			}
			break;
		default:
			title = null;
		}
		return title;
	}

	private void pushToCustom(Integer userId, String deviceID, String title, String body, String extParameters) {
		if (StringUtils.isBlank(deviceID)) {
			return;
		}
		PushToCustom pushToCustom = new PushToCustom();
		pushToCustom.setAppName(ResourceUtils.getBundleValue4String("appName"));
		pushToCustom.setBody(body);
		pushToCustom.setTitle(title);
		pushToCustom.setDeviceID(deviceID);
		pushToCustom.setUserId(userId.toString());
		pushToCustom.setExtParameters(extParameters);
		String param = JsonBinder.getInstance().toJson(pushToCustom);
		logger.info("pushRequest : {}", param);
		HttpTookit.sendPost(ResourceUtils.getBundleValue4String("push.url") + "push_custom.do", param);
	}

	private void tag(Func func, String deviceID, String pushTag) {
		if (StringUtils.isBlank(deviceID)) {
			return;
		}
		TagRequest tagRequest = new TagRequest();
		tagRequest.setAppName(ResourceUtils.getBundleValue4String("appName"));
		tagRequest.setDeviceIds(deviceID);
		tagRequest.setTagName(pushTag);
		String param = JsonBinder.getInstance().toJson(tagRequest);
		logger.info("TagRequest : {}", param);
		switch (func) {
		case bindTag:
			HttpTookit.sendPost(ResourceUtils.getBundleValue4String("push.url") + "bindTag.do", param);
			break;
		case unbindTag:
			HttpTookit.sendPost(ResourceUtils.getBundleValue4String("push.url") + "unbindTag.do", param);
			break;
		default:
			break;
		}
	}

}

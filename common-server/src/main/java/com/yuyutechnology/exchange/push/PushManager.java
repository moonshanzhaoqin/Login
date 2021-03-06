package com.yuyutechnology.exchange.push;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.yuyutechnology.exchange.util.HttpClientUtils;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.LanguageUtils.Language;
import com.yuyutechnology.exchange.util.ResourceUtils;

/**
 * 
 * @author suzan.wu
 *
 */
@Service
public class PushManager {
	public static Logger logger = LogManager.getLogger(PushManager.class);
	public static DecimalFormat CURRENCY = new DecimalFormat(",##0.00");
	public static DecimalFormat GDQ = new DecimalFormat(",##0");
	private boolean initPush = false;
	@Autowired
	CommonManager commonManager;
	@Autowired
	ConfigManager configManager;

	public enum Func {
		bindTag, unbindTag
	}

	// 到账提醒
	// en_US
	private StringBuffer transfer_title_en = new StringBuffer();
	// zh_CN
	private StringBuffer transfer_title_cn = new StringBuffer();
	// zh_HK
	private StringBuffer transfer_title_hk = new StringBuffer();
	// en_US
	private StringBuffer transfer_en = new StringBuffer();
	// zh_CN
	private StringBuffer transfer_cn = new StringBuffer();
	// zh_HK
	private StringBuffer transfer_hk = new StringBuffer();

	// 请求转账
	// en_US
	private StringBuffer transfer_request_title_en = new StringBuffer();
	// zh_CN
	private StringBuffer transfer_request_title_cn = new StringBuffer();
	// zh_HK
	private StringBuffer transfer_request_title_hk = new StringBuffer();
	// en_US
	private StringBuffer transfer_request_en = new StringBuffer();
	// zh_CN
	private StringBuffer transfer_request_cn = new StringBuffer();
	// zh_HK
	private StringBuffer transfer_request_hk = new StringBuffer();

	// 退款refund
	// en_US
	private StringBuffer refund_title_en = new StringBuffer();
	// zh_CN
	private StringBuffer refund_title_cn = new StringBuffer();
	// zh_hk
	private StringBuffer refund_title_hk = new StringBuffer();
	// en_us
	private StringBuffer refund_en = new StringBuffer();
	// zh_cn
	private StringBuffer refund_cn = new StringBuffer();
	// zh_hk
	private StringBuffer refund_hk = new StringBuffer();

	// 下线offline
	// en_us
	private StringBuffer offline_title_en = new StringBuffer();
	// zh_cn
	private StringBuffer offline_title_cn = new StringBuffer();
	// zh_hk
	private StringBuffer offline_title_hk = new StringBuffer();
	// en_us
	private StringBuffer offline_en = new StringBuffer();
	// zh_cn
	private StringBuffer offline_cn = new StringBuffer();
	// zh_hk
	private StringBuffer offline_hk = new StringBuffer();

	/* 提现成功 withdraw_complete */
//	// en_us
//	private StringBuffer withdraw_complete_title_en = new StringBuffer();
//	// zh_cn
//	private StringBuffer withdraw_complete_title_cn = new StringBuffer();
//	// zh_hk
//	private StringBuffer withdraw_complete_title_hk = new StringBuffer();
//	// en_us
//	private StringBuffer withdraw_complete_en = new StringBuffer();
//	// zh_cn
//	private StringBuffer withdraw_complete_cn = new StringBuffer();
//	// zh_hk
//	private StringBuffer withdraw_complete_hk = new StringBuffer();

	/* 提现退回 withdraw_refund */
//	// en_us
//	private StringBuffer withdraw_refund_title_en = new StringBuffer();
//	// zh_cn
//	private StringBuffer withdraw_refund_title_cn = new StringBuffer();
//	// zh_hk
//	private StringBuffer withdraw_refund_title_hk = new StringBuffer();
//	// en_us
//	private StringBuffer withdraw_refund_en = new StringBuffer();
//	// zh_cn
//	private StringBuffer withdraw_refund_cn = new StringBuffer();
//	// zh_hk
//	private StringBuffer withdraw_refund_hk = new StringBuffer();
//
	/* 邀请活动奖励 invite */
	// en_us
	private StringBuffer invite_title_en = new StringBuffer();
	// zh_cn
	private StringBuffer invite_title_cn = new StringBuffer();
	// zh_hk
	private StringBuffer invite_title_hk = new StringBuffer();
	// en_us
	private StringBuffer invite_en = new StringBuffer();
	// zh_cn
	private StringBuffer invite_cn = new StringBuffer();
	// zh_hk
	private StringBuffer invite_hk = new StringBuffer();

	private final String PUSH_REPLACE_FROM = "[FROM]";
	private final String PUSH_REPLACE_TO = "[TO]";
	private final String PUSH_REPLACE_CURRENCY = "[CURRENCY]";
	private final String PUSH_REPLACE_AMOUNT = "[AMOUNT]";
	private final String PUSH_REPLACE_DAY = "[DAY]";

	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void init() {
		logger.info("==========init PushManager==========");

		readTemplate("template/push/en_US/transfer.template", transfer_title_en, transfer_en);
		readTemplate("template/push/zh_CN/transfer.template", transfer_title_cn, transfer_cn);
		readTemplate("template/push/zh_HK/transfer.template", transfer_title_hk, transfer_hk);

		readTemplate("template/push/en_US/transfer_request.template", transfer_request_title_en, transfer_request_en);
		readTemplate("template/push/zh_CN/transfer_request.template", transfer_request_title_cn, transfer_request_cn);
		readTemplate("template/push/zh_HK/transfer_request.template", transfer_request_title_hk, transfer_request_hk);

		readTemplate("template/push/en_US/refund.template", refund_title_en, refund_en);
		readTemplate("template/push/zh_CN/refund.template", refund_title_cn, refund_cn);
		readTemplate("template/push/zh_HK/refund.template", refund_title_hk, refund_hk);

		readTemplate("template/push/en_US/offline.template", offline_title_en, offline_en);
		readTemplate("template/push/zh_CN/offline.template", offline_title_cn, offline_cn);
		readTemplate("template/push/zh_HK/offline.template", offline_title_hk, offline_hk);

//		readTemplate("template/push/en_US/withdraw_complete.template", withdraw_complete_title_en,
//				withdraw_complete_en);
//		readTemplate("template/push/zh_CN/withdraw_complete.template", withdraw_complete_title_cn,
//				withdraw_complete_cn);
//		readTemplate("template/push/zh_HK/withdraw_complete.template", withdraw_complete_title_hk,
//				withdraw_complete_hk);
//
//		readTemplate("template/push/en_US/withdraw_refund.template", withdraw_refund_title_en, withdraw_refund_en);
//		readTemplate("template/push/zh_CN/withdraw_refund.template", withdraw_refund_title_cn, withdraw_refund_cn);
//		readTemplate("template/push/zh_HK/withdraw_refund.template", withdraw_refund_title_hk, withdraw_refund_hk);

		readTemplate("template/push/en_US/invite.template", invite_title_en, invite_en);
		readTemplate("template/push/zh_CN/invite.template", invite_title_cn, invite_cn);
		readTemplate("template/push/zh_HK/invite.template", invite_title_hk, invite_hk);

		initPush = true;
	}

	private void readTemplate(String filePath, StringBuffer tital, StringBuffer content) {
		try {
			tital.setLength(0);
			content.setLength(0);
			Resource resource = new ClassPathResource(filePath);
			String fileString = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
			tital.append(
					fileString.substring(0, fileString.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", ""));
			content.append(fileString.substring(fileString.indexOf("\n")).replaceAll("\n", "").replaceAll("\r", ""));
		} catch (Exception e) {
			if (!initPush)
				logger.warn("push template ({}) read error , can't push this msg : {} ",
						new Object[] { filePath, e.getMessage() });
		}
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
	public void push4Transfer(String transferId, User userFrom, User userTo, String currency, BigDecimal amount) {
		String title = titleChoose("transfer", userTo.getPushTag());
		String transferBody = templateChoose("transfer", userTo.getPushTag());
		String body = transferBody.replace(PUSH_REPLACE_FROM, userFrom.getUserName())
				.replace(PUSH_REPLACE_CURRENCY, commonManager.getCurreny(currency).getCurrencyUnit())
				.replace(PUSH_REPLACE_AMOUNT, currency.equals(ServerConsts.CURRENCY_OF_GOLDPAY) ? GDQ.format(amount)
						: CURRENCY.format(amount));
		Map<String, String> ext = new HashMap<>();
		ext.put("type", "transfer");
		ext.put("transferId", transferId);
		ext.put("userId", userTo.getUserId().toString());
		pushToCustom(userTo.getPushId(), title, body, JsonBinder.getInstance().toJson(ext));

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
		String title = titleChoose("transfer_request", userFrom.getPushTag());
		String transferRuquestBody = templateChoose("transfer_request", userFrom.getPushTag());
		String body = transferRuquestBody.replace(PUSH_REPLACE_TO, userTo.getUserName());
		Map<String, String> ext = new HashMap<>();
		ext.put("type", "transfer_request");
		pushToCustom(userFrom.getPushId(), title, body, JsonBinder.getInstance().toJson(ext));

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
		logger.info("refund,{}=={}", userFrom.getPushTag(), refundBody);
		String body = refundBody.replace(PUSH_REPLACE_TO, areaCode + " " + phone)
				.replace(PUSH_REPLACE_CURRENCY, commonManager.getCurreny(currency).getCurrencyUnit())
				.replace(PUSH_REPLACE_AMOUNT,
						currency.equals(ServerConsts.CURRENCY_OF_GOLDPAY) ? GDQ.format(amount)
								: CURRENCY.format(amount))
				.replace(PUSH_REPLACE_DAY, configManager.getConfigStringValue(ConfigKeyEnum.REFUNTIME, "7"));
		Map<String, String> ext = new HashMap<>();
		ext.put("type", "refund");

		pushToCustom(userFrom.getPushId(), title, body, JsonBinder.getInstance().toJson(ext));
	}

	/**
	 * offline 下线消息
	 * 
	 * @param user
	 */
	@Async
	public void push4Offline(String pushId, Language pushTag, String date) {
		String title = titleChoose("offline", pushTag);
		String offlineBody = templateChoose("offline", pushTag);
		String body = offlineBody;
		Map<String, String> ext = new HashMap<>();
		ext.put("type", "offline");
		ext.put("time", date);
		pushToCustom(pushId, title, body, JsonBinder.getInstance().toJson(ext));
	}

	/**
	 * 提现成功
	 * 
	 * @param pushId
	 * @param pushTag
	 * @param amount
	 */
//	@Async
//	public void push4WithdrawComplete(String pushId, Language pushTag, BigDecimal amount) {
//		String title = titleChoose("withdraw_complete", pushTag);
//		String offlineBody = templateChoose("withdraw_complete", pushTag);
//		String body = offlineBody.replace(PUSH_REPLACE_AMOUNT, GDQ.format(amount));
//		Map<String, String> ext = new HashMap<>();
//		ext.put("type", "withdraw_complete");
//		pushToCustom(pushId, title, body, JsonBinder.getInstance().toJson(ext));
//	}

	/**
	 * 提现退回
	 * 
	 * @param pushId
	 * @param pushTag
	 * @param amount
	 */
//	@Async
//	public void push4WithdrawRefund(String pushId, Language pushTag, BigDecimal amount) {
//		String title = titleChoose("withdraw_refund", pushTag);
//		String offlineBody = templateChoose("withdraw_refund", pushTag);
//		String body = offlineBody.replace(PUSH_REPLACE_AMOUNT, GDQ.format(amount));
//		Map<String, String> ext = new HashMap<>();
//		ext.put("type", "withdraw_refund");
//		pushToCustom(pushId, title, body, JsonBinder.getInstance().toJson(ext));
//	}

	/**
	 * 邀请奖励金
	 * 
	 * @param user
	 * @param transferId
	 * @param amount
	 */
	@Async
	public void push4Invite(User user, String transferId, BigDecimal amount) {
		String title = titleChoose("invite", user.getPushTag());
		String offlineBody = templateChoose("invite", user.getPushTag());
		String body = offlineBody.replace(PUSH_REPLACE_AMOUNT, GDQ.format(amount));
		Map<String, String> ext = new HashMap<>();
		ext.put("type", "transfer");
		ext.put("transferId", transferId);
		ext.put("userId", user.getUserId().toString());
		pushToCustom(user.getPushId(), title, body, JsonBinder.getInstance().toJson(ext));
	}

	/**
	 * 绑定Tag
	 * 
	 * @param user
	 */
	@Async
	public void bindPushTag(String pushId, Language pushTag) {
		switch (pushTag) {
		case en_US:
			tag(Func.unbindTag, pushId, Language.zh_CN);
			tag(Func.unbindTag, pushId, Language.zh_TW);
			tag(Func.bindTag, pushId, Language.en_US);
			return;
		case zh_CN:
			tag(Func.unbindTag, pushId, Language.en_US);
			tag(Func.unbindTag, pushId, Language.zh_TW);
			tag(Func.bindTag, pushId, Language.zh_CN);
			return;
		case zh_TW:
			tag(Func.unbindTag, pushId, Language.zh_CN);
			tag(Func.unbindTag, pushId, Language.en_US);
			tag(Func.bindTag, pushId, Language.zh_TW);
			return;
		default:
			return;
		}
	}

	/**
	 * 解绑 Tag
	 * 
	 * @param user
	 */
	// @Async
	// public void unbindPushTag(String pushId, Language pushTag) {
	// tag(Func.unbindTag, pushId, pushTag.toString());
	// }

	/**
	 * 根据功能和语言选择模板
	 * 
	 * @param func
	 * @param pushTag
	 * @return
	 */

	private String templateChoose(String func, Language pushTag) {
		StringBuffer body = null;
		switch (func) {
		case "transfer":
			switch (pushTag) {
			case en_US:
				body = transfer_en;
				break;
			case zh_CN:
				body = transfer_cn;
				break;
			case zh_TW:
				body = transfer_hk;
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
				body = transfer_request_cn;
				break;
			case zh_TW:
				body = transfer_request_hk;
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
				body = refund_cn;
				break;
			case zh_TW:
				body = refund_hk;
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
				body = offline_cn;
				break;
			case zh_TW:
				body = offline_hk;
				break;
			default:
				break;
			}
			break;
//		case "withdraw_complete":
//			switch (pushTag) {
//			case en_US:
//				body = withdraw_complete_en;
//				break;
//			case zh_CN:
//				body = withdraw_complete_cn;
//				break;
//			case zh_TW:
//				body = withdraw_complete_hk;
//				break;
//			default:
//				break;
//			}
//			break;
//		case "withdraw_refund":
//			switch (pushTag) {
//			case en_US:
//				body = withdraw_refund_en;
//				break;
//			case zh_CN:
//				body = withdraw_refund_cn;
//				break;
//			case zh_TW:
//				body = withdraw_refund_hk;
//				break;
//			default:
//				break;
//			}
//			break;
		case "invite":
			switch (pushTag) {
			case en_US:
				body = invite_en;
				break;
			case zh_CN:
				body = invite_cn;
				break;
			case zh_TW:
				body = invite_hk;
				break;
			default:
				break;
			}
			break;
		default:
			body = new StringBuffer();
		}
		return body.toString();
	}

	/**
	 * 根据功能和语言选择标题
	 * 
	 * @param func
	 * @param pushTag
	 * @return
	 */
	private String titleChoose(String func, Language pushTag) {
		StringBuffer title = null;
		switch (func) {
		case "transfer":
			switch (pushTag) {
			case en_US:
				title = transfer_title_en;
				break;
			case zh_CN:
				title = transfer_title_cn;
				break;
			case zh_TW:
				title = transfer_title_hk;
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
				title = transfer_request_title_cn;
				break;
			case zh_TW:
				title = transfer_request_title_hk;
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
				title = refund_title_cn;
				break;
			case zh_TW:
				title = refund_title_hk;
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
				title = offline_title_cn;
				break;
			case zh_TW:
				title = offline_title_hk;
				break;
			default:
				break;
			}
			break;
//		case "withdraw_complete":
//			switch (pushTag) {
//			case en_US:
//				title = withdraw_complete_title_en;
//				break;
//			case zh_CN:
//				title = withdraw_complete_title_cn;
//				break;
//			case zh_TW:
//				title = withdraw_complete_title_hk;
//				break;
//			default:
//				break;
//			}
//			break;
//		case "withdraw_refund":
//			switch (pushTag) {
//			case en_US:
//				title = withdraw_refund_title_en;
//				break;
//			case zh_CN:
//				title = withdraw_refund_title_cn;
//				break;
//			case zh_TW:
//				title = withdraw_refund_title_hk;
//				break;
//			default:
//				break;
//			}
//			break;
		case "invite":
			switch (pushTag) {
			case en_US:
				title = invite_title_en;
				break;
			case zh_CN:
				title = invite_title_cn;
				break;
			case zh_TW:
				title = invite_title_hk;
				break;
			default:
				break;
			}
			break;
		default:
			title = new StringBuffer();
		}
		return title.toString();
	}

	private void pushToCustom(String deviceID, String title, String body, String extParameters) {
		if (StringUtils.isBlank(deviceID) || StringUtils.isBlank(body)) {
			return;
		}
		PushToCustom pushToCustom = new PushToCustom();
		pushToCustom.setAppName(ResourceUtils.getBundleValue4String("appName"));
		pushToCustom.setBody(body);
		pushToCustom.setTitle(title);
		pushToCustom.setDeviceID(deviceID);
		pushToCustom.setExtParameters(extParameters);
		String param = JsonBinder.getInstance().toJson(pushToCustom);
		logger.info("pushRequest : {}", param);
		HttpClientUtils.sendPost(ResourceUtils.getBundleValue4String("push.url") + "push_custom.do", param);
	}

	private void tag(Func func, String deviceID, Language pushTag) {
		if (StringUtils.isBlank(deviceID)) {
			return;
		}
		TagRequest tagRequest = new TagRequest();
		tagRequest.setAppName(ResourceUtils.getBundleValue4String("appName"));
		tagRequest.setDeviceIds(deviceID);
		tagRequest.setTagName(pushTag.toString());
		String param = JsonBinder.getInstance().toJson(tagRequest);
		logger.info("{} -> TagRequest : {}", func, param);
		switch (func) {
		case bindTag:
			HttpClientUtils.sendPost(ResourceUtils.getBundleValue4String("push.url") + "bindTag.do", param);
			return;
		case unbindTag:
			HttpClientUtils.sendPost(ResourceUtils.getBundleValue4String("push.url") + "unBindTag.do", param);
			return;
		default:
			return;
		}
	}

}

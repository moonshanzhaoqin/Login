/**
 * 
 */
package com.yuyutechnology.exchange.mail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dto.NotifyWithdrawDTO;
import com.yuyutechnology.exchange.util.DateFormatUtils;
import com.yuyutechnology.exchange.util.HttpClientUtils;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.ResourceUtils;

/**
 * @author suzan.wu
 *
 */
@Service
public class MailManager {
	public static Logger logger = LogManager.getLogger(MailManager.class);
	private boolean initMail = false;

	private StringBuffer contactTital = new StringBuffer();
	private StringBuffer contactContent = new StringBuffer();

	private StringBuffer criticalAlarmTital = new StringBuffer();
	private StringBuffer criticalAlarmContent = new StringBuffer();

	private StringBuffer largeTransWarnTital = new StringBuffer();
	private StringBuffer largeTransWarnContent = new StringBuffer();

	private StringBuffer largeExchangeWarnTital = new StringBuffer();
	private StringBuffer largeExchangeWarnContent = new StringBuffer();

	private StringBuffer badAccountWarnTital = new StringBuffer();
	private StringBuffer badAccountWarnContent = new StringBuffer();

	private StringBuffer registrationWarnTital = new StringBuffer();
	private StringBuffer registrationWarnContent = new StringBuffer();

	// private StringBuffer remitFailWarnTital = new StringBuffer();
	// private StringBuffer remitFailWarnContent = new StringBuffer();

	private StringBuffer totalGDQWarnTital = new StringBuffer();
	private StringBuffer totalGDQWarnContent = new StringBuffer();

	private StringBuffer notifyWithdrawTital = new StringBuffer();
	private StringBuffer notifyWithdrawContent = new StringBuffer();

	private final String MAIL_REPLACE_EMAIL = "[EMAIL]";
	private final String MAIL_REPLACE_NAME = "[NAME]";
	private final String MAIL_REPLACE_CATEGORY = "[CATEGORY]";
	private final String MAIL_REPLACE_ENQUIRY = "[ENQUIRY]";
	private final String MAIL_REPLACE_VERSIONNUM = "[VERSIONNUM]";
	private final String MAIL_REPLACE_DEVICENAME = "[DEVICENAME]";
	private final String MAIL_REPLACE_DEVICEID = "[DEVICEID]";
	private final String MAIL_REPLACE_SYSTEM = "[SYSTEM]";
	private final String MAIL_REPLACE_PHONEMODEL = "[PHONEMODEL]";

	private final String MAIL_REPLACE_DIFFERENCE = "[DIFFERENCE]";
	private final String MAIL_REPLACE_LIMIT = "[LOWERLIMIT]";
	private final String MAIL_REPLACE_TIME = "[TIME]";

	private final String MAIL_REPLACE_PAYER = "[PAYER]";
	private final String MAIL_REPLACE_PAYEE = "[PAYEE]";
	private final String MAIL_REPLACE_AMOUNT = "[AMOUNT]";
	private final String MAIL_REPLACE_CURRENCY = "[CURRENCY]";

	private final String MAIL_REPLACE_AMOUNTIN = "[AMOUNTIN]";
	private final String MAIL_REPLACE_CURRENCYIN = "[CURRENCYIN]";

	private final String MAIL_REPLACE_PERCENT = "[PERCENT]";
	private final String MAIL_REPLACE_NUMBER = "[NUMBER]";
	private final String MAIL_REPLACE_PHONE = "[PHONE]";

	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void init() {
		logger.info("==========init MailManager==========");
		readTemplate("template/mail/zh_CN/contactUs.template", contactTital, contactContent);
		readTemplate("template/mail/zh_CN/criticalAlarm.template", criticalAlarmTital, criticalAlarmContent);
		readTemplate("template/mail/zh_CN/largeTransWarn.template", largeTransWarnTital, largeTransWarnContent);
		readTemplate("template/mail/zh_CN/largeExchangeWarn.template", largeExchangeWarnTital,
				largeExchangeWarnContent);
		readTemplate("template/mail/zh_CN/badAccountAlarm.template", badAccountWarnTital, badAccountWarnContent);
		readTemplate("template/mail/zh_CN/registrationAlarm.template", registrationWarnTital, registrationWarnContent);
		// readTemplate("template/mail/zh_CN/remitFailAlarm.template",
		// remitFailWarnTital, remitFailWarnContent);
		readTemplate("template/mail/zh_CN/totalGDQWarn.template", totalGDQWarnTital, totalGDQWarnContent);
		readTemplate("template/mail/zh_CN/notifyWithdraw.template", notifyWithdrawTital, notifyWithdrawContent);
		initMail = true;
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
			if (!initMail)
				logger.warn("Mail template ({}) read error , can't send this email : {} ",
						new Object[] { filePath, e.getMessage() });
		}
	}

	@Async
	public void mail4contact(String name, String email, String category, String enquiry, String versionNum,
			String deviceName, String deviceId, String system, String phoneModel) {
		String content = contactContent.toString().replace(MAIL_REPLACE_CATEGORY, category)
				.replace(MAIL_REPLACE_EMAIL, email).replace(MAIL_REPLACE_ENQUIRY, enquiry)
				.replace(MAIL_REPLACE_NAME, name).replace(MAIL_REPLACE_VERSIONNUM, versionNum)
				.replace(MAIL_REPLACE_DEVICENAME, deviceName).replace(MAIL_REPLACE_DEVICEID, deviceId)
				.replace(MAIL_REPLACE_SYSTEM, system).replace(MAIL_REPLACE_PHONEMODEL, phoneModel);
		logger.info("content : {}", content);
		List<String> toMails = new ArrayList<>();
		String mails[] = ResourceUtils.getBundleValue4String("contact.to").split(",");
		for (String mail : mails) {
			toMails.add(mail);
		}
		sendMail(toMails, contactTital.toString(), content);
	}

	@Async
	public void mail4criticalAlarm(String email, BigDecimal difference, BigDecimal lowerLimit, String dateTime) {
		String content = criticalAlarmContent.toString().replace(MAIL_REPLACE_DIFFERENCE, difference.toString())
				.replace(MAIL_REPLACE_LIMIT, lowerLimit.toString()).replace(MAIL_REPLACE_TIME, dateTime);
		logger.info("content : {},tital : {}", content, criticalAlarmTital.toString());
		List<String> toMails = new ArrayList<>();
		toMails.add(email);
		sendMail(toMails, criticalAlarmTital.toString(), content);
	}

	@Async
	public void mail4LargeTrans(String email, String payerMobile, String payeeMobile, BigDecimal amount,
			String currency, String dateTime) {
		String content = largeTransWarnContent.toString().replace(MAIL_REPLACE_PAYER, payerMobile)
				.replace(MAIL_REPLACE_TIME, dateTime).replace(MAIL_REPLACE_AMOUNT, amount.toString())
				.replace(MAIL_REPLACE_CURRENCY, currency).replace(MAIL_REPLACE_PAYEE, payeeMobile);
		logger.info("content : {},tital : {}", content, largeTransWarnTital.toString());
		List<String> toMails = new ArrayList<>();
		toMails.add(email);
		sendMail(toMails, largeTransWarnTital.toString(), content);
	}

	@Async
	public void mail4LargeExchange(String email, String payerMobile, String dateTime, BigDecimal amountOut,
			String currencyOut, BigDecimal amountIn, String currencyIn) {
		String content = largeExchangeWarnContent.toString().replace(MAIL_REPLACE_PAYER, payerMobile)
				.replace(MAIL_REPLACE_TIME, dateTime).replace(MAIL_REPLACE_AMOUNT, amountOut.toString())
				.replace(MAIL_REPLACE_CURRENCY, currencyOut).replace(MAIL_REPLACE_AMOUNTIN, amountIn.toString())
				.replace(MAIL_REPLACE_CURRENCYIN, currencyIn);
		logger.info("content : {},tital : {}", content, largeExchangeWarnTital.toString());
		List<String> toMails = new ArrayList<>();
		toMails.add(email);
		sendMail(toMails, largeExchangeWarnTital.toString(), content);
	}

	@Async
	public void mail4BadAccount(String email, String dateTime) {
		String content = badAccountWarnContent.toString().replace(MAIL_REPLACE_TIME, dateTime);
		logger.info("content : {},tital : {}", content, badAccountWarnTital.toString());
		List<String> toMails = new ArrayList<>();
		toMails.add(email);
		sendMail(toMails, badAccountWarnTital.toString(), content);
	}

	@Async
	public void mail4Registration(String email, String dateTime, String number) {
		String content = registrationWarnContent.toString().replace(MAIL_REPLACE_TIME, dateTime)
				.replace(MAIL_REPLACE_NUMBER, number);
		logger.info("content : {},tital : {}", content, registrationWarnTital.toString());
		List<String> toMails = new ArrayList<>();
		toMails.add(email);
		sendMail(toMails, registrationWarnTital.toString(), content);
	}

	// @Async
	// public void mail4RemitFail(String email, String dateTime) {
	// String content = remitFailWarnContent.toString().replace(MAIL_REPLACE_TIME,
	// dateTime);
	// logger.info("content : {},tital : {}", content,
	// remitFailWarnTital.toString());
	// List<String> toMails = new ArrayList<>();
	// toMails.add(email);
	// sendMail(toMails, remitFailWarnTital.toString(), content);
	// }

	@Async
	public void mail4ReachTotalGDQLimit(String email, String amount, String percent) {
		String content = totalGDQWarnContent.toString().replace(MAIL_REPLACE_AMOUNT, amount)
				.replace(MAIL_REPLACE_PERCENT, percent);
		logger.info("content : {},tital : {}", content, totalGDQWarnTital.toString());
		List<String> toMails = new ArrayList<>();
		toMails.add(email);
		sendMail(toMails, totalGDQWarnTital.toString(), content);
	}

	@Async
	public void mail4NotifyWithdray(List<String> toMails, NotifyWithdrawDTO notifyWithdrawDTO) {
		String content = notifyWithdrawContent.toString()
				.replace(MAIL_REPLACE_PHONE, notifyWithdrawDTO.getAreaCode() + notifyWithdrawDTO.getUserPhone())
				.replace(MAIL_REPLACE_NAME, notifyWithdrawDTO.getUserName())
				.replace(MAIL_REPLACE_NUMBER, String.valueOf(notifyWithdrawDTO.getQuantity()))
				.replace(MAIL_REPLACE_EMAIL, notifyWithdrawDTO.getUserEmail())
				.replace(MAIL_REPLACE_TIME, DateFormatUtils.formatDateGMT8(notifyWithdrawDTO.getApplyTime()));
		sendMail(toMails, notifyWithdrawTital.toString(), content);

	}

	public void sendMail(List<String> toMails, String tital, String content) {
		logger.info("sendMail, tital : {}, content : {}", tital, content);
		if (StringUtils.isNotBlank(content)) {
			SendMailRequest sendMessageRequest = new SendMailRequest();
			sendMessageRequest.setContent(content);
			sendMessageRequest.setFromMailAddress(ResourceUtils.getBundleValue4String("contact.from"));
			sendMessageRequest.setFromName(ResourceUtils.getBundleValue4String("contact.from"));
			sendMessageRequest.setSubject(tital);
			sendMessageRequest.setToMails(toMails);
			String param = JsonBinder.getInstance().toJson(sendMessageRequest);
			logger.info("sendMailRequest : {}", param);
			HttpClientUtils.sendPost(ResourceUtils.getBundleValue4String("sendMail.url"), param);
		}
	}

}

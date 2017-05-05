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

import com.yuyutechnology.exchange.util.HttpTookit;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.ResourceUtils;

/**
 * @author suzan.wu
 *
 */
@Service
public class MailManager {
	public static Logger logger = LogManager.getLogger(MailManager.class);
	
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

	private final String MAIL_REPLACE_EMAIL = "[EMAIL]";
	private final String MAIL_REPLACE_NAME = "[NAME]";
	private final String MAIL_REPLACE_CATEGORY = "[CATEGORY]";
	private final String MAIL_REPLACE_ENQUIRY = "[ENQUIRY]";
	
	private final String MAIL_REPLACE_DIFFERENCE = "[DIFFERENCE]";
	private final String MAIL_REPLACE_LIMIT = "[LOWERLIMIT]";
	private final String MAIL_REPLACE_TIME = "[TIME]";
	
	private final String MAIL_REPLACE_PAYER = "[PAYER]";
	private final String MAIL_REPLACE_PAYEE = "[PAYEE]";
	private final String MAIL_REPLACE_AMOUNT = "[AMOUNT]";
	private final String MAIL_REPLACE_CURRENCY = "[CURRENCY]";
	
	private final String MAIL_REPLACE_AMOUNTIN = "[AMOUNTIN]";
	private final String MAIL_REPLACE_CURRENCYIN = "[CURRENCYIN]";

	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void init() {
		logger.info("==========init MailManager==========");
		readTemplate("template/mail/zh_CN/contactUs.template", contactTital, contactContent);
		readTemplate("template/mail/zh_CN/criticalAlarm.template", criticalAlarmTital, criticalAlarmContent);
		readTemplate("template/mail/zh_CN/largeTransWarn.template", largeTransWarnTital, largeTransWarnContent);
		readTemplate("template/mail/zh_CN/largeExchangeWarn.template", largeExchangeWarnTital, largeExchangeWarnContent);
		readTemplate("template/mail/zh_CN/badAccountAlarm.template", badAccountWarnTital, badAccountWarnContent);
	}
	
	private void readTemplate(String filePath, StringBuffer tital, StringBuffer content) {
		try {
			tital.setLength(0);
			content.setLength(0);
			Resource resource = new ClassPathResource(filePath);
			String fileString = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
			tital.append(fileString.substring(0, fileString.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", ""));
			content.append(fileString.substring(fileString.indexOf("\n")).replaceAll("\n", "").replaceAll("\r", ""));
		} catch (Exception e) {
			logger.warn("Mail template ({}) read error , can't send this email : {} ", new Object[]{filePath, e.getMessage()});
		}
	}
	
	@Async
	public void mail4contact(String name, String email, String category, String enquiry) {
		String content = contactContent.toString().replace(MAIL_REPLACE_CATEGORY, category).replace(MAIL_REPLACE_EMAIL, email)
				.replace(MAIL_REPLACE_ENQUIRY, enquiry).replace(MAIL_REPLACE_NAME, name);
		logger.info("content : {}", content);
		List<String> toMails = new ArrayList<>();
		String mails[] = ResourceUtils.getBundleValue4String("contact.to").split(",");
		for (String mail : mails) {
			toMails.add(mail);
		}
		sendMail(toMails, contactTital.toString(), content);
	}
	
	@Async
	public void mail4criticalAlarm(String email,BigDecimal difference,BigDecimal lowerLimit,String dateTime) {
		String content = criticalAlarmContent.toString().
				replace(MAIL_REPLACE_DIFFERENCE, difference.toString()).
				replace(MAIL_REPLACE_LIMIT, lowerLimit.toString()).
				replace(MAIL_REPLACE_TIME, dateTime);
		logger.info("content : {},tital : {}", content,criticalAlarmTital.toString());
		List<String> toMails = new ArrayList<>();
		toMails.add(email);
		sendMail(toMails, criticalAlarmTital.toString(), content);
	}
	
	@Async
	public void mail4LargeTrans(String email,String payerMobile,String payeeMobile,
			BigDecimal amount,String currency,String dateTime) {
		String content = largeTransWarnContent.toString().
				replace(MAIL_REPLACE_PAYER, payerMobile).
				replace(MAIL_REPLACE_TIME, dateTime).
				replace(MAIL_REPLACE_AMOUNT, amount.toString()).
				replace(MAIL_REPLACE_CURRENCY, currency).
				replace(MAIL_REPLACE_PAYEE, payeeMobile);
		logger.info("content : {},tital : {}", content,largeTransWarnTital.toString());
		List<String> toMails = new ArrayList<>();
		toMails.add(email);
		sendMail(toMails, largeTransWarnTital.toString(), content);
	}
	
	@Async
	public void mail4LargeExchange(String email,String payerMobile,String dateTime,
			BigDecimal amountOut,String currencyOut,BigDecimal amountIn,String currencyIn) {
		String content = largeExchangeWarnContent.toString()
				.replace(MAIL_REPLACE_PAYER,payerMobile)
				.replace(MAIL_REPLACE_TIME,dateTime)
				.replace(MAIL_REPLACE_AMOUNT,amountOut.toString())
				.replace(MAIL_REPLACE_CURRENCY,currencyOut)
				.replace(MAIL_REPLACE_AMOUNTIN,amountIn.toString())
				.replace(MAIL_REPLACE_CURRENCYIN,currencyIn);
		logger.info("content : {},tital : {}", content,largeExchangeWarnTital.toString());
		List<String> toMails = new ArrayList<>();
		toMails.add(email);
		sendMail(toMails, largeExchangeWarnTital.toString(), content);
	}
	
	@Async
	public void mail4BadAccount(String email,String dateTime) {
		String content = badAccountWarnContent.toString()
				.replace(MAIL_REPLACE_TIME,dateTime);
		logger.info("content : {},tital : {}", content,badAccountWarnTital.toString());
		List<String> toMails = new ArrayList<>();
		toMails.add(email);
		sendMail(toMails, badAccountWarnTital.toString(), content);
	}
	
	public void sendMail(List<String> toMails, String tital, String content){
		logger.info("sendMail,tital : {}, content : {}",tital, content);
		if (StringUtils.isNotBlank(content)) {
			SendMailRequest sendMessageRequest = new SendMailRequest();
			sendMessageRequest.setContent(content);
			sendMessageRequest.setFromMailAddress(ResourceUtils.getBundleValue4String("contact.from"));
			sendMessageRequest.setFromName(ResourceUtils.getBundleValue4String("contact.from"));
			sendMessageRequest.setSubject(tital);
			sendMessageRequest.setToMails(toMails);
			String param = JsonBinder.getInstance().toJson(sendMessageRequest);
			logger.info("sendMailRequest : {}", param);
			HttpTookit.sendPost(ResourceUtils.getBundleValue4String("sendMail.url"), param);
		}
	}
}

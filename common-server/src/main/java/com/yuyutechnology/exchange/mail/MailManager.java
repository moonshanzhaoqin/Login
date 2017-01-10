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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.ResourceUtils;

/**
 * @author suzan.wu
 *
 */
@Service
public class MailManager {
	public static Logger logger = LoggerFactory.getLogger(MailManager.class);
	
	private StringBuffer contactTital = new StringBuffer();
	private StringBuffer contactContent = new StringBuffer();
	
	private StringBuffer criticalAlarmTital = new StringBuffer();
	private StringBuffer criticalAlarmContent = new StringBuffer();

	private final String MAIL_REPLACE_EMAIL = "[EMAIL]";
	private final String MAIL_REPLACE_NAME = "[NAME]";
	private final String MAIL_REPLACE_CATEGORY = "[CATEGORY]";
	private final String MAIL_REPLACE_ENQUIRY = "[ENQUIRY]";

	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void init() {
		logger.info("==========init MailManager==========");
		readTemplate("template/mail/zh_CN/contactUs.template", contactTital, contactContent);
		readTemplate("template/mail/zh_CN/criticalAlarm.template", criticalAlarmTital, criticalAlarmContent);
	}
	
	private void readTemplate(String filePath, StringBuffer tital, StringBuffer content) {
		try {
			Resource resource = new ClassPathResource(filePath);
			String fileString = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
			tital.append(fileString.substring(0, fileString.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", ""));
			content.append(fileString.substring(fileString.indexOf("\n")).replaceAll("\n", "").replaceAll("\r", ""));
		} catch (Exception e) {
			logger.warn("Mail template ({}) read error , can't send this email : {} ", new Object[]{filePath, e.getMessage()});
		}
	}

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
	
	public void mail4criticalAlarm(String email,BigDecimal difference,BigDecimal lowerLimit,String grade,String dateTime) {
		String content = criticalAlarmContent.toString().
				replace(MAIL_REPLACE_CATEGORY, difference.toString()).
				replace(MAIL_REPLACE_EMAIL, lowerLimit.toString()).
				replace(MAIL_REPLACE_ENQUIRY, grade).
				replace(MAIL_REPLACE_NAME, dateTime);
		logger.info("content : {}", content);
		List<String> toMails = new ArrayList<>();
		toMails.add(email);
		sendMail(toMails, criticalAlarmTital.toString(), content);
	}
	
	public void sendMail(List<String> toMails, String tital, String content){
		logger.info("sendMail,tital : {}, content : {}",tital, content);
		if (StringUtils.isNotBlank(content)) {
			SendMailRequest sendMessageRequest = new SendMailRequest();
			sendMessageRequest.setContent(content);
			sendMessageRequest.setFromMailAddress(ResourceUtils.getBundleValue4String("contact.from"));
			sendMessageRequest.setFromName(ResourceUtils.getBundleValue4String("contact.from"));
			sendMessageRequest.setSubject(contactTital.toString());
			sendMessageRequest.setToMails(toMails);
			String param = JsonBinder.getInstance().toJson(sendMessageRequest);
			logger.info("sendMailRequest : {}", param);
			HttpTookit.sendPost(ResourceUtils.getBundleValue4String("sendMail.url"), param);
		}
	}
}

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
import org.springframework.scheduling.annotation.Async;
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
	
	private String contactTital;
	private String contactContent;
	
	private String criticalAlarmTital;
	private String criticalAlarmContent;

	private final String MAIL_REPLACE_EMAIL = "[EMAIL]";
	private final String MAIL_REPLACE_NAME = "[NAME]";
	private final String MAIL_REPLACE_CATEGORY = "[CATEGORY]";
	private final String MAIL_REPLACE_ENQUIRY = "[ENQUIRY]";

	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void init() {
		logger.info("==========init MailManager==========");
		// 加载模板
		// 到账提醒
		try {
			Resource resource = new ClassPathResource("mail/en_US/contactUs.template");
			String contact = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
			contactTital = contact.substring(0, contact.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", "");
			contactContent = contact.substring(contact.indexOf("\n")).replaceAll("\n", "").replaceAll("\r", "");
			
			resource = new ClassPathResource("mail/zh_CN/criticalAlarm.template");
			contact = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
			criticalAlarmTital = contact.substring(0, contact.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", "");
			criticalAlarmContent = contact.substring(contact.indexOf("\n")).replaceAll("\n", "").replaceAll("\r", "");

		} catch (Exception e) {
			logger.warn("Mail template read error , can't send email : "+ e.getMessage());
		}
	}

	public void mail4contact(String name, String email, String category, String enquiry) {
		String content = contactContent.replace(MAIL_REPLACE_CATEGORY, category).replace(MAIL_REPLACE_EMAIL, email)
				.replace(MAIL_REPLACE_ENQUIRY, enquiry).replace(MAIL_REPLACE_NAME, name);
		logger.info("content : {}", content);
		sendMail(content);
	}
	
	public void mail4criticalAlarm(String email,BigDecimal difference,BigDecimal lowerLimit,String grade,String dateTime) {
		String content = contactContent.
				replace(MAIL_REPLACE_CATEGORY, difference.toString()).
				replace(MAIL_REPLACE_EMAIL, lowerLimit.toString()).
				replace(MAIL_REPLACE_ENQUIRY, grade).
				replace(MAIL_REPLACE_NAME, dateTime);
		logger.info("content : {}", content);
		
		List<String> toMails = new ArrayList<>();
		toMails.add(email);
		
		sendMail(toMails,content);
	}
	
	
	
	
	

	@Async
	private void sendMail(String content) {
		logger.info("sendMail , content : {}", content);
		if (StringUtils.isNotBlank(content)) {
			SendMailRequest sendMessageRequest = new SendMailRequest();
			sendMessageRequest.setContent(content);
			sendMessageRequest.setFromMailAddress(ResourceUtils.getBundleValue4String("contact.from"));
			sendMessageRequest.setFromName(ResourceUtils.getBundleValue4String("contact.from"));
			sendMessageRequest.setSubject(contactTital);
			List<String> toMails = new ArrayList<>();
			String mails[] = ResourceUtils.getBundleValue4String("contact.to").split(",");
			for (String mail : mails) {
				toMails.add(mail);
			}
			sendMessageRequest.setToMails(toMails);
			String param = JsonBinder.getInstance().toJson(sendMessageRequest);
			logger.info("sendMailRequest : {}", param);
			HttpTookit.sendPost(ResourceUtils.getBundleValue4String("sendMail.url"), param);
		}
	}
	
	public void sendMail(List<String> toMails,String content){
		logger.info("sendMail, content : {}",content);
		
		SendMailRequest sendMessageRequest = new SendMailRequest();
		sendMessageRequest.setContent(content);
		sendMessageRequest.setFromMailAddress(ResourceUtils.getBundleValue4String("contact.from"));
		sendMessageRequest.setFromName(ResourceUtils.getBundleValue4String("contact.from"));
		sendMessageRequest.setSubject(contactTital);
		sendMessageRequest.setToMails(toMails);
		String param = JsonBinder.getInstance().toJson(sendMessageRequest);
		logger.info("sendMailRequest : {}", param);
		HttpTookit.sendPost(ResourceUtils.getBundleValue4String("sendMail.url"), param);
	}
	
	

}

/**
 * 
 */
package com.yuyutechnology.exchange.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class MailManager {
	public static Logger logger = LoggerFactory.getLogger(MailManager.class);
	private String contactTital;
	private String contactContent;

	private final String MAIL_REPLACE_EMAIL = "[EMAIL]";
	private final String MAIL_REPLACE_NAME = "[NAME]";
	private final String MAIL_REPLACE_CATEGORY = "[CATEGORY]";
	private final String MAIL_REPLACE_ENQUIRY = "[ENQUIRY]";

	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void init() throws IOException {
		logger.info("==========init MailManager==========");
		// 加载模板
		// 到账提醒
		Resource resource = new ClassPathResource("mail/en_US/contactUs.template");
		String contact = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
		contactTital = contact.substring(0, contact.indexOf("\n") + 1).replaceAll("\n", "").replaceAll("\r", "");
		contactContent = contact.substring(contact.indexOf("\n")).replaceAll("\n", "").replaceAll("\r", "");
	}

	public void mail4contact(String name, String email, String category, String enquiry) {
		contactContent.replace(MAIL_REPLACE_CATEGORY, category).replace(MAIL_REPLACE_EMAIL, email)
				.replace(MAIL_REPLACE_ENQUIRY, enquiry).replace(MAIL_REPLACE_NAME, name);
		logger.info("content : {}",contactContent);
		sendMail(contactContent);
	}

	@Async
	private void sendMail(String content) {
		SendMailRequest sendMessageRequest = new SendMailRequest();
		sendMessageRequest.setContent(content);
		sendMessageRequest.setFromMailAddress(ResourceUtils.getBundleValue4String("contact.from"));
		sendMessageRequest.setFromName(ResourceUtils.getBundleValue4String("contact.from"));
		sendMessageRequest.setSubject(ResourceUtils.getBundleValue4String("contact.subject"));
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

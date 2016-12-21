/**
 * 
 */
package com.yuyutechnology.exchange.mail;

import java.io.IOException;
import java.math.BigDecimal;
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

import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.utils.*;

/**
 * @author suzan.wu
 *
 */
@Service
public class MailManager {
	public static Logger logger = LoggerFactory.getLogger(MailManager.class);

	private String sendMailURL = "";
	private String contactSubject = "";
	private String contactFrom = "";
	private String contactTo = "";

	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void init() throws IOException {
		sendMailURL = ResourceUtils.getBundleValue("sendMail.Url");
		contactSubject = ResourceUtils.getBundleValue("contact.subject");
		contactFrom = ResourceUtils.getBundleValue("contact.from");
		contactTo = ResourceUtils.getBundleValue("contact.to");
	}

	public void mail4contact(String content) {
		sendMail(content);
	}

	@Async
	private void sendMail(String content) {
		SendMailRequest sendMessageRequest = new SendMailRequest();
		sendMessageRequest.setContent(content);
		sendMessageRequest.setFromMailAddress(contactFrom);
		sendMessageRequest.setFromName(contactFrom);
		sendMessageRequest.setSubject(contactSubject);
		List<String> toMails = new ArrayList<>();
		toMails.add(contactTo);
		sendMessageRequest.setToMails(toMails);

		String param = JsonBinder.getInstance().toJson(sendMessageRequest);
		logger.info("sendMailRequest : {}", param);
		HttpTookit.sendPost(sendMailURL, param);
	}

}

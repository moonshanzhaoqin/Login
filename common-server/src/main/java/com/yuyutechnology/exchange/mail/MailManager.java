/**
 * 
 */
package com.yuyutechnology.exchange.mail;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.utils.*;

/**
 * @author suzan.wu
 *
 */
@Service
public class MailManager {
	public static Logger logger = LoggerFactory.getLogger(MailManager.class);

	public void mail4contact(String content) {
		sendMail(content);
	}

	@Async
	private void sendMail(String content) {
		SendMailRequest sendMessageRequest = new SendMailRequest();
		sendMessageRequest.setContent(content);
		sendMessageRequest.setFromMailAddress(ResourceUtils.getBundleValue("contact.from"));
		sendMessageRequest.setFromName(ResourceUtils.getBundleValue("contact.from"));
		sendMessageRequest.setSubject(ResourceUtils.getBundleValue("contact.subject"));
		List<String> toMails = new ArrayList<>();
		toMails.add(ResourceUtils.getBundleValue("contact.to"));
		sendMessageRequest.setToMails(toMails);

		String param = JsonBinder.getInstance().toJson(sendMessageRequest);
		logger.info("sendMailRequest : {}", param);
		HttpTookit.sendPost(ResourceUtils.getBundleValue("sendMail.url"), param);
	}

}

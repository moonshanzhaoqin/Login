package com.yuyutechnology.exchange.push;

import java.io.IOException;

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
import com.yuyutechnology.exchange.sms.SmsManager;
import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.ResourceUtils;

/**
 * 
 * @author suzan.wu
 *
 */
@Service
public class pushManager {
	public static Logger logger = LoggerFactory.getLogger(pushManager.class);
	private String appName = "";
	private String pushToAllURL = "";
	private String pushToCustomURL = "";
	private String pushToTagURL = "";
	// en
	private String transfer_en = "";
	// zh_CN
	private String transfer_CN = "";
	// zh_HK
	private String transfer_HK = "";

//	@PostConstruct
//	@Scheduled(cron = "0 1/10 * * * ?")
	public void init() throws IOException {
		appName = ResourceUtils.getBundleValue("appName");
		pushToAllURL = ResourceUtils.getBundleValue("push.all.url");
		pushToCustomURL = ResourceUtils.getBundleValue("push.custom.url");
		pushToTagURL = ResourceUtils.getBundleValue("push.tag.url");

		// 加载模板
		Resource resource = new ClassPathResource("push/en/transfer.template");
		transfer_en = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_CN/transfer.template");
		transfer_CN = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");

		resource = new ClassPathResource("push/zh_HK/transfer.template");
		transfer_HK = IOUtils.toString(resource.getInputStream(), "UTF-8").replaceAll("\r", "");
	}

	public void push(User user) {
		String title = "";
		String body = "";
		pushToCustom(user.getUserId(), user.getPushId(), title, body);
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
	private void pushToTag() {
		// TODO Auto-generated method stub

	}

	@Async
	private void pushToAll() {
		// TODO Auto-generated method stub

	}

}

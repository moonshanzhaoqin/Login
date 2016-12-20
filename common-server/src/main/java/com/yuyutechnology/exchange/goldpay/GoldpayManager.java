package com.yuyutechnology.exchange.goldpay;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.ResourceUtils;

@Service
public class GoldpayManager {
	public static Logger logger = LoggerFactory.getLogger(GoldpayManager.class);

	private String bindGoldpayURL = "";

	@PostConstruct
	public void init() {
		bindGoldpayURL = ResourceUtils.getBundleValue("GoldpayURL");
	}

	public GoldpayUser getGoldpayInfo(String accessToken) {
		String result = HttpTookit.sendGet(bindGoldpayURL + "?access_token=" + accessToken, null);
		logger.info("result==={}", result);
		if (StringUtils.isNotEmpty(result)) {
			GoldpayInfo goldpayInfo = JsonBinder.getInstance().fromJson(result, GoldpayInfo.class);
			if (goldpayInfo.getRetCode() == 1) {// 成功
				return goldpayInfo.getOauthGoldqUser();
			}
		}
		return null;
	}
}

package com.yuyutechnology.exchange.goldpay;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.ResourceUtils;

@Service
public class GoldpayManager {
	public static Logger logger = LogManager.getLogger(GoldpayManager.class);

	public GoldpayUser getGoldpayInfo(String accessToken) {
		String result = HttpTookit.sendGet(ResourceUtils.getBundleValue4String("goldpayUas.url") + "unity/userInfo.do?access_token=" + accessToken, null);
		logger.info("result==={}", result);
		if (StringUtils.isNotEmpty(result)) {
			GoldpayInfo goldpayInfo = JsonBinder.getInstance().fromJson(result, GoldpayInfo.class);
			if (goldpayInfo!=null && goldpayInfo.getRetCode() == 1) {// 成功
				return goldpayInfo.getOauthGoldqUser();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public GoldpayUser checkGoldpay(String userName, String password) {
		StringBuilder param = new StringBuilder();
		param.append("client_id=").append(ResourceUtils.getBundleValue4String("goldpayUas.client.id", "exchange-client")).append("&");
		param.append("client_secret=").append(ResourceUtils.getBundleValue4String("goldpayUas.client.key", "exchange")).append("&");
		param.append("grant_type=").append("password").append("&");
		param.append("username=").append(userName).append("&");
		param.append("password=").append(password);
		String result = HttpTookit.sendPost4Form(ResourceUtils.getBundleValue4String("goldpayUas.url") + "oauth/token", param.toString());
		logger.info("result==={}", result);
		if (StringUtils.isNotEmpty(result)) {
			Map<String, String> resultMap = JsonBinder.getInstance().fromJson(result, HashMap.class);
			if (StringUtils.isNotBlank(resultMap.get("access_token"))) {
				return getGoldpayInfo(resultMap.get("access_token"));
			}
		}
		return null;
	}
}

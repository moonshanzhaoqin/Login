package com.yuyutechnology.exchange.goldpay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.goldpay.msg.CreateGoldpayC2S;
import com.yuyutechnology.exchange.goldpay.msg.CreateGoldpayS2C;
import com.yuyutechnology.exchange.goldpay.msg.GoldpayUserDTO;
import com.yuyutechnology.exchange.goldpay.msg.Transfer2GoldpayC2S;
import com.yuyutechnology.exchange.util.HttpClientUtils;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.ResourceUtils;

@Service
public class GoldpayManager {
	public static Logger logger = LogManager.getLogger(GoldpayManager.class);

	// public GoldpayUser getGoldpayInfo(String accessToken) {
	// String result =
	// HttpClientUtils.sendGet(ResourceUtils.getBundleValue4String("goldpayUas.url")
	// + "unity/userInfo.do?access_token=" + accessToken, null);
	// logger.info("result==={}", result);
	// if (StringUtils.isNotEmpty(result)) {
	// GoldpayInfo goldpayInfo = JsonBinder.getInstance().fromJson(result,
	// GoldpayInfo.class);
	// if (goldpayInfo!=null && goldpayInfo.getRetCode() == 1) {// 成功
	// return goldpayInfo.getOauthGoldqUser();
	// }
	// }
	// return null;
	// }
	//
	// @SuppressWarnings("unchecked")
	// public GoldpayUser checkGoldpay(String userName, String password) {
	// StringBuilder param = new StringBuilder();
	// param.append("client_id=").append(ResourceUtils.getBundleValue4String("goldpayUas.client.id",
	// "exchange-client")).append("&");
	// param.append("client_secret=").append(ResourceUtils.getBundleValue4String("goldpayUas.client.key",
	// "exchange")).append("&");
	// param.append("grant_type=").append("password").append("&");
	// param.append("username=").append(userName.trim()).append("&");
	// param.append("password=").append(password);
	// logger.info("param to string : {}",param.toString());
	// String result =
	// sendPost(ResourceUtils.getBundleValue4String("goldpayUas.url") +
	// "oauth/token", param.toString());
	// logger.info("result==={}", result);
	// if (StringUtils.isNotEmpty(result)) {
	// Map<String, String> resultMap = JsonBinder.getInstance().fromJson(result,
	// HashMap.class);
	// if (StringUtils.isNotBlank(resultMap.get("access_token"))) {
	// return getGoldpayInfo(resultMap.get("access_token"));
	// }
	// }
	// return null;
	// }

	public GoldpayUserDTO createGoldpay(String areaCode, String userPhone, boolean newUser) {
		CreateGoldpayC2S createGoldpayRequest = new CreateGoldpayC2S(areaCode, userPhone, newUser);
		String param = JsonBinder.getInstanceNonNull().toJson(createGoldpayRequest);
		logger.info("param==={}", param);
		String result = HttpClientUtils
				.sendPost(ResourceUtils.getBundleValue4String("goldpay.url") + "member/createMember", param);
		logger.info("result==={}", result);
		if (StringUtils.isNotEmpty(result)) {
			CreateGoldpayS2C goldpayInfo = JsonBinder.getInstance().fromJson(result, CreateGoldpayS2C.class);
			if (goldpayInfo != null && (goldpayInfo.getRetCode() == 1 || goldpayInfo.getRetCode() == 100004)) {// 成功
				return goldpayInfo.getGoldpayUserDTO();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public boolean transferGDQ2Goldpay(String accountNum, BigDecimal balance) {
		Transfer2GoldpayC2S transfer2GoldpayRequset = new Transfer2GoldpayC2S();
		transfer2GoldpayRequset.setBalance(balance.longValue());
		transfer2GoldpayRequset.setComment("");
		transfer2GoldpayRequset
				.setFromAccountNum(ResourceUtils.getBundleValue4String("goldpay.merchant.fromAccountNum"));
		transfer2GoldpayRequset.setOrderType("3");
		transfer2GoldpayRequset.setPayOrderId("");
		transfer2GoldpayRequset.setToAccountNum(accountNum);
		transfer2GoldpayRequset.setToken(ResourceUtils.getBundleValue4String("goldpay.merchant.token"));

		String param = JsonBinder.getInstance().toJson(transfer2GoldpayRequset);
		logger.info("param==={}", param);
		String result = HttpClientUtils.sendPost(
				ResourceUtils.getBundleValue4String("goldpay.url") + "trans/payConfirmTransaction4Merchant", param);
		logger.info("result==={}", result);
		if (StringUtils.isNotEmpty(result)) {
			Map resultMap = JsonBinder.getInstance().fromJson(result, HashMap.class);
			if ((Integer)resultMap.get("retCode") == 1) {
				return true;
			}else {
				
			}
		}
		return false;
	}

}

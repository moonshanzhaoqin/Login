package com.yuyutechnology.exchange.goldpay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mangofactory.swagger.models.dto.jackson.SwaggerApiListingJsonSerializer;
import com.yuyutechnology.exchange.util.HttpClientUtils;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.ResourceUtils;

@Service
public class GoldpayManager {
	public static Logger logger = LogManager.getLogger(GoldpayManager.class);

//	public GoldpayUser getGoldpayInfo(String accessToken) {
//		String result = HttpClientUtils.sendGet(ResourceUtils.getBundleValue4String("goldpayUas.url") + "unity/userInfo.do?access_token=" + accessToken, null);
//		logger.info("result==={}", result);
//		if (StringUtils.isNotEmpty(result)) {
//			GoldpayInfo goldpayInfo = JsonBinder.getInstance().fromJson(result, GoldpayInfo.class);
//			if (goldpayInfo!=null && goldpayInfo.getRetCode() == 1) {// 成功
//				return goldpayInfo.getOauthGoldqUser();
//			}
//		}
//		return null;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public GoldpayUser checkGoldpay(String userName, String password) {
//		StringBuilder param = new StringBuilder();
//		param.append("client_id=").append(ResourceUtils.getBundleValue4String("goldpayUas.client.id", "exchange-client")).append("&");
//		param.append("client_secret=").append(ResourceUtils.getBundleValue4String("goldpayUas.client.key", "exchange")).append("&");
//		param.append("grant_type=").append("password").append("&");
//		param.append("username=").append(userName.trim()).append("&");
//		param.append("password=").append(password);
//		logger.info("param to string : {}",param.toString());
//		String result = sendPost(ResourceUtils.getBundleValue4String("goldpayUas.url") + "oauth/token", param.toString());
//		logger.info("result==={}", result);
//		if (StringUtils.isNotEmpty(result)) {
//			Map<String, String> resultMap = JsonBinder.getInstance().fromJson(result, HashMap.class);
//			if (StringUtils.isNotBlank(resultMap.get("access_token"))) {
//				return getGoldpayInfo(resultMap.get("access_token"));
//			}
//		}
//		return null;
//	}
	
	
	
	
	
	
	
	public GoldpayUser createGoldpay(String areaCode, String userPhone,boolean newUser) {
		CreateGoldpayRequest createGoldpayRequest=new CreateGoldpayRequest(areaCode,userPhone,newUser);
		String param =JsonBinder.getInstance().toJson(createGoldpayRequest);
		String result=	HttpClientUtils.sendPost(ResourceUtils.getBundleValue4String("goldpay.url") + "member/createMember", param);
		logger.info("result==={}", result);
		if (StringUtils.isNotEmpty(result)) {
			GoldpayInfo goldpayInfo = JsonBinder.getInstance().fromJson(result, GoldpayInfo.class);
			if (goldpayInfo!=null && (goldpayInfo.getRetCode() == 1||goldpayInfo.getRetCode() == 10004)) {// 成功
				return goldpayInfo.getGoldpayUserDTO();
			}
		}
		return null;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public boolean transferGDQ2Goldpay(String accountNum,BigDecimal balance) {
		StringBuilder param = new StringBuilder();
		param.append("toAccountNum=").append(accountNum).append("&");
		param.append("fromAccountNum=").append(ResourceUtils.getBundleValue4String("goldpay.merchant.fromAccountNum")).append("&");
		param.append("token=").append(ResourceUtils.getBundleValue4String("goldpay.merchant.token")).append("&");
		param.append("balance=").append(balance).append("&");
		param.append("orderType=").append(3).append("&");
		param.append("payOrderId=").append("");
		String result=	HttpClientUtils.sendPost(ResourceUtils.getBundleValue4String("goldpay.url") + "trans/payConfirmTransaction4Merchant", param.toString());
		logger.info("result==={}", result);
		if (StringUtils.isNotEmpty(result)) {
			Map<String, String> resultMap = JsonBinder.getInstance().fromJson(result, HashMap.class);
			if (resultMap.get("retCode")=="1") {
				return true;
			}
		}		
		return false;
	}
	
	
	
	
	
	
	
}

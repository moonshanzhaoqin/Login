package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.PayPalTransManager;
import com.yuyutechnology.exchange.server.controller.request.PaypalTransInitRequest;
import com.yuyutechnology.exchange.server.controller.response.GetExchangeRate4GDQResponse;
import com.yuyutechnology.exchange.server.controller.response.PaypalTransInitResponse;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

@Controller
public class PayPalTransController {
	
	@Autowired
	OandaRatesManager oandaRatesManager;
	@Autowired
	PayPalTransManager payPalTransManager;
	
	public static Logger logger = LogManager.getLogger(PayPalTransController.class);
	
	@ApiOperation(value = "paypal交易  获取汇率")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/paypalTrans/getExchangeRate4GDQ")
	public @ResponseEncryptBody GetExchangeRate4GDQResponse 
	getExchangeRate4GDQ(@PathVariable String token){
		
		GetExchangeRate4GDQResponse rep = new GetExchangeRate4GDQResponse();
		HashMap<String, Double> result = oandaRatesManager.getExchangeRateDiffLeft4OneRight(ServerConsts.CURRENCY_OF_GOLDPAY);
		Date updateDate = oandaRatesManager.getExchangeRateUpdateDate();
		
		rep.setRates(result);
		rep.setUpdateDate(updateDate);
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		
		return rep;
	}
	
	@ApiOperation(value = "paypal交易 订单初始化")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/paypalTrans/paypalTransInit")
	public @ResponseEncryptBody PaypalTransInitResponse 
	paypalTransInit(@PathVariable String token,@RequestDecryptBody PaypalTransInitRequest reqMsg){
		// 从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		PaypalTransInitResponse rep = new PaypalTransInitResponse();
		
		//判断条件.币种合法，GDQ数量为整数且大于100
		if(reqMsg.getAmount() == null ||(reqMsg.getAmount().compareTo(new BigDecimal("100")) == -1 || reqMsg.getAmount().longValue()%1 > 0)){
			logger.warn("The number of inputs does not meet the requirements");
			rep.setRetCode(RetCodeConsts.TRANSFER_PAYPALTRANS_ILLEGAL_DATA);
			rep.setMessage("The number of inputs does not meet the requirements");
			return rep;
			
		}
		
		HashMap<String, Object> result = payPalTransManager.paypalTransInit(sessionData.getUserId(), reqMsg.getCurrency(), reqMsg.getAmount());
		if(RetCodeConsts.EXCHANGE_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY.equals(result.get("retCode"))){
			rep.setRetCode(RetCodeConsts.EXCHANGE_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			rep.setMessage((String) result.get("msg"));
			return rep;
		}
		
		rep.setTransId((String) result.get("transId"));
		rep.setAccessToken((String) result.get("token"));
		rep.setAmount(((BigDecimal) result.get("amount")).doubleValue());
		
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);

		return rep;
	}
	
	@ApiOperation(value = "paypal交易  交易确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/paypalTrans/paypalTransConfirm")
	public void paypalTransConfirm(@PathVariable String token,String transId,String paypalCallback){
		
	}

}

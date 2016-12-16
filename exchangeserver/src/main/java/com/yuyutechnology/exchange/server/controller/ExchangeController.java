package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dto.WalletInfo;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.server.controller.request.ExchangeCalculationRequest;
import com.yuyutechnology.exchange.server.controller.request.ExchangeConfirmRequest;
import com.yuyutechnology.exchange.server.controller.response.ExchangeCalculationResponse;
import com.yuyutechnology.exchange.server.controller.response.ExchangeConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.GetCurrentBalanceResponse;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

@Controller
public class ExchangeController {
	
	@Autowired
	ExchangeManager exchangeManager;
	
	public static Logger logger = LoggerFactory.getLogger(ExchangeController.class);
	
	@ApiOperation(value = "获取当前余额")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/getCurrentBalance")
	public @ResponseBody
	GetCurrentBalanceResponse getCurrentBalance(@PathVariable String token){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GetCurrentBalanceResponse rep = new GetCurrentBalanceResponse();
		List<WalletInfo> wallets = exchangeManager.getWalletsByUserId(sessionData.getUserId());
		if(wallets.isEmpty()){
			rep.setRetCode(ServerConsts.RET_CODE_FAILUE);
			rep.setMessage("");
		}else{
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage("");
			rep.setWallets(wallets);
		}
		return rep;
	}
	
	@ApiOperation(value = "兑换计算")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/exchangeCalculation")
	public @ResponseBody
	ExchangeCalculationResponse exchangeCalculation(@PathVariable String token,@RequestBody ExchangeCalculationRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		ExchangeCalculationResponse rep = new ExchangeCalculationResponse();
		
		HashMap<String, String> result = exchangeManager.exchangeCalculation(sessionData.getUserId(), 
				reqMsg.getCurrencyOut(), reqMsg.getCurrencyIn(), new BigDecimal(reqMsg.getAmountOut()));
		
		rep.setRetCode(result.get("retCode"));
		rep.setMessage(result.get("msg"));
		
		if(result.get("retCode").equals(ServerConsts.RET_CODE_SUCCESS)){
			rep.setAmountIn(Double.parseDouble(result.get("in")));
			rep.setAmountOut(Double.parseDouble(result.get("out")));
		}

		return rep;
	}
	
	@ApiOperation(value = "兑换确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/exchange/exchangeConfirm")
	public @ResponseBody
	ExchangeConfirmResponse exchangeConfirm(@PathVariable String token,@RequestBody ExchangeConfirmRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		ExchangeConfirmResponse rep = new ExchangeConfirmResponse();
		String retCode = exchangeManager.exchangeConfirm(sessionData.getUserId(), 
				reqMsg.getCurrencyOut(), reqMsg.getCurrencyIn(), 
				new BigDecimal(reqMsg.getAmountOut()), new BigDecimal(reqMsg.getAmountIn()));
		if(retCode.equals(ServerConsts.RET_CODE_SUCCESS)){
			rep.setMessage("ok");
		}else if(retCode.equals(ServerConsts.EXCHANGE_WALLET_CAN_NOT_BE_QUERIED)){
			rep.setMessage("");
		}else if(retCode.equals(ServerConsts.EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE)){
			rep.setMessage("");
		}else if(retCode.equals(ServerConsts.EXCHANGE_AMOUNT_LESS_THAN_MINIMUM_TRANSACTION_AMOUNT)){
			rep.setMessage("");
		}
		rep.setRetCode(retCode);
		return rep;
	}
	
}

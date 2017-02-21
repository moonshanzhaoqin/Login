package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.manager.WalletManager;
import com.yuyutechnology.exchange.server.controller.response.GetTotalAmontGoldResponse;
import com.yuyutechnology.exchange.server.session.SessionData;
import com.yuyutechnology.exchange.server.session.SessionDataHolder;

@Controller
public class WalletController {
	
	@Autowired
	WalletManager walletManager;
	
	public static Logger logger = LoggerFactory.getLogger(TransferController.class);

	@ApiOperation(value = "获取黄金总量")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/wallet/getTotalAmontGold")
	public @ResponseBody
	GetTotalAmontGoldResponse getTotalAmontGold(@PathVariable String token){
		
		SessionData sessionData = SessionDataHolder.getSessionData();
		GetTotalAmontGoldResponse rep = new GetTotalAmontGoldResponse();
		double amount = walletManager.getTotalAmoutGold(sessionData.getUserId());
		
		BigDecimal amoutOz = new BigDecimal(amount).divide(new BigDecimal("28.3495231"),2, RoundingMode.DOWN);
		
		rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
		rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
		rep.setAmountOfGold(amount);
		rep.setAmountOfGoldOz(amoutOz.doubleValue());
		
		return rep;
	}
	

}

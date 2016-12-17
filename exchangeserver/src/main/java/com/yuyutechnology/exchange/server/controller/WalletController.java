package com.yuyutechnology.exchange.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.manager.WalletManager;
import com.yuyutechnology.exchange.server.controller.response.GetTotalAmontGoldResponse;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

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
		
		rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
		rep.setMessage("ok");
		rep.setAmountOfGold(amount);
		
		return rep;
	}
	

}

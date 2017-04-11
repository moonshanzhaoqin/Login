package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.manager.WalletManager;
import com.yuyutechnology.exchange.server.controller.response.GetTotalAmontGoldResponse;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

@Controller
public class WalletController {
	
	@Autowired
	WalletManager walletManager;
	
	public static Logger logger = LogManager.getLogger(TransferController.class);

	@ApiOperation(value = "获取黄金总量")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/wallet/getTotalAmontGold")
	public @ResponseEncryptBody
	GetTotalAmontGoldResponse getTotalAmontGold(@PathVariable String token){
		
		SessionData sessionData = SessionDataHolder.getSessionData();
		GetTotalAmontGoldResponse rep = new GetTotalAmontGoldResponse();

		HashMap<String, BigDecimal> result = walletManager.getTotalAmoutGold(sessionData.getUserId());
		
		if(result != null){
			rep.setRetCode(RetCodeConsts.RET_CODE_SUCCESS);
			rep.setMessage(MessageConsts.RET_CODE_SUCCESS);
			rep.setAmountOfGold(result.get("goldAmount").doubleValue());
			rep.setAmountOfGoldOz(result.get("goldAmountOz").doubleValue());
		}else{
			rep.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
			rep.setMessage(MessageConsts.RET_CODE_FAILUE);
		}
		
		return rep;
	}
	

}

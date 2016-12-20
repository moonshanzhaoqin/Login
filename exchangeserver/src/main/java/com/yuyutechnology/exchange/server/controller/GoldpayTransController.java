package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.HashMap;

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
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.server.controller.request.GoldpayPurchaseRequest;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

@Controller
public class GoldpayTransController {
	
	@Autowired
	GoldpayTransManager goldpayTransManager;
	
	public static Logger logger = LoggerFactory.getLogger(GoldpayTransController.class);
	
	@ApiOperation(value = "交易初始化")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/goldpayPurchase")
	public @ResponseBody
	void goldpayPurchase(@PathVariable String token,@RequestBody GoldpayPurchaseRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		HashMap<String, String> map = goldpayTransManager.goldpayPurchase(sessionData.getUserId(), 
				reqMsg.getGoldpayAccount(), new BigDecimal(reqMsg.getAmount()));
		
//		if()
		
		
	}

}

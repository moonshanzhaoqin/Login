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
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.server.controller.request.GoldpayPurchaseRequest;
import com.yuyutechnology.exchange.server.controller.request.RequestPinRequest;
import com.yuyutechnology.exchange.server.controller.response.GoldpayPurchaseResponse;
import com.yuyutechnology.exchange.server.controller.response.RequestPinResponse;
import com.yuyutechnology.exchange.server.session.SessionData;
import com.yuyutechnology.exchange.server.session.SessionDataHolder;

@Controller
public class GoldpayTransController {
	
	@Autowired
	GoldpayTransManager goldpayTransManager;
	
	public static Logger logger = LoggerFactory.getLogger(GoldpayTransController.class);
	
	@ApiOperation(value = "goldpay 买入")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/goldpayPurchase")
	public @ResponseBody
	GoldpayPurchaseResponse goldpayPurchase(@PathVariable String token,@RequestBody GoldpayPurchaseRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GoldpayPurchaseResponse rep = new GoldpayPurchaseResponse();
		HashMap<String, String> map = goldpayTransManager.goldpayPurchase(sessionData.getUserId(), 
				reqMsg.getGoldpayAccount(), new BigDecimal(reqMsg.getAmount()));
		
		if(map != null && map.get("retCode").equals(ServerConsts.RET_CODE_SUCCESS)){
			rep.setTransferId(map.get("transferId"));
		}
		
		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));
		
		return rep;
		
		
	}
	
	@ApiOperation(value = "goldpay 重新发送Pin")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/requestPin")
	public @ResponseBody
	RequestPinResponse requestPin(@PathVariable String token,@RequestBody RequestPinRequest reqMsg){
		
		RequestPinResponse rep = new RequestPinResponse();
		
		HashMap<String, String> map = goldpayTransManager.requestPin(reqMsg.getTransferId());
		if(map != null && map.get("retCode").equals(ServerConsts.RET_CODE_SUCCESS)){
			rep.setTransferId(map.get("transferId"));
		}
		
		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));
		
		return rep;
	}

	public void goldpayTransConfirm(String pin,String transferId){
		
	}
	
	
}

package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.aspectj.apache.bcel.generic.IINC;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.server.controller.request.GoldpayPurchaseRequest;
import com.yuyutechnology.exchange.server.controller.request.GoldpayTransConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.GoldpayWithdrawRequest;
import com.yuyutechnology.exchange.server.controller.request.RequestPinRequest;
import com.yuyutechnology.exchange.server.controller.request.WithdrawConfirmRequest;
import com.yuyutechnology.exchange.server.controller.response.GoldpayPurchaseResponse;
import com.yuyutechnology.exchange.server.controller.response.GoldpayTransConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.GoldpayWithdrawResponse;
import com.yuyutechnology.exchange.server.controller.response.RequestPinResponse;
import com.yuyutechnology.exchange.server.controller.response.WithdrawConfirmResponse;
import com.yuyutechnology.exchange.server.session.SessionData;
import com.yuyutechnology.exchange.server.session.SessionDataHolder;

@Controller
public class GoldpayTransController {
	
	@Autowired
	GoldpayTransManager goldpayTransManager;
	@Autowired
	ConfigManager configManager;
	
	public static Logger logger = LogManager.getLogger(GoldpayTransController.class);
	
	@ApiOperation(value = "goldpay 买入")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/goldpayPurchase")
	public @ResponseBody
	GoldpayPurchaseResponse goldpayPurchase(@PathVariable String token,@RequestBody GoldpayPurchaseRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GoldpayPurchaseResponse rep = new GoldpayPurchaseResponse();
		
		if(reqMsg.getAmount() == 0 || reqMsg.getAmount()%1 > 0 ){
			logger.warn("The input amount is less than the minimum amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The input amount is less than the minimum amount");
			return rep;
		}else if(reqMsg.getAmount() > configManager.getConfigLongValue(ConfigKeyEnum.ENTERMAXIMUMAMOUNT, 1000000000L)){
			logger.warn("Fill out the allowable amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_FILL_OUT_THE_ALLOWABLE_AMOUNT);
			rep.setMessage("Fill out the allowable amount");
			return rep;
		}
		
		HashMap<String, String> map = goldpayTransManager.goldpayPurchase(sessionData.getUserId(), new BigDecimal(reqMsg.getAmount()));
		
		if(map != null && map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)){
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
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		RequestPinResponse rep = new RequestPinResponse();
		
		HashMap<String, String> map = goldpayTransManager.requestPin(sessionData.getUserId(),reqMsg.getTransferId());
		if(map != null && map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)){
			rep.setTransferId(map.get("transferId"));
		}
		
		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));
		
		return rep;
	}

	@ApiOperation(value = "goldpay 交易确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/goldpayTransConfirm")
	public @ResponseBody
	GoldpayTransConfirmResponse goldpayTransConfirm(@PathVariable String token,@RequestBody GoldpayTransConfirmRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GoldpayTransConfirmResponse rep = new GoldpayTransConfirmResponse(); 
		HashMap<String, String> map = goldpayTransManager.goldpayTransConfirm(
				sessionData.getUserId(), reqMsg.getPin(), reqMsg.getTransferId());
		
		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));
		
		return rep;
	}
	
	
	@ApiOperation(value = "goldpay 提现")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/goldpayWithdraw")
	public @ResponseBody
	GoldpayWithdrawResponse goldpayWithdraw(@PathVariable String token,@RequestBody GoldpayWithdrawRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		GoldpayWithdrawResponse rep = new GoldpayWithdrawResponse();
		
		if(reqMsg.getAmount() == 0 || reqMsg.getAmount()%1 > 0  ){
			logger.warn("The input amount is less than the minimum amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			rep.setMessage("The input amount is less than the minimum amount");
			return rep;
		}else if(reqMsg.getAmount() > configManager.getConfigLongValue(ConfigKeyEnum.ENTERMAXIMUMAMOUNT, 1000000000L)){
			logger.warn("Fill out the allowable amount");
			rep.setRetCode(RetCodeConsts.TRANSFER_FILL_OUT_THE_ALLOWABLE_AMOUNT);
			rep.setMessage("Fill out the allowable amount");
			return rep;
		}
		
		HashMap<String, String> map = goldpayTransManager.goldpayWithdraw(sessionData.getUserId(), reqMsg.getAmount());
		
		if(map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)){
			rep.setTransferId(map.get("transferId"));
			rep.setFee((Long.valueOf(map.get("charge")).doubleValue()));
		}
		
		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));
		
		return rep;
	}
	
	@ApiOperation(value = "goldpay 提现确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/goldpayTrans/withdrawConfirm")
	public @ResponseBody 
	WithdrawConfirmResponse withdrawConfirm(@PathVariable String token,@RequestBody WithdrawConfirmRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		WithdrawConfirmResponse rep = new WithdrawConfirmResponse();
		if(StringUtils.isEmpty(reqMsg.getPayPwd())){
			rep.setRetCode(RetCodeConsts.TRANSFER_PAYMENTPWD_INCORRECT);
			rep.setMessage("The payment password is incorrect");
			return rep;
		}
		HashMap<String, String> map = goldpayTransManager.withdrawConfirm1(sessionData.getUserId(),
				reqMsg.getPayPwd(), reqMsg.getTransferId());
		if(map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)){
			HashMap<String, String> map2 = goldpayTransManager.withdrawConfirm2(sessionData.getUserId(), reqMsg.getTransferId());
			rep.setRetCode(map2.get("retCode"));
			rep.setMessage(map2.get("msg"));
			return rep;
		}
		rep.setRetCode(map.get("retCode"));
		rep.setMessage(map.get("msg"));
		return rep;
	}
	
}

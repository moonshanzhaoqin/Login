package com.yuyutechnology.exchange.server.controller;

import java.math.BigDecimal;

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
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.server.controller.request.TransPwdConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.TransferConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.TransferInitiateRequest;
import com.yuyutechnology.exchange.server.controller.response.TransPwdConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.TransferInitiateResponse;

@Controller
public class TransferController {
	
	@Autowired
	TransferManager transferManager;
	
	public static Logger logger = LoggerFactory.getLogger(TransferController.class);

	@ApiOperation(value = "交易初始化")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transferInitiate")
	public @ResponseBody
	TransferInitiateResponse transferInitiate(@PathVariable String token,
			@RequestBody TransferInitiateRequest reqMsg){
		//从Session中获取Id
		int userId = 2;
		TransferInitiateResponse rep = new TransferInitiateResponse();
		String result = transferManager.transferInitiate(userId, reqMsg.getAreaCode(), reqMsg.getUserPhone(),
				reqMsg.getCurrency(), new BigDecimal(reqMsg.getAmount()), reqMsg.getTransferComment());
		
		if(!result.contains("_")){
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage("");
			rep.setTransferId(result);
		}else if(result.equals(ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT)){
			rep.setRetCode(ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			rep.setMessage("");
		}else if(result.equals(ServerConsts.TRANSFER_EXCEEDED_TRANSACTION_LIMIT)){
			rep.setRetCode(ServerConsts.TRANSFER_EXCEEDED_TRANSACTION_LIMIT);
			rep.setMessage("");
		}

		return rep;
		
	}
	
	@ApiOperation(value = "验证支付密码")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transPwdConfirm")
	public @ResponseBody
	TransPwdConfirmResponse transPwdConfirm(@PathVariable String token,@RequestBody TransPwdConfirmRequest reqMsg){
		//从Session中获取Id
		int userId = 2;
		TransPwdConfirmResponse rep = new TransPwdConfirmResponse();
		String result = transferManager.payPwdConfirm(userId, reqMsg.getTransferId(), reqMsg.getUserPayPwd());
		
		if(result.equals(ServerConsts.RET_CODE_SUCCESS)){
			rep.setMessage("ok");
		}else{
			rep.setMessage("The payment password is incorrect");
		}
		rep.setRetCode(result);
		return rep;
	}
	
	@ApiOperation(value = "pinCode 验证及交易确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transPwdConfirm")
	public void transferConfirm(@PathVariable String token,@RequestBody TransferConfirmRequest reqMsg){
		
		//判断PinCode是否正确
		if(true){
			
		}
		
		
	}
	
	
	

}

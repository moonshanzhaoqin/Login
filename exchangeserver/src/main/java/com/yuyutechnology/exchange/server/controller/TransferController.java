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
import com.yuyutechnology.exchange.dto.UserInfo;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.server.controller.request.GetTransactionRecordRequest;
import com.yuyutechnology.exchange.server.controller.request.RequestATransferRequest;
import com.yuyutechnology.exchange.server.controller.request.TransPwdConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.TransferConfirmRequest;
import com.yuyutechnology.exchange.server.controller.request.TransferInitiateRequest;
import com.yuyutechnology.exchange.server.controller.response.RequestATransferResponse;
import com.yuyutechnology.exchange.server.controller.response.TransPwdConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.TransferConfirmResponse;
import com.yuyutechnology.exchange.server.controller.response.TransferInitiateResponse;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;

@Controller
public class TransferController {
	
	@Autowired
	UserManager userManager;
	@Autowired
	TransferManager transferManager;
	
	
	public static Logger logger = LoggerFactory.getLogger(TransferController.class);

	@ApiOperation(value = "交易初始化")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transferInitiate")
	public @ResponseBody
	TransferInitiateResponse transferInitiate(@PathVariable String token,
			@RequestBody TransferInitiateRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		TransferInitiateResponse rep = new TransferInitiateResponse();
		String result = transferManager.transferInitiate(sessionData.getUserId(), reqMsg.getAreaCode(),
				reqMsg.getUserPhone(),reqMsg.getCurrency(), new BigDecimal(reqMsg.getAmount()), 
				reqMsg.getTransferComment(),0);
		
		if(result.equals(ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT)){
			rep.setRetCode(ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			rep.setMessage("");
		}else if(result.equals(ServerConsts.TRANSFER_EXCEEDED_TRANSACTION_LIMIT)){
			rep.setRetCode(ServerConsts.TRANSFER_EXCEEDED_TRANSACTION_LIMIT);
			rep.setMessage("");
		}else{
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage("");
			rep.setTransferId(result);
		}

		return rep;
		
	}
	
	@ApiOperation(value = "验证支付密码")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transPwdConfirm")
	public @ResponseBody
	TransPwdConfirmResponse transPwdConfirm(@PathVariable String token,@RequestBody TransPwdConfirmRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		TransPwdConfirmResponse rep = new TransPwdConfirmResponse();
		String result = transferManager.payPwdConfirm(sessionData.getUserId(), reqMsg.getTransferId(), reqMsg.getUserPayPwd());
		
		if(result.equals(ServerConsts.RET_CODE_SUCCESS)){
			rep.setMessage("ok");
		}else if(result.equals(ServerConsts.TRANSFER_REQUIRES_PHONE_VERIFICATION)){
			//发PIN码
			UserInfo user = userManager.getUserInfo(sessionData.getUserId());
			userManager.getPinCode(reqMsg.getTransferId(),user.getAreaCode(), user.getPhone());
			rep.setMessage("Need to send pin code verification");
		}else{
			rep.setMessage("The payment password is incorrect");
		}
		rep.setRetCode(result);
		return rep;
	}
	
	@ApiOperation(value = "交易确认")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/transferConfirm")
	public  @ResponseBody
	TransferConfirmResponse transferConfirm(@PathVariable String token,@RequestBody TransferConfirmRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		UserInfo user = userManager.getUserInfo(sessionData.getUserId());
		TransferConfirmResponse rep = new TransferConfirmResponse();
		//判断PinCode是否正确
		if(userManager.testPinCode(reqMsg.getTransferId(), user.getAreaCode(), user.getPhone(),reqMsg.getPinCode())){
			String result = transferManager.transferConfirm(reqMsg.getTransferId());
			
			if(result.equals(ServerConsts.RET_CODE_SUCCESS)){
				rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
				rep.setMessage("ok");
			}else{
				rep.setRetCode(result);
				rep.setMessage("Current balance is insufficient");
			}

		}else{
			rep.setRetCode(ServerConsts.PIN_CODE_INCORRECT);
			rep.setMessage("The pin code is incorrect");
		}
		return rep;
	}
	
	@ApiOperation(value = "请求转账")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/requestATransfer")
	public @ResponseBody
	RequestATransferResponse requestATransfer(RequestATransferRequest reqMsg){
		//从Session中获取Id
		SessionData sessionData = SessionDataHolder.getSessionData();
		RequestATransferResponse rep = new RequestATransferResponse();
		String result = transferManager.transferInitiate(sessionData.getUserId(), reqMsg.getAreaCode(),
				reqMsg.getUserPhone(),reqMsg.getCurrency(), new BigDecimal(reqMsg.getAmount()), 
				null,reqMsg.getNoticeId());
		
		if(result.equals(ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT)){
			rep.setRetCode(ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			rep.setMessage("");
		}else if(result.equals(ServerConsts.TRANSFER_EXCEEDED_TRANSACTION_LIMIT)){
			rep.setRetCode(ServerConsts.TRANSFER_EXCEEDED_TRANSACTION_LIMIT);
			rep.setMessage("");
		}else{
			rep.setRetCode(ServerConsts.RET_CODE_SUCCESS);
			rep.setMessage("");
			rep.setTransferId(result);
		}
		return rep;
	}
	
	@ApiOperation(value = "获取交易明细")
	@RequestMapping(method = RequestMethod.POST, value = "/token/{token}/transfer/getTransactionRecord")
	public void getTransactionRecord(GetTransactionRecordRequest reqMsq){
		
	}

}

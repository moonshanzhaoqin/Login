package com.yuyutechnology.exchange.server.controller;

import java.util.List;

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
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.server.controller.request.ExchangeCalculationRequest;
import com.yuyutechnology.exchange.server.controller.response.GetWalletInfoResponse;

@Controller
@RequestMapping(value="/token/{token}/exchange")
public class ExchangeController {
	
	@Autowired
	ExchangeManager exchangeManager;
	
	public static Logger logger = LoggerFactory.getLogger(ExchangeController.class);
	
	@ApiOperation(value = "获取当前余额")
	@RequestMapping(method = RequestMethod.POST, value = "/getCurrentBalance")
	public @ResponseBody
	GetWalletInfoResponse getCurrentBalance(@PathVariable String token){
		//从Session中获取Id
		int userId = 0;
		GetWalletInfoResponse rep = new GetWalletInfoResponse();
		List<Wallet> wallets = exchangeManager.getWalletsByUserId(userId);
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
	@RequestMapping(method = RequestMethod.POST, value = "/exchangeCalculation")
	public void exchangeCalculation(ExchangeCalculationRequest exchangeCalculationRequest){

	}
	
	
	
}

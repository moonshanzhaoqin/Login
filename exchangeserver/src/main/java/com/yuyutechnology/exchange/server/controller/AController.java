package com.yuyutechnology.exchange.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.FeeManager;

@Controller
public class AController {
	@Autowired
	CommonManager commonManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	FeeManager feeManager;
	
	@ResponseBody
	@ApiOperation(value = "邀请人信息")
	@RequestMapping(value = "/refresh", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public void refresh() {
		commonManager.refreshConfig();
		configManager.refreshConfig();
		feeManager.refresh();
	}
}

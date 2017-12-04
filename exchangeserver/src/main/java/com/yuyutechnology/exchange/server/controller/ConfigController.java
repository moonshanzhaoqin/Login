package com.yuyutechnology.exchange.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.FeeManager;

@Controller
@ApiIgnore
public class ConfigController {
	@Autowired
	CommonManager commonManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	FeeManager feeManager;
	
	@ResponseBody
	@RequestMapping(value = "/refresh", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public String refresh() {
		commonManager.refreshConfig();
		configManager.refreshConfig();
		feeManager.refresh();
		return "ok!!";
	}
}

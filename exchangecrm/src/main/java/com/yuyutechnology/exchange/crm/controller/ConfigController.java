package com.yuyutechnology.exchange.crm.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyutechnology.exchange.crm.reponse.BaseResponse;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.pojo.Config;

@Controller
public class ConfigController {
	private static Logger logger = LoggerFactory.getLogger(ConfigController.class);

	@Autowired
	ConfigManager configManager;

	// TODO updateConfig
	@ResponseBody
	@RequestMapping(value = "/updateConfig", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse updateConfig(@RequestBody Config config) {
		BaseResponse rep = new BaseResponse();
		logger.info("updateConfig({}, {})",config.getConfigKey(),config.getConfigValue());
		String retCode=configManager.updateConfig(config.getConfigKey(), config.getConfigValue());
		rep.setRetCode(retCode);
		return rep;
	}


	// TODO getConfigList
	@ResponseBody
	@RequestMapping(value = "/getConfigList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<Config> getConfigList() {
		return configManager.getConfigList();
	}

}
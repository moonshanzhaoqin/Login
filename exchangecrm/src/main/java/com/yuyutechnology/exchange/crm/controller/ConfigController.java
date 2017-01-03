package com.yuyutechnology.exchange.crm.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public String updateConfig(Config config) {
		configManager.updateConfig(config.getConfigValue(), config.getConfigValue());
		return null;
	}

	// TODO addConfig
	@ResponseBody
	@RequestMapping(value = "/addConfig", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String addConfig(Config config) {
		configManager.addConfig(config);
		return null;
	}

	// TODO getConfigList
	@ResponseBody
	@RequestMapping(value = "/getConfigList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<Config> getConfigList() {
		return configManager.getConfigList();
	}

}
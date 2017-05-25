package com.yuyutechnology.exchange.crm.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyutechnology.exchange.crm.reponse.BaseResponse;
import com.yuyutechnology.exchange.enums.Operation;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.pojo.Config;
import com.yuyutechnology.exchange.pojo.CrmLog;

@Controller
public class ConfigController {
	private static Logger logger = LogManager.getLogger(ConfigController.class);

	@Autowired
	ConfigManager configManager;
	@Autowired
	CrmLogManager CrmLogManager;

	@ResponseBody
	@RequestMapping(value = "/updateConfig", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResponse updateConfig(@RequestBody Config config,HttpServletRequest request, HttpServletResponse response) {
		BaseResponse rep = new BaseResponse();
		logger.info("updateConfig({}, {})",config.getConfigKey(),config.getConfigValue());
		String retCode=configManager.updateConfig(config.getConfigKey(), config.getConfigValue());
		configManager.refreshConfig();
		CrmLogManager.saveCrmLog(new CrmLog((String) request.getSession().getAttribute("adminName"), new Date(),
				Operation.UPDATE_CONFIG.getOperationName(),config.toString()));
		rep.setRetCode(retCode);
		return rep;
	}

	@ResponseBody
	@RequestMapping(value = "/getConfigList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<Config> getConfigList() {
		return configManager.getConfigList();
	}

}
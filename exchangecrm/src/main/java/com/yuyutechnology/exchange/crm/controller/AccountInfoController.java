package com.yuyutechnology.exchange.crm.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.manager.CrmUserInfoManager;

@Controller
public class AccountInfoController {
	
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	
	ModelAndView mav;
	
	private static Logger log = LoggerFactory.getLogger(AccountInfoController.class);

	public ModelAndView getTotalAssetsInfo(){

		mav = new ModelAndView();
		
		HashMap<String, BigDecimal> systemTotalAssets = crmUserInfoManager.getSystemAccountTotalAssets();
		HashMap<String, BigDecimal> userTotalAssets = crmUserInfoManager.getUserAccountTotalAssets();
		
		log.info("systemTotalAssets : {}",systemTotalAssets.toString());
		log.info("userTotalAssets : {}",userTotalAssets.toString());
		
		mav.addObject("systemTotalAssets", systemTotalAssets);
		mav.addObject("userTotalAssets", userTotalAssets);
		mav.setViewName("");
		
		return mav;
	}
	
	
	
	
	
	
	
	
}

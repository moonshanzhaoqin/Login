package com.yuyutechnology.exchange.crm.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.utils.oanda.PriceInfo;

@Controller
public class ExchangeRateController {
	@Autowired
	OandaRatesManager oandaRatesManager;
	
	ModelAndView mav;
	
	private static Logger logger = LogManager.getLogger(AdminController.class);
	
	public ModelAndView getAllExchangeRates(){
		

		List<PriceInfo> priceInfos = oandaRatesManager.getAllPrices();
		
		
		return mav;
		
	}

	
	

}

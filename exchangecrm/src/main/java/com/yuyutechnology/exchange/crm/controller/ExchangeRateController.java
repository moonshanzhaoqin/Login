package com.yuyutechnology.exchange.crm.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.util.oanda.PriceInfo;

@Controller
public class ExchangeRateController {
	@Autowired
	OandaRatesManager oandaRatesManager;

	ModelAndView mav;

	private static Logger logger = LogManager.getLogger(AdminController.class);

	@RequestMapping(value = "/exchangeRate/getAllExchangeRates", method = { RequestMethod.GET })
	public ModelAndView getAllExchangeRates() {
		mav = new ModelAndView();
		List<PriceInfo> priceInfos = oandaRatesManager.getAllPrices();
		// if(priceInfos!= null){
		// for (PriceInfo priceInfo : priceInfos) {
		// logger.info(" priceInfo : instrument : {}",priceInfo.getInstrument());
		// }
		// }
		mav.addObject("priceInfos", priceInfos);
		mav.setViewName("exchangeRate/priceInfos");
		return mav;
	}

}

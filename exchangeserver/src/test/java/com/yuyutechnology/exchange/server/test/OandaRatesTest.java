/**
 * 
 */
package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.server.controller.TransferController;
import com.yuyutechnology.exchange.utils.oanda.OandaRespData;
import com.yuyutechnology.exchange.utils.oanda.PriceInfo;

/**
 * @author silent.sun
 *
 */
public class OandaRatesTest extends BaseSpringJunit4 {

	@Autowired
	OandaRatesManager oandaRatesManager;
	
	public static Logger logger = LoggerFactory.getLogger(TransferController.class);

	@Test
	public void test() {

		OandaRespData oandaRespData = oandaRatesManager.getCurrentPrices("USD_JPY2CEUR_JPY");
		
		
		
		if(oandaRespData != null){
			logger.info("oandaRespData : {} ",oandaRespData.toString());

			PriceInfo priceInfo = oandaRespData.getPrices()[0];
			
			logger.info("bid : {}",priceInfo.getBid());
		}else{
			logger.warn("#errors");
		}

		
		
	}

}

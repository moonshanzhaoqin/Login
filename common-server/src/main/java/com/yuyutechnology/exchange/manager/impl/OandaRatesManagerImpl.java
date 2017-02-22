package com.yuyutechnology.exchange.manager.impl;

import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.utils.HttpTookit;

@Service
public class OandaRatesManagerImpl implements OandaRatesManager {
	
	
	
	
	
	public void getRatesFromOanda(){
		
        String domain = "https://stream-fxpractice.oanda.com";
        String access_token = "ACCESS-TOKEN";
        String account_id = "1234567";
        String instruments = "EUR_USD,USD_JPY,EUR_JPY";
        
        String result = HttpTookit.sendGet(domain, "");
        
        
		
		
	}

}

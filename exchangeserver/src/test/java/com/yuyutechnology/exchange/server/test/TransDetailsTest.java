package com.yuyutechnology.exchange.server.test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.pojo.Currency;

public class TransDetailsTest extends BaseSpringJunit4 {

	
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	TransferManager transferManager;
	@Autowired
	TransDetailsManager transDetailsManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	OandaRatesManager oandaRatesManager;
	
	@Test
	public void test() throws InterruptedException {
		
//		TransDetailsDTO dto = transferManager.getTransDetails("2017042503120T000064",8);
//		System.out.println(dto.getTraderName());
		
		List<Currency> list = commonManager.getCurrentCurrencies();
		
		for (Currency currency : list) {
			System.out.println(currency.getCurrency());
		}
		
		
		LinkedHashMap<String, Double> map = oandaRatesManager.getExchangeRate(ServerConsts.CURRENCY_OF_GOLDPAY);
		
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			System.out.println(entry.getKey());
		}
		
	}
	
	
}

package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.manager.OandaExchangeRateManager;
import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.oanda.CurrencyInfo;
import com.yuyutechnology.exchange.utils.oanda.OandaRedisData;
import com.yuyutechnology.exchange.utils.oanda.OandaRepData;

@Service
public class OandaExchangeRateManagerImpl implements OandaExchangeRateManager {
	
	
	private static Logger logger = LoggerFactory.getLogger(OandaExchangeRateManagerImpl.class);

	@Override
	public void updateExchangeRate(boolean refresh) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getExchangeRate(String base, String outCurrency) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getExchangeResult(String transCurrency, BigDecimal transAmount) {
		// TODO Auto-generated method stub
		getExchangeRate();
		return null;
	}
	
	
	
	private void getExchangeRate(){
		String oandaUrl = "https://www.oanda.com/rates/api/v1/rates/CNY.json";
		String oandaParam = "api_key=aGlOOUmEduY3PMTvvoGEmzmj&"
				+ "decimal_places=5&"
				+ "date=2017-02-21&"
				+ "fields=averages"
				+ "&quote=USD&quote=ADP&quote=AFA&quote=ALL";
		String result = HttpTookit.sendGet(oandaUrl, oandaParam);
		logger.info("result : {}",result);
		OandaRepData oandaRepData = JsonBinder.getInstance().fromJson(result, OandaRepData.class);

		//处理收到的数据
		OandaRedisData oandaRedisData = new OandaRedisData();
		
//		oandaRedisData.setBase(base);
		oandaRedisData.setUpdateTime(oandaRepData.getMeta().getRequest_time());
		HashMap<String, CurrencyInfo> exchangeRate = new HashMap<>();
		Map<String,CurrencyInfo> quotes = oandaRepData.getQuotes();
		for (Entry<String, CurrencyInfo> entry : quotes.entrySet()) {
			if(entry.getKey().equals("XAU")){
				//将黄金兑换成克，当前为盎司（os）
			}else{
				exchangeRate.put(entry.getKey(), entry.getValue());
			}
		}
		
		

	}

}

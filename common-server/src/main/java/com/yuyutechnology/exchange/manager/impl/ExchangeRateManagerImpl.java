package com.yuyutechnology.exchange.manager.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.ResourceUtiles;
import com.yuyutechnology.exchange.utils.exchangerate.ExchangeRate;

@Service
public class ExchangeRateManagerImpl implements ExchangeRateManager {

	@Autowired
	CurrencyDAO currencyDAO;
	@Autowired
	RedisDAO redisDAO;
	
	public static Logger logger = LoggerFactory.getLogger(ExchangeRateManagerImpl.class);
	
	@Override
	public void updateExchangeRate(){
		
		String exchangeRateUrl = ResourceUtiles.getBundleValue("exchange.rate.url");
		List<Currency> currencies = currencyDAO.getCurrencys();
		if(currencies.isEmpty()){
			return ;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		for (Currency currency : currencies) {
			String result = HttpTookit.sendGet(exchangeRateUrl, "base="+currency.getCurrency());
			logger.info("result : {}",result);
			map.put(currency.getCurrency(), result);
		}
//		redisDAO.saveData("redis_exchangeRate", new Gson().toJson(map), 5);
		redisDAO.saveData("redis_exchangeRate",JsonBinder.getInstance().toJson(map), 5);
	}

	@SuppressWarnings("unchecked")
	@Override
	public double getExchangeRate(String base, String outCurrency) {
		double out = 0;
		HashMap<String, String> map = new HashMap<String, String>();
		String result = redisDAO.getValueByKey("redis_exchangeRate");
		if(StringUtils.isNotBlank(result)){
//			logger.info("result : {}",result);
			map = new Gson().fromJson(result, map.getClass());
			String value = map.get(base);
			logger.info("value : {}",value);
			ExchangeRate exchangeRate = new Gson().fromJson(value, ExchangeRate.class);
			
			if(base.equals(ServerConsts.CURRENCY_OF_GOLDPAY) || outCurrency.equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
				//当兑换中有goldpay时，需要特殊处理
			}else{
				switch (outCurrency) {
				case "CNY"://人民币
					out = exchangeRate.getRates().getCNY();
					break;
				case "USD"://美元
					out = exchangeRate.getRates().getUSD();
					break;
				case "HKD"://港币
					out = exchangeRate.getRates().getHKD();
					break;
				case "EUR"://欧元
					out = exchangeRate.getRates().getEUR();
					break;
				case "GBP"://英镑
					out = exchangeRate.getRates().getGBP();
					break;
				case "JPY"://日元
					out = exchangeRate.getRates().getJPY();
					break;

				default:
					break;
				}
			}
		}
		
		logger.info("base : {},out : {}",base,out);
		
		return out;
	}
	
	
	
	


}

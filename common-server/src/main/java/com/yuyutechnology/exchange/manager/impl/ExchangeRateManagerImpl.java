package com.yuyutechnology.exchange.manager.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.ConfigDAO;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.ResourceUtils;
import com.yuyutechnology.exchange.utils.exchangerate.ExchangeRate;
import com.yuyutechnology.exchange.utils.exchangerate.GoldpayExchangeRate;

@Service
public class ExchangeRateManagerImpl implements ExchangeRateManager {
	
	@Autowired
	RedisDAO redisDAO;
	@Autowired
	ConfigDAO configDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	CurrencyDAO currencyDAO;
	
	public static Logger logger = LoggerFactory.getLogger(ExchangeRateManagerImpl.class);
	
	@Override
	public void updateExchangeRateNoGoldq(){
		
		String exchangeRateUrl = ResourceUtils.getBundleValue("exchange.rate.url");
		List<Currency> currencies = currencyDAO.getCurrencys();
		if(currencies.isEmpty()){
			return ;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		for (Currency currency : currencies) {
			if(!currency.getCurrency().equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
				String result = HttpTookit.sendGet(exchangeRateUrl, "base="+currency.getCurrency());
				logger.info("result : {}",result);
				map.put(currency.getCurrency(), result);
			}
		}
		redisDAO.saveData("redis_exchangeRate",JsonBinder.getInstance().toJson(map));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateGoldpayExchangeRate(){

		Document doc = null;
		try {
			doc = Jsoup.connect(ResourceUtils.getBundleValue("gold.price.url")).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(doc == null ){
			updateGoldpayExchangeRate();
		}
		Element title = doc.getElementById("sp-bid");
		logger.info("cunrrent bid gold price is {}",title.text());
		String spBid = title.text().trim().replace(",","");
//		title = doc.getElementById("sp-ask");
//		String spAsk = title.text();

		//1盎司黄金约等于31.1035克黄金 
		//1克黄金是一万goldpay
		//获取1goldpay等于多少美金
		//BigDecimal oneGoldpay = new BigDecimal(map.get(type)).divide(new BigDecimal(311035));
		BigDecimal gdp4USDExchangeRate = (new BigDecimal(spBid)).divide(new BigDecimal(311035),5,BigDecimal.ROUND_DOWN);
		logger.info("goldpay for USD exchangeRate : {}",gdp4USDExchangeRate);
		BigDecimal USD4GdpExchangeRate = (new BigDecimal(311035)).divide(new BigDecimal(spBid),5,BigDecimal.ROUND_DOWN);
		logger.info("USD for goldpay exchangeRate : {}",USD4GdpExchangeRate);
		
		GoldpayExchangeRate goldpayExchangeRate = new GoldpayExchangeRate();
		goldpayExchangeRate.setDate(new Date());
		
		String result = redisDAO.getValueByKey("redis_exchangeRate");
		if(StringUtils.isNotBlank(result)){
			HashMap<String, String> map = JsonBinder.getInstance().
					fromJson(result, HashMap.class);
			String value = map.get("USD");

			ExchangeRate exchangeRate = JsonBinder.getInstanceNonNull().
					fromJson(value, ExchangeRate.class);
			//gdp4Others 表示 1gdp兑换多少其他币种
			Map<String,BigDecimal> gdp4Others = new HashMap<String,BigDecimal>();
			gdp4Others.put("USD", gdp4USDExchangeRate);
			for(Map.Entry<String, Double> entry : exchangeRate.getRates().entrySet()){
				gdp4Others.put(entry.getKey(), (new BigDecimal(entry.getValue())).divide(USD4GdpExchangeRate, 5,BigDecimal.ROUND_DOWN));
			}
			logger.info("gdp4Others Map : {}",gdp4Others.toString());
			
			//others4Gdp 表示 1其他币种兑换多少gdp
			Map<String,BigDecimal> others4Gdp = new HashMap<String,BigDecimal>();
			others4Gdp.put("USD", USD4GdpExchangeRate);
			List<Currency> list = currencyDAO.getCurrencys();
			for (Currency index : list) {
				if(!index.getCurrency().equals("USD") && !index.getCurrency().equals(ServerConsts.CURRENCY_OF_GOLDPAY) ){
					others4Gdp.put(index.getCurrency(), (new BigDecimal(getExchangeRateNoGoldq(index.getCurrency(),"USD"))).divide(gdp4USDExchangeRate, 5,BigDecimal.ROUND_DOWN));
				}
			}
			
			logger.info("others4Gdp Map : {}",others4Gdp.toString());
			
			goldpayExchangeRate.setGdp4Others(gdp4Others);
			goldpayExchangeRate.setOthers4Gdp(others4Gdp);
		}
		
		redisDAO.saveData("redis_goldpay_exchangerate",JsonBinder.getInstance().
				toJson(goldpayExchangeRate));

	}
	
	@Override
	public double getExchangeRate(String base, String outCurrency) {
		double out = 0;
		if(base.equals(ServerConsts.CURRENCY_OF_GOLDPAY) || 
				outCurrency.equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
			//当兑换中有goldpay时，需要特殊处理
			String goldpayER = redisDAO.getValueByKey("redis_goldpay_exchangerate");
			GoldpayExchangeRate goldpayExchangeRate = JsonBinder.getInstance().
					fromJson(goldpayER, GoldpayExchangeRate.class);
			
			if(base.equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
				HashMap<String, BigDecimal> gdp4Others = (HashMap<String, BigDecimal>) 
						goldpayExchangeRate.getGdp4Others();
				out = gdp4Others.get(outCurrency).doubleValue();
			}else{
				HashMap<String, BigDecimal> others4Gdp = (HashMap<String, BigDecimal>) 
						goldpayExchangeRate.getOthers4Gdp();
				out = others4Gdp.get(base).doubleValue();
			}
			
		}else{
			out = getExchangeRateNoGoldq(base,outCurrency);
		}
		
		logger.info("base : {},outCurrency : {},out : {}",new Object[]{base,outCurrency,out});
		
		return out;
	}
	
	@Override
	public HashMap<String, Double> getExchangeRate(String base) {
		
		 HashMap<String, Double> map = new HashMap<>();
		
		 List<Currency> list = currencyDAO.getCurrencys();
		
		for (Currency currency : list) {
			
			if(!currency.getCurrency().equals(base)){
				Double value = getExchangeRate(base, currency.getCurrency());
				map.put(currency.getCurrency(), value);
			}
		}
		
		return map;
	}

	@Override
	public BigDecimal getExchangeResult(String transCurrency,BigDecimal transAmount){
		BigDecimal result = null;
		//默认币种
		String standardCurrency = configDAO.getConfigValue(ServerConsts.STANDARD_CURRENCY);
		if(transCurrency.equals(standardCurrency)){
			result = transAmount;
		}else{
			double exchangeRate = getExchangeRate(transCurrency, standardCurrency);
			result = transAmount.multiply(new BigDecimal(exchangeRate));
		}
		return result;
	}
	
	@Override
	public BigDecimal getTotalBalance(int userId){
		String standardCurrency = configDAO.getConfigValue(ServerConsts.STANDARD_CURRENCY);
		logger.info("The current default currency : {}" ,standardCurrency);
		List<Wallet> list = walletDAO.getWalletsByUserId(userId);
		double totalBalance = 0;
		if(list.isEmpty()){
			return new BigDecimal(0);
		}
		
		for (Wallet wallet : list) {
			
			if(!wallet.getCurrency().getCurrency().equals(standardCurrency)){
				
				double exchangeRate = getExchangeRate(
						wallet.getCurrency().getCurrency(), standardCurrency);
				
				totalBalance = totalBalance+wallet.getBalance().longValue()*exchangeRate;
				
			}else{
				totalBalance = totalBalance+wallet.getBalance().longValue();
			}
		}
		
		BigDecimal out = null ;
		if(standardCurrency.equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
			out = new BigDecimal(totalBalance).setScale(0,BigDecimal.ROUND_FLOOR);
		}else{
			out = new BigDecimal(totalBalance).setScale(2,BigDecimal.ROUND_FLOOR);
		}
		
		logger.info("Total assets of the current account : {}" ,out);
		
		return out;
	}

	/////////////////////////////////////////////////方法内调用//////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	private double getExchangeRateNoGoldq(String base, String outCurrency) {
		double out = 0;
		HashMap<String, String> map = new HashMap<String, String>();
		String result = redisDAO.getValueByKey("redis_exchangeRate");
		if(StringUtils.isNotBlank(result)){
//			logger.info("result : {}",result);
			map = JsonBinder.getInstance().fromJson(result, HashMap.class);
			String value = map.get(base);
			logger.info("value : {}",value);
			ExchangeRate exchangeRate = JsonBinder.getInstanceNonNull().
					fromJson(value, ExchangeRate.class);
			out = exchangeRate.getRates().get(outCurrency);
		}
		
		logger.info("base : {},out : {}",base,out);
		
		return out;
	}


	
}

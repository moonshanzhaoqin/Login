package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.utils.HttpClientUtils;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.ResourceUtils;
import com.yuyutechnology.exchange.utils.oanda.OandaRespData;
import com.yuyutechnology.exchange.utils.oanda.PriceInfo;

@Service
public class OandaRatesManagerImpl implements OandaRatesManager {
	
	
	@Autowired
	CurrencyDAO currencyDAO;
	
	
	@Autowired
	RedisDAO redisDAO;
	
	
	public static Logger logger = LoggerFactory.getLogger(OandaRatesManagerImpl.class);
	
	private static String oandaRatesPrefix = "oanda_rates_[key]";

	@Override
	public void updateExchangeRates(){
		
		String instruments = redisDAO.getValueByKey(oandaRatesPrefix.replace("[key]", "instruments"));
		
		logger.info("instruments from redis : {}",instruments);
		
		List<Currency> currencies = currencyDAO.getCurrentCurrency();
		
		if(StringUtils.isNotBlank(instruments)){
			
			boolean isUpdate = false;
			
			for (Currency currency : currencies) {
				
				//CNY->CNH
				String replacedStr = instruments.replace("CNH", "CNY");
				logger.info("instruments replaced : {}",replacedStr);
				
				if(!"GDQ".equals(currency.getCurrency()) && 
						!replacedStr.contains(currency.getCurrency())){
					isUpdate = true;	
				}
			}
			
			if(isUpdate){
				instruments = saveExchangeRatesAllParams(currencies);
				logger.info("instruments : {}",instruments);
				//更新redis参数字段
				redisDAO.saveData(oandaRatesPrefix.replace("[key]", "instruments"), instruments);
			}else{
				saveExchangeRatesMultiParams(instruments);
			}

		}else{
			instruments = saveExchangeRatesAllParams(currencies);
			logger.info("instruments : {}",instruments);
			//更新redis参数字段
			redisDAO.saveData(oandaRatesPrefix.replace("[key]", "instruments"), instruments);
			
		}
		
		//单独更新黄金对美元的汇率
		saveExchangeRatesMultiParams("XAU_USD");
		
	}
	
	
	@Override
	public BigDecimal getExchangedAmount(String currencyIn,BigDecimal amountIn,String currencyOut,String type){
		
		
		if((!currencyIn.equals("GDQ")&&!currencyIn.equals("XAU"))
				&&(!currencyOut.equals("GDQ")&&!currencyOut.equals("XAU"))){
			
			//获取汇率，如果存在  amountIn*汇率
			BigDecimal exchangeRate = getExchangeRate(currencyIn,currencyOut,type);
			
			if(exchangeRate != null){
				return amountIn.multiply(exchangeRate);
			}else{
				//如果不存在  amount/汇率
				exchangeRate = getExchangeRate(currencyOut,currencyIn,type);
				if(exchangeRate != null){
					return amountIn.divide(exchangeRate, 8, BigDecimal.ROUND_DOWN);
				}else{
					return new BigDecimal("-1");
				}
			}
		}
		//黄金与其他货币的兑换
		
		
		
		
		//GDQ与其他货币的兑换
		if(currencyIn.equals("GDQ") || currencyOut.equals("GDQ")){
			
			//首先获取 1oz黄金对应的美元价值
			BigDecimal rate4XAU2USD = getExchangeRate("XAU", "USD", type);
			//计算1GDQ对应的美元价值
			BigDecimal rate4GDQ2USD = rate4XAU2USD.divide(new BigDecimal("10000").
					multiply(new BigDecimal(ResourceUtils.getBundleValue4String("exchange.oz4g", "31.1034768"))),
					8,BigDecimal.ROUND_DOWN);
			logger.info("rate4XAU2USD :{} ,rate4GDQ2USD : {}",rate4XAU2USD,rate4GDQ2USD);
			
			if(currencyIn.equals("GDQ")){
				if(currencyOut.equals("USD")){
					return rate4GDQ2USD.multiply(amountIn);
				}else{
					BigDecimal rateUSD2Other = getExchangeRate("USD",currencyOut,type);
					if(rateUSD2Other != null){
						return amountIn.multiply(rate4GDQ2USD).multiply(rateUSD2Other);
					}else{
						BigDecimal rateOther2USD = getExchangeRate(currencyOut,"USD",type);
						if(rateOther2USD != null){
							return rate4GDQ2USD.multiply(new BigDecimal("1")
									.divide(rateOther2USD,8,BigDecimal.ROUND_DOWN))
									.multiply(amountIn);
						}

					}
				}
			}
			
			
			if(currencyOut .equals("GDQ")){
				if(currencyIn.equals("USD")){
					return amountIn.divide(rate4GDQ2USD, 8, BigDecimal.ROUND_DOWN);
				}else{
					BigDecimal rateOther2USD = getExchangeRate(currencyIn,"USD",type);
					if(rateOther2USD!= null){
						return rateOther2USD.divide(rate4GDQ2USD,8,BigDecimal.ROUND_DOWN).multiply(amountIn);
					}else{
						BigDecimal rateUSD2Other = getExchangeRate("USD",currencyIn,type);
						if(rateUSD2Other != null){
							return new BigDecimal("1")
									.divide(rate4GDQ2USD.multiply(rateUSD2Other),8,BigDecimal.ROUND_DOWN)
									.multiply(amountIn);
						}
					}
				}
			}

		}
		
		return null;
		
	}
	
	@Override
	public BigDecimal getDefaultCurrencyAmount(String transCurrency,BigDecimal transAmount,String type){
		
		return getExchangedAmount(transCurrency,transAmount,ServerConsts.STANDARD_CURRENCY,type);
		
	}
	


	///////////////////////////////////////////////////////////////////////////////////////
	
	public String saveExchangeRatesAllParams(List<Currency> currencies){
		
		List<String> multiParamsList = new ArrayList<>();
		
		for (Currency base : currencies) {
			if(!base.getCurrency().equals("GDQ")){
				for (Currency targetCurrency : currencies) {
					if(!base.getCurrency().equals(targetCurrency.getCurrency()) 
							&& !targetCurrency.getCurrency().equals("GDQ")){
						String instruments = generateParam(base.getCurrency(),targetCurrency.getCurrency());
						logger.info("instruments : {}",instruments);
						
						OandaRespData oandaRespData = getCurrentPrices(instruments);
						if(oandaRespData != null){
							//保存到redis中
							PriceInfo[] prices = oandaRespData.getPrices();
							
							for (PriceInfo priceInfo : prices) {
								//保存到redis中
								String jsonStr = JsonBinder.getInstance().toJson(priceInfo);
								redisDAO.saveData(oandaRatesPrefix.replace("[key]", priceInfo.getInstrument()), jsonStr);
							}
							
							multiParamsList.add(instruments);
						}
					}
				}
			}
		}
		
		logger.info("multiParamsList content : {}",multiParamsList.toString());
		
		return generateParams(multiParamsList);
	}
	
	public void saveExchangeRatesMultiParams(String instruments){
		OandaRespData oandaRespData = getCurrentPrices(instruments);
		if(oandaRespData!= null){
			PriceInfo[] prices = oandaRespData.getPrices();
			
			for (PriceInfo priceInfo : prices) {
				//保存到redis中
				String jsonStr = JsonBinder.getInstance().toJson(priceInfo);
				redisDAO.saveData(oandaRatesPrefix.replace("[key]", priceInfo.getInstrument()), jsonStr);
			}
		}

	}
	
	public BigDecimal getExchangeRate(String currencyIn,String currencyOut,String type){
		
		String param = generateParam(currencyIn,currencyOut);
		
		String redisData = redisDAO.getValueByKey(oandaRatesPrefix.replace("[key]", param));
		
		if(StringUtils.isNotBlank(redisData)){
			PriceInfo priceInfo = JsonBinder.getInstance().fromJson(redisData, PriceInfo.class);
			
			if(type.equals("bid")){
				return priceInfo.getBid();
			}else{
				return priceInfo.getAsk();
			}
		}
		return null;
		
	}

	public OandaRespData getCurrentPrices(String instruments){
		
		OandaRespData oandaRespData = null;
		
		String domain = "https://api-fxpractice.oanda.com/v1/prices";
		String params = "instruments="+instruments;
		BasicHeader basicHeader = new BasicHeader("Authorization", 
				"Bearer " + "d413e2cd916ebc4613376c3a3ca826ae-ebdc8079ec4cca1b1d650ea030036226");
		String result = HttpClientUtils.sendGet(domain,params,basicHeader);
		logger.info("result : {}",result);
		
		if(result.contains("#errors")){
        	return null;
        }else{
        	oandaRespData = JsonBinder.getInstance().fromJson(result, OandaRespData.class);
        }
		
        return oandaRespData;
	}

	private String generateParam(String base,String targetCurrency){
		
		String param = "[BASE]_[TARGETCURRENCY]";
		String result = param.replace("[BASE]", base).replace("[TARGETCURRENCY]", targetCurrency);
		if(result.contains("CNY")){
			return result.replace("CNY", "CNH");
		}
		return result;
		
	}
	
	private String generateParams(List<String> multiParamsList){
		
		StringBuilder stringBuilder = new StringBuilder();
		if(!multiParamsList.isEmpty()){
			if(multiParamsList.size() == 1){
				return multiParamsList.get(0);
			}else{
				for(int i = 0;i<multiParamsList.size()-1;i++){
					stringBuilder.append(multiParamsList.get(i)+"%2C");
				}
				stringBuilder.append(multiParamsList.get(multiParamsList.size()-1));
				return stringBuilder.toString();
			}
		}
		return null;
	}

}

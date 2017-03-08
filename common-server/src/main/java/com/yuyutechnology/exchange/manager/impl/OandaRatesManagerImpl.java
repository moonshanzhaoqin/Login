package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.utils.HttpClientUtils;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.ResourceUtils;
import com.yuyutechnology.exchange.utils.oanda.OandaRespData;
import com.yuyutechnology.exchange.utils.oanda.PriceInfo;

@Service
public class OandaRatesManagerImpl implements OandaRatesManager {
	
	
	@Autowired
	WalletDAO walletDAO;
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
	public BigDecimal getExchangedAmount(String currencyLeft,BigDecimal amountIn,String currencyRight,String type){
		
		
		if((!currencyLeft.equals("GDQ")&&!currencyLeft.equals("XAU"))
				&&(!currencyRight.equals("GDQ")&&!currencyRight.equals("XAU"))){
			
			//获取汇率，如果存在  amountIn*汇率
			BigDecimal exchangeRate = getExchangeRate(currencyLeft,currencyRight,"bid");
			
			if(exchangeRate != null){
				return amountIn.multiply(exchangeRate);
			}else{
				//如果不存在  amount/汇率
				exchangeRate = getExchangeRate(currencyRight,currencyLeft,"ask");
				if(exchangeRate != null){
					return amountIn.divide(exchangeRate, 8, BigDecimal.ROUND_DOWN);
				}else{
					return new BigDecimal("-1");
				}
			}
			
			
		}
		//黄金与其他货币的兑换
		
		
		
		
		//GDQ与其他货币的兑换
		if(currencyLeft.equals("GDQ") || currencyRight.equals("GDQ")){
			
			//首先获取 1oz黄金对应的美元价值
			BigDecimal rate4XAU2USD = getExchangeRate("XAU", "USD", "bid");
			//计算1GDQ对应的美元价值
			BigDecimal rate4GDQ2USD = rate4XAU2USD.divide(new BigDecimal("10000").
					multiply(new BigDecimal(ResourceUtils.getBundleValue4String("exchange.oz4g", "31.1034768"))),
					8,BigDecimal.ROUND_DOWN);
			logger.info("rate4XAU2USD :{} ,rate4GDQ2USD : {}",rate4XAU2USD,rate4GDQ2USD);
			
			if(currencyLeft.equals("GDQ")){
				if(currencyRight.equals("USD")){
					return rate4GDQ2USD.multiply(amountIn);
				}else{
					BigDecimal rateUSD2Other = getExchangeRate("USD",currencyRight,"bid");
					if(rateUSD2Other != null){
						return amountIn.multiply(rate4GDQ2USD).multiply(rateUSD2Other);
					}else{
						BigDecimal rateOther2USD = getExchangeRate(currencyRight,"USD","ask");
						if(rateOther2USD != null){
							return rate4GDQ2USD.multiply(new BigDecimal("1")
									.divide(rateOther2USD,8,BigDecimal.ROUND_DOWN))
									.multiply(amountIn);
						}

					}
				}
			}
			
			
			if(currencyRight .equals("GDQ")){
				if(currencyLeft.equals("USD")){
					return amountIn.divide(rate4GDQ2USD, 8, BigDecimal.ROUND_DOWN);
				}else{
					BigDecimal rateOther2USD = getExchangeRate(currencyLeft,"USD","ask");
					if(rateOther2USD!= null){
						return rateOther2USD.divide(rate4GDQ2USD,8,BigDecimal.ROUND_DOWN).multiply(amountIn);
					}else{
						BigDecimal rateUSD2Other = getExchangeRate("USD",currencyLeft,"ask");
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
	
	@Override
	public Date getExchangeRateUpdateDate() {
		
		PriceInfo priceInfo = getPriceInfo("USD","CNH");
		
		if(priceInfo != null){
			String time = priceInfo.getTime().replace("T", " ").substring(0,19);
			logger.info("update time : {}",time);
			try {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return new Date();
		
	}
	
	@Override
	public BigDecimal getTotalBalance(int userId){
		logger.info("The current default currency : {}", ServerConsts.STANDARD_CURRENCY);
		List<Wallet> list = walletDAO.getWalletsByUserId(userId);
		BigDecimal totalBalance = BigDecimal.ZERO;
		if (!list.isEmpty()) {
			for (Wallet wallet : list) {
				if (wallet.getCurrency().getCurrency().equals(ServerConsts.STANDARD_CURRENCY)) {
					totalBalance = totalBalance.add(wallet.getBalance());
				} else {
					totalBalance = totalBalance.add(
							getDefaultCurrencyAmount(wallet.getCurrency().getCurrency(), wallet.getBalance(),"bid"));
				}
			}
		}
		BigDecimal out = totalBalance.setScale(4, RoundingMode.DOWN);
		logger.info("Total assets of the current account : {}", out);
		return out;
	}
	

	
	@Override
	public HashMap<String, Double> getExchangeRate(String base,String type) {
		
		HashMap<String, Double> map = new HashMap<>();

		List<Currency> list = currencyDAO.getCurrencys();

		for (Currency currency : list) {

			if (!currency.getCurrency().equals(base)) {
				BigDecimal value = getSingleExchangeRate(base, currency.getCurrency());
				map.put(currency.getCurrency(), value.doubleValue());
			}
		}

		return map;
		
	}
	@Override
	public BigDecimal getSingleExchangeRate(String currencyLeft, String currencyRight) {
		if((!currencyLeft.equals("GDQ")&&!currencyLeft.equals("XAU"))
				&&(!currencyRight.equals("GDQ")&&!currencyRight.equals("XAU"))){
			
			//获取汇率，如果存在  amountIn*汇率
			BigDecimal exchangeRate = getExchangeRate(currencyLeft,currencyRight,"bid");
			
			if(exchangeRate != null){
				return exchangeRate;
			}else{
				//如果不存在  amount/汇率
				exchangeRate = getExchangeRate(currencyRight,currencyLeft,"ask");
				if(exchangeRate != null){
					return (new BigDecimal("1")).divide(exchangeRate, 8, BigDecimal.ROUND_DOWN);
				}else{
					return new BigDecimal("-1");
				}
			}
		}
		//黄金与其他货币的兑换
		
		
		
		
		//GDQ与其他货币的兑换
		if(currencyLeft.equals("GDQ") || currencyRight.equals("GDQ")){
			
			//首先获取 1oz黄金对应的美元价值
			BigDecimal rate4XAU2USD = getExchangeRate("XAU", "USD", "bid");
			//计算1GDQ对应的美元价值
			BigDecimal rate4GDQ2USD = rate4XAU2USD.divide(new BigDecimal("10000").
					multiply(new BigDecimal(ResourceUtils.getBundleValue4String("exchange.oz4g", "31.1034768"))),
					8,BigDecimal.ROUND_DOWN);
			logger.info("rate4XAU2USD :{} ,rate4GDQ2USD : {}",rate4XAU2USD,rate4GDQ2USD);
			
			if(currencyLeft.equals("GDQ")){
				if(currencyRight.equals("USD")){
					return rate4GDQ2USD;
				}else{
					BigDecimal rateUSD2Other = getExchangeRate("USD",currencyRight,"bid");
					if(rateUSD2Other != null){
						return rate4GDQ2USD.multiply(rateUSD2Other);
					}else{
						BigDecimal rateOther2USD = getExchangeRate(currencyRight,"USD","ask");
						if(rateOther2USD != null){
							return rate4GDQ2USD.multiply(new BigDecimal("1")
									.divide(rateOther2USD,8,BigDecimal.ROUND_DOWN));
						}

					}
				}
			}
			
			
			if(currencyRight .equals("GDQ")){
				if(currencyLeft.equals("USD")){
					return (new BigDecimal("1")).divide(rate4GDQ2USD, 8, BigDecimal.ROUND_DOWN);
				}else{
					BigDecimal rateOther2USD = getExchangeRate(currencyLeft,"USD","ask");
					if(rateOther2USD!= null){
						return rateOther2USD.divide(rate4GDQ2USD,8,BigDecimal.ROUND_DOWN);
					}else{
						BigDecimal rateUSD2Other = getExchangeRate("USD",currencyLeft,"ask");
						if(rateUSD2Other != null){
							return new BigDecimal("1")
									.divide(rate4GDQ2USD.multiply(rateUSD2Other),8,BigDecimal.ROUND_DOWN);
						}
					}
				}
			}

		}
		
		return null;
	}
	


	///////////////////////////////////////////////////////////////////////////////////////
	
	private String saveExchangeRatesAllParams(List<Currency> currencies){
		
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
	
	private void saveExchangeRatesMultiParams(String instruments){
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
	
	
	public PriceInfo getPriceInfo(String currencyLeft,String currencyRight) {
		
		String param = generateParam(currencyLeft,currencyRight);
		
		String redisData = redisDAO.getValueByKey(oandaRatesPrefix.replace("[key]", param));
		
		if(StringUtils.isNotBlank(redisData)){
			PriceInfo priceInfo = JsonBinder.getInstance().fromJson(redisData, PriceInfo.class);
			return priceInfo;
		}
		return null;
	}
	
	
	private BigDecimal getExchangeRate(String currencyLeft,String currencyRight,String type){
		
		String param = generateParam(currencyLeft,currencyRight);
		
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
	
	
	

	private OandaRespData getCurrentPrices(String instruments){
		
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

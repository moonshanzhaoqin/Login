package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.CommonManager;
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
	CommonManager commonManager;
	
	@Autowired
	RedisDAO redisDAO;
	
	
	public static Logger logger = LoggerFactory.getLogger(OandaRatesManagerImpl.class);
	
	private final String oandaRatesPrefix = "oanda_rates_[key]";
	
	private final String URL_AND_SYMBOL = "%2C";

	@Override
	public void updateExchangeRates(){
		String instruments = redisDAO.getValueByKey(oandaRatesPrefix.replace("[key]", "instruments"));
		instruments = instruments == null ? "" : instruments;
		logger.info("instruments from redis : {}",instruments);
		instruments = incrementalUpdateExchangeRates(instruments);
		saveExchangeRatesMultiParams(instruments);
	}
	
	@Override
	public BigDecimal getExchangedAmount(String currencyLeft,BigDecimal amountIn,String currencyRight,String type){
		
		BigDecimal result ;
		
		
		if((!currencyLeft.equals(ServerConsts.CURRENCY_OF_GOLDPAY)&&!currencyLeft.equals(ServerConsts.CURRENCY_OF_GOLD))
				&&(!currencyRight.equals(ServerConsts.CURRENCY_OF_GOLDPAY)&&!currencyRight.equals(ServerConsts.CURRENCY_OF_GOLD))){
			
			//获取汇率，如果存在  amountIn*汇率
			BigDecimal exchangeRate = getExchangeRate(currencyLeft,currencyRight,"bid");
			
			if(exchangeRate != null){
				
				result = amountIn.multiply(exchangeRate);
				
				logger.info("amount : {} ,bid rate : {},result : {}",amountIn,exchangeRate,result);
	
				return result;
			}else{
				//如果不存在  amount/汇率
				exchangeRate = getExchangeRate(currencyRight,currencyLeft,"ask");
				if(exchangeRate != null){
					
					result = amountIn.divide(exchangeRate, 8, BigDecimal.ROUND_DOWN);
					logger.info("amount : {} ,ask rate : {},result : {}",amountIn,exchangeRate,result);
					
					return result;
				}else{
					return new BigDecimal("-1");
				}
			}
			
			
		}
		//黄金与其他货币的兑换
		
		
		
		
		//GDQ与其他货币的兑换
		if(currencyLeft.equals(ServerConsts.CURRENCY_OF_GOLDPAY) || currencyRight.equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
			
			//首先获取 1oz黄金对应的美元价值
			BigDecimal rate4XAU2USD = getExchangeRate(ServerConsts.CURRENCY_OF_GOLD, ServerConsts.CURRENCY_OF_USD, "ask");
			//计算1GDQ对应的美元价值
			BigDecimal rate4GDQ2USD = rate4XAU2USD.divide(
					new BigDecimal("10000").multiply(new BigDecimal(ResourceUtils.getBundleValue4String("exchange.oz4g", "31.1034768"))),
					8,BigDecimal.ROUND_DOWN);
			logger.info("rate4XAU2USD :{} ,rate4GDQ2USD : {}",rate4XAU2USD,rate4GDQ2USD);
			
			if(currencyLeft.equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
				if(currencyRight.equals(ServerConsts.CURRENCY_OF_USD)){
					return rate4GDQ2USD.multiply(amountIn);
				}else{
					BigDecimal rateUSD2Other = getExchangeRate(ServerConsts.CURRENCY_OF_USD,currencyRight,"bid");
					if(rateUSD2Other != null){
						return amountIn.multiply(rate4GDQ2USD).multiply(rateUSD2Other);
					}else{
						BigDecimal rateOther2USD = getExchangeRate(currencyRight,ServerConsts.CURRENCY_OF_USD,"ask");
						if(rateOther2USD != null){
							return rate4GDQ2USD.multiply(new BigDecimal("1")
									.divide(rateOther2USD,8,BigDecimal.ROUND_DOWN))
									.multiply(amountIn);
						}

					}
				}
			}
			
			
			if(currencyRight .equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
				if(currencyLeft.equals(ServerConsts.CURRENCY_OF_USD)){
					return amountIn.divide(rate4GDQ2USD, 8, BigDecimal.ROUND_DOWN);
				}else{
					BigDecimal rateOther2USD = getExchangeRate(currencyLeft,ServerConsts.CURRENCY_OF_USD,"ask");
					if(rateOther2USD!= null){
						return rateOther2USD.divide(rate4GDQ2USD,8,BigDecimal.ROUND_DOWN).multiply(amountIn);
					}else{
						BigDecimal rateUSD2Other = getExchangeRate(ServerConsts.CURRENCY_OF_USD,currencyLeft,"ask");
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
		
		PriceInfo priceInfo = getPriceInfo(ServerConsts.CURRENCY_OF_USD,ServerConsts.CURRENCY_OF_CNH);
		
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

		List<Currency> list = commonManager.getCurrentCurrencies();

		for (Currency currency : list) {

			if (!currency.getCurrency().equals(base)) {
				BigDecimal value = getSingleExchangeRate(base, currency.getCurrency());
				if(value != null){
					map.put(currency.getCurrency(), value.doubleValue());
				}
			}
		}

		return map;
		
	}
	@Override
	public BigDecimal getSingleExchangeRate(String currencyLeft, String currencyRight) {
		if((!currencyLeft.equals(ServerConsts.CURRENCY_OF_GOLDPAY)&&!currencyLeft.equals(ServerConsts.CURRENCY_OF_GOLD))
				&&(!currencyRight.equals(ServerConsts.CURRENCY_OF_GOLDPAY)&&!currencyRight.equals(ServerConsts.CURRENCY_OF_GOLD))){
			
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
		if(currencyLeft.equals(ServerConsts.CURRENCY_OF_GOLDPAY) || currencyRight.equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
			
			//首先获取 1oz黄金对应的美元价值
			BigDecimal rate4XAU2USD = getExchangeRate(ServerConsts.CURRENCY_OF_GOLD, ServerConsts.CURRENCY_OF_USD, "ask");
			//计算1GDQ对应的美元价值
			BigDecimal rate4GDQ2USD = rate4XAU2USD.divide(new BigDecimal("10000").
					multiply(new BigDecimal(ResourceUtils.getBundleValue4String("exchange.oz4g", "31.1034768"))),
					8,BigDecimal.ROUND_DOWN);
			logger.info("rate4XAU2USD :{} ,rate4GDQ2USD : {}",rate4XAU2USD,rate4GDQ2USD);
			
			if(currencyLeft.equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
				if(currencyRight.equals(ServerConsts.CURRENCY_OF_USD)){
					return rate4GDQ2USD;
				}else{
					BigDecimal rateUSD2Other = getExchangeRate(ServerConsts.CURRENCY_OF_USD,currencyRight,"bid");
					if(rateUSD2Other != null){
						return rate4GDQ2USD.multiply(rateUSD2Other);
					}else{
						BigDecimal rateOther2USD = getExchangeRate(currencyRight,ServerConsts.CURRENCY_OF_USD,"ask");
						if(rateOther2USD != null){
							return rate4GDQ2USD.multiply(new BigDecimal("1")
									.divide(rateOther2USD,8,BigDecimal.ROUND_DOWN));
						}

					}
				}
			}
			
			
			if(currencyRight .equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
				if(currencyLeft.equals(ServerConsts.CURRENCY_OF_USD)){
					return (new BigDecimal("1")).divide(rate4GDQ2USD, 8, BigDecimal.ROUND_DOWN);
				}else{
					BigDecimal rateOther2USD = getExchangeRate(currencyLeft,ServerConsts.CURRENCY_OF_USD,"ask");
					if(rateOther2USD!= null){
						return rateOther2USD.divide(rate4GDQ2USD,8,BigDecimal.ROUND_DOWN);
					}else{
						BigDecimal rateUSD2Other = getExchangeRate(ServerConsts.CURRENCY_OF_USD,currencyLeft,"ask");
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
	
	@Override
	public List<PriceInfo> getAllPrices(){
		
		List<Currency> currencies = commonManager.getAllCurrencies();
		List<PriceInfo> priceInfos = new LinkedList<>();
		
		for (Currency currency1 : currencies) {
			for (Currency currency2 : currencies) {
				if(!currency1.equals(currency2)){
					PriceInfo priceInfo = getPriceInfo(currency1.getCurrency(),currency2.getCurrency());
					if(priceInfo != null){
						priceInfos.add(priceInfo);
					}
				}
			}
		}

		PriceInfo priceInfo = getPriceInfo(ServerConsts.CURRENCY_OF_GOLD,ServerConsts.CURRENCY_OF_USD);
		if(priceInfo!= null){
			priceInfos.add(priceInfo);
		}
		
		
		return priceInfos;
		
	}
	
	@Override
	public BigDecimal getInputValue(String currencyLeft,BigDecimal amount, String currencyRight){
		
		if((!currencyLeft.equals(ServerConsts.CURRENCY_OF_GOLDPAY)&&!currencyLeft.equals(ServerConsts.CURRENCY_OF_GOLD))
				&&(!currencyRight.equals(ServerConsts.CURRENCY_OF_GOLDPAY)&&!currencyRight.equals(ServerConsts.CURRENCY_OF_GOLD))){
			
			BigDecimal exchangeRate = getExchangeRate(currencyLeft,currencyRight,"bid");
			if(exchangeRate != null){
				return amount.divide(exchangeRate,8, BigDecimal.ROUND_UP);
			}else{
				exchangeRate = getExchangeRate(currencyRight,currencyLeft,"ask");
				if(exchangeRate!= null){
					return amount.multiply(exchangeRate);
				}
			}
			
		}else{
			//首先获取 1oz黄金对应的美元价值
			BigDecimal rate4XAU2USD = getExchangeRate(ServerConsts.CURRENCY_OF_GOLD, ServerConsts.CURRENCY_OF_USD, "ask");
			//计算1GDQ对应的美元价值
			BigDecimal rate4GDQ2USD = rate4XAU2USD.divide(new BigDecimal("10000").
					multiply(new BigDecimal(ResourceUtils.getBundleValue4String("exchange.oz4g", "31.1034768"))),
					8,BigDecimal.ROUND_DOWN);
			
			if(currencyLeft.equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
				
				if(currencyRight.equals(ServerConsts.CURRENCY_OF_USD)){
					return amount.divide(rate4GDQ2USD, 8, BigDecimal.ROUND_UP);
				}else{
					BigDecimal rateUSD2Other = getExchangeRate(ServerConsts.CURRENCY_OF_USD, currencyRight, "bid");
					if(rateUSD2Other != null){
						return amount.divide(rateUSD2Other.multiply(rate4GDQ2USD),8, BigDecimal.ROUND_UP);
					}else{
						BigDecimal rateOther2USD = getExchangeRate(currencyRight,ServerConsts.CURRENCY_OF_USD,  "ask");
						if(rateOther2USD != null){
							return amount.multiply(rateOther2USD).
									divide(rate4GDQ2USD, 8, BigDecimal.ROUND_UP);
						}
					}
				}
			}
			
			if(currencyRight .equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
				if(currencyLeft.equals(ServerConsts.CURRENCY_OF_USD)){
					return amount.multiply(rate4GDQ2USD);
				}else{
					BigDecimal rateOther2USD = getExchangeRate(currencyLeft,ServerConsts.CURRENCY_OF_USD,"ask");
					if(rateOther2USD!= null){
						return amount.multiply(rate4GDQ2USD).divide(rateOther2USD,8,BigDecimal.ROUND_DOWN);
					}else{
						BigDecimal rateUSD2Other = getExchangeRate(ServerConsts.CURRENCY_OF_USD,currencyLeft,"ask");
						if(rateUSD2Other != null){
							return amount.multiply(rate4GDQ2USD).multiply(rateUSD2Other);
						}
					}
				}
			}
			
			
			
		}

		return null;
		
	}

	private String incrementalUpdateExchangeRates(String existentInstruments){
		String replacedStr = existentInstruments.replaceAll(ServerConsts.CURRENCY_OF_CNH, ServerConsts.CURRENCY_OF_CNY).replaceAll(ServerConsts.CURRENCY_OF_GOLD, ServerConsts.CURRENCY_OF_GOLDPAY);
		String[] existentInstrumentsList = replacedStr.split(URL_AND_SYMBOL);
		for (String[] instrument : commonManager.getInstruments()) {
			if (!ArrayUtils.contains(existentInstrumentsList, instrument[0]) && !ArrayUtils.contains(existentInstrumentsList, instrument[1])) {
				String instrumentTmp = instrument[0].replace(ServerConsts.CURRENCY_OF_CNY, ServerConsts.CURRENCY_OF_CNH).replace(ServerConsts.CURRENCY_OF_GOLDPAY, ServerConsts.CURRENCY_OF_GOLD);
				logger.info("instruments : {}",instrumentTmp);
				if (saveExchangeRatesMultiParams(instrumentTmp)) {
					existentInstruments = generateParams(instrumentTmp, existentInstruments);
				}else{
					instrumentTmp = instrument[1].replace(ServerConsts.CURRENCY_OF_CNY, ServerConsts.CURRENCY_OF_CNH).replace(ServerConsts.CURRENCY_OF_GOLDPAY, ServerConsts.CURRENCY_OF_GOLD);
					logger.info("instruments : {}",instrumentTmp);
					if (saveExchangeRatesMultiParams(instrumentTmp)) {
						existentInstruments = generateParams(instrumentTmp, existentInstruments);
					}
				}
			}
		}
		redisDAO.saveData(oandaRatesPrefix.replace("[key]", "instruments"), existentInstruments);
		return existentInstruments;
	}
	
	private String generateParams(String updateInstrument, String existentInstruments){
		StringBuilder stringBuilder = new StringBuilder(existentInstruments);
		if (StringUtils.isNotBlank(stringBuilder.toString())) {
			stringBuilder.append(URL_AND_SYMBOL).append(updateInstrument);
		}else{
			stringBuilder.append(updateInstrument);
		}
		return stringBuilder.toString();
	}
	
	private boolean saveExchangeRatesMultiParams(String instruments){
		OandaRespData oandaRespData = getCurrentPrices(instruments);
		if(oandaRespData!= null){
			PriceInfo[] prices = oandaRespData.getPrices();
			for (PriceInfo priceInfo : prices) {
				//保存到redis中
				logger.info("update PriceInfo : {}", priceInfo.toString());
				String jsonStr = JsonBinder.getInstance().toJson(priceInfo);
				redisDAO.saveData(oandaRatesPrefix.replace("[key]", priceInfo.getInstrument()), jsonStr);
			}
			return true;
		}else{
			return false;
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
		
		String domain = ResourceUtils.getBundleValue4String("oanda.exchangerate.url", "https://api-fxpractice.oanda.com/v1/prices");
		String bearer = ResourceUtils.getBundleValue4String("oanda.exchangerate.key", "d413e2cd916ebc4613376c3a3ca826ae-ebdc8079ec4cca1b1d650ea030036226");
		String params = "instruments="+instruments;
		BasicHeader basicHeader = new BasicHeader("Authorization", 
				"Bearer " + bearer);
		String result = HttpClientUtils.sendGet(domain,params,basicHeader);
//		logger.info("result : {}",result);
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
		if(result.contains(ServerConsts.CURRENCY_OF_CNY)){
			return result.replace(ServerConsts.CURRENCY_OF_CNY, ServerConsts.CURRENCY_OF_CNH);
		}
		return result;
		
	}
	

}

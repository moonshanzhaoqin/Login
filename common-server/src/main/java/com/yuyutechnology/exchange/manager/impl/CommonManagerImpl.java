/**
 * 
 */
package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.ConfigDAO;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dto.MsgFlagInfo;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.utils.ResourceUtils;

/**
 * @author silent.sun
 *
 */
@Service
public class CommonManagerImpl implements CommonManager {

	public static Logger logger = LogManager.getLogger(CommonManagerImpl.class);

	@Autowired
	WalletDAO walletDAO;
	@Autowired
	CurrencyDAO currencyDAO;
	@Autowired
	ConfigDAO configDAO;
	@Autowired
	RedisDAO redisDAO;

	private Map<String, Currency> allCurrenciesMap = new HashMap<String, Currency>();
	private List<Currency> allCurrencies = new ArrayList<Currency>();
	private Map<String, Currency> currentCurrenciesMap = new HashMap<String, Currency>();
	private List<Currency> currentCurrencies = new ArrayList<Currency>();
	private List<String[]> instruments = new ArrayList<String[]>();

	@Override
	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void refreshConfig() {
		ResourceUtils.clearCache();
		initCurrency();
	}

	private void initCurrency() {
		allCurrencies = currencyDAO.getCurrencys();
		List<Currency> currentCurrenciesTmp = new ArrayList<Currency>();
		for (Currency currency : allCurrencies) {
			allCurrenciesMap.put(currency.getCurrency(), currency);
			if (currency.getCurrencyStatus() == ServerConsts.CURRENCY_AVAILABLE) {
				currentCurrenciesTmp.add(currency);
				currentCurrenciesMap.put(currency.getCurrency(), currency);
			}else{
				currentCurrenciesMap.remove(currency.getCurrency());
			}
		}
		currentCurrencies = currentCurrenciesTmp;
		initInstruments();
	}
	
	private void initInstruments() {
		List<String[]> temp = new ArrayList<String[]>();
		int currencySize = allCurrencies.size();
		for (int i = 0; i < currencySize; i++) {
			String left = allCurrencies.get(i).getCurrency();
			if (!ServerConsts.CURRENCY_OF_GOLDPAY.equals(left)) {
				for (int f = i + 1; f < currencySize; f++) {
					if (!ServerConsts.CURRENCY_OF_GOLDPAY.equals(left)) {
						String right = allCurrencies.get(f).getCurrency();
						temp.add(new String[]{left +"_"+right,right +"_"+left});
					}
				}
			}
		}
		temp.add(new String[]{ServerConsts.CURRENCY_OF_GOLDPAY +"_"+ServerConsts.CURRENCY_OF_USD,ServerConsts.CURRENCY_OF_USD +"_"+ServerConsts.CURRENCY_OF_GOLDPAY});
		instruments = temp;
	}

	@Override
	public List<Currency> getAllCurrencies() {
		return allCurrencies;
	}

	@Override
	public List<Currency> getCurrentCurrencies() {
		return currentCurrencies;
	}

	@Override
	public Currency getCurrentCurreny(String currency) {
		return currentCurrenciesMap.get(currency);
	}

	@Override
	public Currency getCurreny(String currency) {
		return allCurrenciesMap.get(currency);
	}

	@Override
	public boolean verifyCurrency(String currency) {
		return currentCurrenciesMap.get(currency) != null;
	}
	
	@Override
	public List<String[]> getInstruments() {
		return instruments;
	}

	@Override
	public void addMsgFlag(int userId, int type) {
		opsMsgFlag(userId, type, true);
	}

	@Override
	public void readMsgFlag(int userId, int type) {
		opsMsgFlag(userId, type, false);
	}

	public MsgFlagInfo getMsgFlag(int userId) {
		MsgFlagInfo info = new MsgFlagInfo();
		String msgFlagNewTransKey = "msgFlagNewTrans";
		String msgFlagNewRequestTransKey = "msgFlagNewRequestTrans";
		String msgFlagNewTrans = redisDAO.getData4Hash(msgFlagNewTransKey, userId + "");
		String msgFlagNewRequestTrans = redisDAO.getData4Hash(msgFlagNewRequestTransKey, userId + "");
		info.setNewTrans(Boolean.valueOf(msgFlagNewTrans));
		info.setNewRequestTrans(Boolean.valueOf(msgFlagNewRequestTrans));
		return info;
	}

	private void opsMsgFlag(int userId, int type, boolean on) {
		String msgFlagNewTransKey = "msgFlagNewTrans";
		String msgFlagNewRequestTransKey = "msgFlagNewRequestTrans";
		if (type == 1) {
			redisDAO.saveData4Hash(msgFlagNewTransKey, userId + "", Boolean.valueOf(on).toString());
		} else {
			redisDAO.saveData4Hash(msgFlagNewRequestTransKey, userId + "", Boolean.valueOf(on).toString());
		}
	}

	@Override
	public List<String> getAllConfigurableCurrencies() {
//		List<String> currencies = new ArrayList<String>();
//		currencies.add(ServerConsts.STANDARD_CURRENCY);
//		currencies.add(ServerConsts.CURRENCY_OF_GOLDPAY);
//		String result = redisDAO.getValueByKey("redis_exchangeRate");
//		if (StringUtils.isNotBlank(result)) {
//			@SuppressWarnings("unchecked")
//			HashMap<String, String> map = JsonBinder.getInstance().fromJson(result, HashMap.class);
//			String value = map.get(ServerConsts.STANDARD_CURRENCY);
//			ExchangeRate exchangeRate = JsonBinder.getInstanceNonNull().fromJson(value, ExchangeRate.class);
//			currencies.addAll(exchangeRate.getRates().keySet());
//			return currencies;
//		}
//		String[] currency = {"USD","GDQ","AUD","BGN","BRL","CAD","CHF","CNY","CZK","DKK","GBP","HKD","HRK","HUF","IDR","ILS","INR","JPY","KRW","MXN","MYR","NOK","NZD","PHP","PLN","RON","RUB","SEK","SGD","THB","TRY","ZAR","EUR"};
		String[] currency = {"AUD","CAD","CHF","CNY","EUR","GBP","HKD","JPY","USD","GDQ"};
		return Arrays.asList(currency);
	}

	@Override
	public void checkAndUpdateWallet(Integer userId, String currency) {
		logger.info("Update Wallet==>");
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
		if (wallet == null) {
			// 没有该货币的钱包，需要新增
			walletDAO.addwallet(new Wallet(allCurrenciesMap.get(currency), userId, BigDecimal.ZERO, new Date(),0));
			logger.info("Added {} wallet to user {}", currency, userId);
		}
	}
}

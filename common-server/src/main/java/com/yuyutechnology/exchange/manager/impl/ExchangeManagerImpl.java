package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.ResourceUtiles;

@Service
public class ExchangeManagerImpl implements ExchangeManager {
	
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	CurrencyDAO currencyDAO;
	
	public static Logger logger = LoggerFactory.getLogger(ExchangeManagerImpl.class);

	@Override
	public List<Wallet> getWalletsByUserId(int userId) {
		return walletDAO.getWalletsByUserId(userId);
	}

	@Override
	public void exchangeCalculation(int userId,String currencyOut, String currencyIn, BigDecimal amountOut) {
		//首先判断输入金额是否超过余额
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currencyOut);
		if(amountOut.compareTo(wallet.getBalance()) == 1){
			logger.warn("The output amount is greater than the balance");
		}
		//然后判断换算后金额是否超过最小限额
		BigDecimal result = null;
		if(currencyOut.equals(ServerConsts.CURRENCY_OF_GOLDPAY) && result.compareTo(new BigDecimal(1)) == 1){
			
		}else if(!currencyOut.equals(ServerConsts.CURRENCY_OF_GOLDPAY) && result.compareTo(new BigDecimal(0.01)) == 1){
			
		}else{
			logger.warn("The amount of the conversion is less than the minimum transaction amount");
		}

	}

}

package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.manager.WalletManager;
import com.yuyutechnology.exchange.pojo.Wallet;

@Service
public class WalletManagerImpl implements WalletManager {

	
	@Autowired
	WalletDAO walletDAO;
	
	@Autowired
	ExchangeRateManager exchangeRateManager;
	
	public static Logger logger = LogManager.getLogger(WalletManagerImpl.class);
	
	@Override
	public double getTotalAmoutGold(int userId) {
		
		BigDecimal goldpayAmount = new BigDecimal("0");

		List<Wallet> list = walletDAO.getWalletsByUserId(userId);
		
		if(list.isEmpty()){
			return 0;
		}
		
		for (Wallet wallet : list) {
			
			if(!wallet.getCurrency().getCurrency().equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
				
				double exchangeRate = exchangeRateManager.getExchangeRate(
						wallet.getCurrency().getCurrency(), ServerConsts.CURRENCY_OF_GOLDPAY);
				
				logger.info("base : {} for goldpay ,exchangeRate : {}" ,wallet.getCurrency().getCurrency(),exchangeRate);
				
				goldpayAmount = goldpayAmount.add(wallet.getBalance().multiply(new BigDecimal(Double.toString(exchangeRate))));
				
			}else{
				goldpayAmount = goldpayAmount.add(wallet.getBalance());
			}
		}

		logger.info("user id : {} ,Currently has total goldpay : {}",userId,goldpayAmount);
		BigDecimal goldAmount = goldpayAmount.divide(new BigDecimal("10000"),2,BigDecimal.ROUND_FLOOR);
		logger.info("user id : {} ,Currently has total gold : {}",userId ,goldAmount);
		
		return goldAmount.doubleValue();
	}

	@Override
	public void getUserTotalBalance(int systemUserId) {
		walletDAO.getUserAccountTotalAssets(systemUserId);
	}

}

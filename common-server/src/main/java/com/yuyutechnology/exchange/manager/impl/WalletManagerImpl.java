package com.yuyutechnology.exchange.manager.impl;

import java.text.DecimalFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	public static Logger logger = LoggerFactory.getLogger(WalletManagerImpl.class);
	
	@Override
	public double getTotalAmoutGold(int userId) {
		
		double goldpayAmount = 0;

		List<Wallet> list = walletDAO.getWalletsByUserId(userId);
		
		if(list.isEmpty()){
			return 0;
		}
		
		for (Wallet wallet : list) {
			
			if(!wallet.getCurrency().getCurrency().equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
				
				double exchangeRate = exchangeRateManager.getExchangeRate(
						wallet.getCurrency().getCurrency(), ServerConsts.CURRENCY_OF_GOLDPAY);
				
				logger.info("base : {} for goldpay ,exchangeRate : {}" ,wallet.getCurrency().getCurrency(),exchangeRate);
				
				goldpayAmount = goldpayAmount+wallet.getBalance().longValue()*exchangeRate;
				
			}else{
				goldpayAmount = goldpayAmount+wallet.getBalance().longValue();
			}
		}
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		logger.info("user id : {} ,Currently has total goldpay : {}",userId,goldpayAmount);
		Double goldAmount = Double.parseDouble(df.format(goldpayAmount/10000));
		
		logger.info("user id : {} ,Currently has total gold : {}",userId ,goldAmount);
		
		return goldAmount;
	}

}

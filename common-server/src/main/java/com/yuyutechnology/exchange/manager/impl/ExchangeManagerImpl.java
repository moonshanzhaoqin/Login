package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.ExchangeDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.pojo.Exchange;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.pojo.WalletSeq;

@Service
public class ExchangeManagerImpl implements ExchangeManager {
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	CurrencyDAO currencyDAO;
	@Autowired
	ExchangeDAO exchangeDAO;
	@Autowired
	WalletSeqDAO walletSeqDAO;

	
	@Autowired
	ExchangeRateManager exchangeRateManager;
	
	
	public static Logger logger = LoggerFactory.getLogger(ExchangeManagerImpl.class);

	@Override
	public List<Wallet> getWalletsByUserId(int userId) {
		return walletDAO.getWalletsByUserId(userId);
	}

	@Override
	public String exchangeCalculation(int userId,String currencyOut, String currencyIn, BigDecimal amountOut) {
		//首先判断输入金额是否超过余额
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currencyOut);
		if(wallet == null){
			logger.warn("The user's information can not be queried");
			return ServerConsts.EXCHANGE_WALLET_CAN_NOT_BE_QUERIED;
		}
		if(amountOut.compareTo(wallet.getBalance()) == 1){
			logger.warn("The output amount is greater than the balance");
			return ServerConsts.EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE;
		}
		//然后判断换算后金额是否超过最小限额
		double exchangeRate = exchangeRateManager.getExchangeRate(currencyOut, currencyIn);
		BigDecimal result = amountOut.multiply(new BigDecimal(exchangeRate));
		logger.info("out : "+amountOut+" exchangeRate : "+exchangeRate+"result : "+result);
		if(currencyOut.equals(ServerConsts.CURRENCY_OF_GOLDPAY) && result.compareTo(new BigDecimal(1)) == 1){
			
		}else if(!currencyOut.equals(ServerConsts.CURRENCY_OF_GOLDPAY) && result.compareTo(new BigDecimal(0.01)) == 1){
			
		}else{
			logger.warn("The amount of the conversion is less than the minimum transaction amount");
			return ServerConsts.EXCHANGE_AMOUNT_LESS_THAN_MINIMUM_TRANSACTION_AMOUNT;
		}
		return result.toString();
	}

	@Override
	public String exchangeConfirm(int userId, String currencyOut, String currencyIn,
			BigDecimal amountOut,BigDecimal amountIn) {
		String result = exchangeCalculation(userId,currencyOut,currencyIn,amountOut);
		if(result.contains("_")){
			return result;
		}else{
			//用户账户
			//扣款
			walletDAO.updateWalletByUserIdAndCurrency(userId, currencyOut, amountOut, "-");
			//加款
//			walletDAO.updateWalletByUserIdAndCurrency(userId, currencyIn, amountIn, "+");
			walletDAO.updateWalletByUserIdAndCurrency(userId, currencyIn, new BigDecimal(result), "+");
			
			//系统账户
			int systemUserId = userDAO.getSystemUser().getUserId(); 
			//加款
			walletDAO.updateWalletByUserIdAndCurrency(systemUserId, currencyOut, amountOut, "+");
			//扣款
//			walletDAO.updateWalletByUserIdAndCurrency(systemUserId, currencyIn, amountIn, "-");
			walletDAO.updateWalletByUserIdAndCurrency(systemUserId, currencyIn, new BigDecimal(result), "-");
			
			String exchangeId = exchangeDAO.createExchangeId(ServerConsts.TRANSFER_TYPE_OF_EXCHANGE);
			//添加Exchange记录
			Exchange exchange = new Exchange();
			exchange.setExchangeId(exchangeId);
			exchange.setUserId(userId);
			exchange.setCurrencyOut(currencyOut);
			exchange.setAmountOut(amountOut);
			exchange.setCurrencyIn(currencyIn);
//			exchange.setAmountIn(amountIn);
			exchange.setAmountIn(new BigDecimal(result));
			exchange.setCreateTime(new Date());
			exchange.setFinishTime(new Date());
			exchange.setExchangeRate(new BigDecimal(exchangeRateManager.getExchangeRate(currencyOut, currencyIn)));
			
			exchangeDAO.addExchange(exchange);
			
			//添加seq记录
			walletSeqDAO.addWalletSeq(userId, ServerConsts.TRANSFER_TYPE_OF_EXCHANGE, exchangeId, 
					currencyOut, amountOut, currencyIn, amountIn);
			walletSeqDAO.addWalletSeq(systemUserId, ServerConsts.TRANSFER_TYPE_OF_EXCHANGE, exchangeId, 
					currencyIn, amountIn, currencyOut, amountOut);
		}
		
		return ServerConsts.RET_CODE_SUCCESS;
	}

}

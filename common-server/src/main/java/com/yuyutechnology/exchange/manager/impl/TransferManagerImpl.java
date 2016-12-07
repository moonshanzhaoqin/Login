package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;

@Service
public class TransferManagerImpl implements TransferManager{
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	TransferDAO transferDAO;
	
	public static Logger logger = LoggerFactory.getLogger(TransferManagerImpl.class);

	@Override
	public String transferInitiate(int userId,String areaCode,String userPhone, String currency, 
			BigDecimal amount, String transferComment) {
		//判断余额是否足够支付
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
		if(wallet == null || wallet.getBalance().compareTo(amount) == -1){
			logger.warn("Current balance is insufficient");
			return ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT;
		}
		//当日累加金额
		BigDecimal dayCumulativeAmount =  new BigDecimal(20000);
		//当日最大金额
		BigDecimal dayMaxAmount =  new BigDecimal(20000);
		//判断是否超过当日累加金额
		if(dayCumulativeAmount.add(amount).compareTo(dayMaxAmount) == 1){
			logger.warn("Exceeded the day's transaction limit");
			return ServerConsts.TRANSFER_EXCEEDED_TRANSACTION_LIMIT;
		}
		
		User receiver = userDAO.getUserByUserPhone(areaCode, userPhone);
		
		//生成TransId
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_OF_TRANSACTION);
		
		Transfer transfer = new Transfer();
		transfer.setTransferId(transferId);
		transfer.setCreateTime(new Date());
		transfer.setCurrency(currency);
		transfer.setTransferAmount(amount);
		transfer.setTransferComment(transferComment);
		transfer.setTransferStatus(1);
		transfer.setTransferType(ServerConsts.TRANSFER_TYPE_OF_TRANSACTION);
		transfer.setUserFrom(userId);
		//判断接收人是否是已注册账号
		if(receiver != null){
			transfer.setUserTo(receiver.getUserId());
		}else{
			transfer.setUserTo(0);
		}
		transfer.setUserToPhone(areaCode+userPhone);
		
		//保存
		transferDAO.addTransfer(transfer);
		
		return transferId;
	}

}

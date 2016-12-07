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
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
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
	@Autowired
	WalletSeqDAO walletSeqDAO;
	
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
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
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

	@Override
	public String payPwdConfirm(int userId, String transferId, String userPayPwd) {
		
		User user = userDAO.getUser(userId);
		
		//验证交易密码
		if(user.getUserPayPwd().equals(userPayPwd)){
			transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_PROCESSING);
			
			//发送验证码
			
			//
			return ServerConsts.RET_CODE_SUCCESS;
		}
		
		return ServerConsts.TRANSFER_PAYMENTPWD_INCORRECT;
	}

	@Override
	public void transferConfirm(String transferId) {
		Transfer transfer = transferDAO.getTransferById(transferId);
		
		if(transfer.getUserTo() == 0){  	//交易对象没有注册账号
			
			//获取系统账号
			User systemUser = userDAO.getSystemUser();
			//扣款
			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "-");
			//加款
			walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "+");
			//添加gift记录
			
			//更改用户的当日累计金额
			
			
			
			//增加seq记录
			walletSeqDAO.addWalletSeq4Transaction(transfer.getUserFrom(), systemUser.getUserId(), 
					ServerConsts.TRANSFER_TYPE_OF_TRANSACTION, transfer.getTransferId(), 
					transfer.getCurrency(), transfer.getTransferAmount());
			
		}else{								//交易对象注册账号,交易正常进行，无需经过系统账户
			
			//扣款
			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "-");
			//加款
			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserTo(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "+");
			//更改Transfer状态
			transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
			
			//更改用户的当日累计金额
			
			//添加seq记录
			walletSeqDAO.addWalletSeq4Transaction(transfer.getUserFrom(), transfer.getUserTo(), 
					ServerConsts.TRANSFER_TYPE_OF_TRANSACTION, transfer.getTransferId(), 
					transfer.getCurrency(), transfer.getTransferAmount());
				
		}
	}

}

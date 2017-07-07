package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.TransDetailsDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
import com.yuyutechnology.exchange.pojo.TransDetails;
import com.yuyutechnology.exchange.pojo.User;


@Service
public class TransDetailsManagerImpl implements TransDetailsManager {
	
	private static Logger logger = LogManager.getLogger(AccountingManagerImpl.class);
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	TransDetailsDAO transDetailsDAO;
	
	@Override
	public void addTransDetails(String transferId,Integer payerId,Integer traderId,
			String traderName,String traderAreaCode,String traderPhone,String transCurrency,
			BigDecimal transAmount,String transRemarks,Integer transType){
		
		User payer = userDAO.getUser(payerId);
		
		switch (transType) {
			case ServerConsts.TRANSFER_TYPE_TRANSACTION:
				logger.info("the transType is {} ServerConsts.TRANSFER_TYPE_TRANSACTION",transType);
				
				TransDetails payerTransDetails = new TransDetails(transferId,payerId,
						traderName,traderAreaCode,traderPhone,transCurrency,
						transAmount.negate(),transRemarks);
				TransDetails payeeTransDetails = new TransDetails(transferId,traderId,
						payer.getUserName(),payer.getAreaCode(),payer.getUserPhone(),
						transCurrency,transAmount,transRemarks);
				
				transDetailsDAO.addTransDetails(payerTransDetails);
				transDetailsDAO.addTransDetails(payeeTransDetails);
				break;
				
			case ServerConsts.TRANSFER_TYPE_OUT_INVITE:
			case ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW:
				logger.info("the transType is {} ServerConsts.TRANSFER_TYPE_OUT_INVITE",transType);
				
				TransDetails payerTransDetails4Invite = new TransDetails(transferId,payerId,
						traderName,traderAreaCode,traderPhone,transCurrency,
						transAmount.negate(),transRemarks);
				
				transDetailsDAO.addTransDetails(payerTransDetails4Invite);
				break;
				
			case ServerConsts.TRANSFER_TYPE_TRANSACTION-1:
			case ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND:
			case ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE:
			case ServerConsts.TRANSFER_TYPE_IN_PAYPAL_RECHAEGE:
				logger.info("the transType is {} ",transType);
				
				TransDetails payerTransDetails4Refund = new TransDetails(transferId,payerId,
						traderName,traderAreaCode,traderPhone,transCurrency,
						transAmount,transRemarks);
				
				transDetailsDAO.addTransDetails(payerTransDetails4Refund);
				break;
				
//			case ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW:
//				logger.info("the transType is {} ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW",transType);
//				
//				TransDetails payerTransDetails4Withdraw = new TransDetails(transferId,payerId,
//						traderName,traderAreaCode,traderPhone,transCurrency,
//						transAmount.negate(),transRemarks);
//				
//				transDetailsDAO.addTransDetails(payerTransDetails4Withdraw);
//				break;
				
//			case ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE:
//				logger.info("the transType is {} ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE",transType);
//				
//				TransDetails payerTransDetails4Recharge = new TransDetails(transferId,payerId,
//						traderName,traderAreaCode,traderPhone,transCurrency,
//						transAmount,transRemarks);
//				
//				transDetailsDAO.addTransDetails(payerTransDetails4Recharge);
//				break;
				
//			case ServerConsts.TRANSFER_TYPE_IN_PAYPAL_RECHAEGE:
//				logger.info("the transType is {} ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE",transType);
//				
//				TransDetails payerTransDetails4PaypalRecharge = new TransDetails(transferId,payerId,
//						traderName,traderAreaCode,traderPhone,transCurrency,
//						transAmount,transRemarks);
//				
//				transDetailsDAO.addTransDetails(payerTransDetails4PaypalRecharge);
//				break;
				
	
			default:
				break;
		}

	}
	
	

}

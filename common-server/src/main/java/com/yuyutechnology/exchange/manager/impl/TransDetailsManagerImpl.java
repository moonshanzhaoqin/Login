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

	private static Logger logger = LogManager.getLogger(TransDetailsManagerImpl.class);

	@Autowired
	UserDAO userDAO;
	@Autowired
	TransDetailsDAO transDetailsDAO;

	@Override
	public void addTransDetails(String transferId, Integer payerId, Integer traderId, String traderName,
			String traderAreaCode, String traderPhone, String transCurrency, BigDecimal transAmount,
			BigDecimal transFee,String transSnapshot,String transRemarks, Integer transType) {

		switch (transType) {
		case ServerConsts.TRANSFER_TYPE_TRANSACTION:
		
			logger.info("the transType is {} ", transType);

			TransDetails payerTransDetails = new TransDetails(transferId, payerId, traderName, traderAreaCode,
					traderPhone, transCurrency, transAmount.negate(), transFee.negate(),transRemarks,transSnapshot);
			transDetailsDAO.addTransDetails(payerTransDetails);

			User payer = userDAO.getUser(payerId);
			if (payer != null) {
				TransDetails payeeTransDetails = new TransDetails(transferId, traderId, payer.getUserName(),
						payer.getAreaCode(), payer.getUserPhone(), transCurrency, transAmount,transFee,
						transRemarks,transSnapshot);
				transDetailsDAO.addTransDetails(payeeTransDetails);
			}

			break;

		case ServerConsts.TRANSFER_TYPE_OUT_INVITE:
		case ServerConsts.TRANSFER_TYPE_IN_FEE:
		case ServerConsts.TRANSFER_TYPE_IN_WITHDRAW:
			logger.info("the transType is {} ", transType);

			TransDetails payerTransDetails4Invite = new TransDetails(transferId, payerId, traderName, traderAreaCode,
					traderPhone, transCurrency, transAmount.negate(), transFee.negate(), transRemarks,transSnapshot);

			transDetailsDAO.addTransDetails(payerTransDetails4Invite);
			break;

		case ServerConsts.TRANSFER_TYPE_TRANSACTION - 1:
		case ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND:
			// case ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE:
		case ServerConsts.TRANSFER_TYPE_IN_PAYPAL_RECHAEGE:
		case ServerConsts.TRANSFER_TYPE_IN_INVITE_CAMPAIGN:
			logger.info("the transType is {} ", transType);

			TransDetails payerTransDetails4Refund = new TransDetails(transferId, payerId, traderName, traderAreaCode,
					traderPhone, transCurrency, transAmount,transFee, transRemarks,transSnapshot);

			transDetailsDAO.addTransDetails(payerTransDetails4Refund);
			break;
		case ServerConsts.TRANSFER_TYPE_IN_WITHDRAW_REFUND:
			logger.info("the transType is {} ", transType);

			TransDetails payerTransDetails4WithdrawRefund = new TransDetails(transferId, traderId, traderName, traderAreaCode,
					traderPhone, transCurrency, transAmount,transFee, transRemarks,transSnapshot);

			transDetailsDAO.addTransDetails(payerTransDetails4WithdrawRefund);
			break;

		default:
			break;
		}

	}

	@Override
	public void updateTransDetailsWhenOtherOneRegist(String transferId, Integer payerId, String userName) {

		TransDetails transDetails = transDetailsDAO.getTransDetails(payerId, transferId);

		if (transDetails != null) {

			logger.info("TransId : {}, userId : {},register name : {}", transferId, payerId, userName);

			transDetails.setTraderName(userName);
			transDetailsDAO.updateTransDetails(transDetails);

		}

	}

}

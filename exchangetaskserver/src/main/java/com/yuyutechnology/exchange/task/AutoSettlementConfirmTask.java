package com.yuyutechnology.exchange.task;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.manager.CheckManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.util.DateFormatUtils;

public class AutoSettlementConfirmTask {
	
	@Autowired
	TransferDAO transferDAO;
	
	@Autowired
	CheckManager checkManager;
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;
	
	public static Logger logger = LogManager.getLogger(AutoSettlementConfirmTask.class);
	
	public void settlementConfirm(){
		
//		Date time = DateFormatUtils.getpreDays(3);
//		
//		List<Transfer> list = transferDAO.getTransferListByTime(
//				ServerConsts.TRANSFER_STATUS_OF_PROCESSING, ServerConsts.TRANSFER_TYPE_IN_INVITE_CAMPAIGN,time);
//		
//		for (Transfer transfer : list) {
//			boolean isInsufficient = checkManager.isInsufficientBalance(
//					transfer.getUserFrom(), transfer.getCurrency(), transfer.getTransferAmount());
//			if(!isInsufficient){
//				goldpayTrans4MergeManager.updateWallet4GoldpayTrans(transfer.getTransferId());
//			}
//		}
		
	}

}

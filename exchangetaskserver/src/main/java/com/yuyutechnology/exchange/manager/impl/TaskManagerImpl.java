package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.TaskManager;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;

@Service
public class TaskManagerImpl implements TaskManager {
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	UnregisteredDAO unregisteredDAO;
	
	@Autowired
	TransDetailsManager transDetailsManager;
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;
	
	public static Logger logger = LogManager.getLogger(TaskManagerImpl.class);
	
	@Override
	public HashMap<String, Object> crtTransByUnregistered(User user,Unregistered unregistered){
		
		HashMap<String, Object> result = new HashMap<>();
		
		Transfer inviteTransfer = transferDAO.getTransferById(unregistered.getTransferId());
		User payer = userDAO.getUser(inviteTransfer.getUserFrom());
		Integer systemUserId = userDAO.getSystemUser().getUserId();
		String goldpayOrderId = null;
		if (ServerConsts.CURRENCY_OF_GOLDPAY.equals(unregistered.getCurrency())) {
			goldpayOrderId = goldpayTrans4MergeManager.getGoldpayOrderId();
		}

		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);			
		logger.info("transfer for [Invite to invite transfer] id is {}",transferId);
		
		Transfer transfer = new Transfer();
		transfer.setTransferId(transferId);
		transfer.setUserFrom(systemUserId);
		transfer.setUserTo(user.getUserId());
		transfer.setAreaCode(payer.getAreaCode());
		transfer.setPhone(payer.getUserPhone());
		transfer.setCurrency(unregistered.getCurrency());
		transfer.setTransferAmount(unregistered.getAmount());
		transfer.setCreateTime(new Date());
		transfer.setFinishTime(new Date());
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		transfer.setTransferType(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		transfer.setTransferComment(unregistered.getTransferId());
		transfer.setNoticeId(0);
		transfer.setGoldpayOrderId(goldpayOrderId);
		transferDAO.addTransfer(transfer);
		
		result.put("retCode", ServerConsts.GOLDPAY_RETURN_SUCCESS);
		result.put("transferId", transferId);
		result.put("comment", inviteTransfer.getTransferComment());
		result.put("payer", payer);
		
		return result;
	}
	
	@Override
	public void transAndConfirm(String transferId,Integer userId,Unregistered unregistered,User payer,String comment){
		goldpayTrans4MergeManager.updateWallet4GoldpayTrans(transferId);

		transDetailsManager.addTransDetails(transferId, userId, payer.getUserId(), payer.getUserName(),
				payer.getAreaCode(), payer.getUserPhone(), unregistered.getCurrency(), unregistered.getAmount(),BigDecimal.ZERO,"", comment
				, ServerConsts.TRANSFER_TYPE_TRANSACTION - 1);

		/* 更改unregistered状态 */
		unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_COMPLETED);
		unregisteredDAO.updateUnregistered(unregistered);
	}

}

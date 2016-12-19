package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.goldpay.transaction.ClientPayOrder;
import com.yuyutechnology.exchange.goldpay.transaction.PayModel;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.ResourceUtils;

@Service
public class GoldpayTransManagerImpl implements GoldpayTransManager{
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	TransferDAO transferDAO;
	

	@Override
	public HashMap<String, String> goldpayPurchase(int userId,String goldpayAccount,BigDecimal amount) {
		
		HashMap<String, String> map = new HashMap<>();
		
		User systemUser = userDAO.getSystemUser();
		User user = userDAO.getUser(userId);

		//生成TransId
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_OF_TRANSACTION);
		ClientPayOrder clientPayOrder = new ClientPayOrder();
		clientPayOrder.setOrderId(transferId);
		clientPayOrder.setPayAmount(amount.intValue());
		clientPayOrder.setFromAccountNum(goldpayAccount);
		clientPayOrder.setType(0);
		String clientId = ResourceUtils.getBundleValue("client.id");
		clientPayOrder.setClientId(clientId);
		
		String sign = DigestUtils.md5Hex(JsonBinder.getInstance().toJson(clientPayOrder)
				+ResourceUtils.getBundleValue("client.key"));
		clientPayOrder.setSign(sign);

		String result = HttpTookit.sendPost(ResourceUtils.getBundleValue("tpps.url")+"clientPay.do",
				JsonBinder.getInstance().toJson(clientPayOrder));
		
		PayModel payModel;
		
		if(!StringUtils.isEmpty(result)){
			payModel = JsonBinder.getInstance().fromJson(result, PayModel.class);
			
			Transfer transfer = new Transfer(); 
			transfer.setTransferId(transferId);
			transfer.setCreateTime(new Date());
			transfer.setCurrency(ServerConsts.CURRENCY_OF_GOLDPAY);
			transfer.setTransferAmount(amount);
			transfer.setTransferComment(payModel.getPayOrderId());
			transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
			transfer.setUserFrom(systemUser.getUserId());
			transfer.setAreaCode(user.getAreaCode());
			transfer.setPhone(user.getUserPhone());
			transfer.setUserTo(userId);
			transfer.setTransferType(ServerConsts.TRANSFER_TYPE_OF_GOLDPAYBUY);
			transfer.setNoticeId(0);
			//保存
			transferDAO.addTransfer(transfer);
			
			map.put("retCode", ServerConsts.RET_CODE_SUCCESS);
			map.put("msg", "ok");
			map.put("transferId", transferId);
		}else{
			map.put("msg", "something wrong!");
			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
		}

		return map;
	}
	

	

}

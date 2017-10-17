package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.goldpay.trans4merge.ConfirmTransactionS2C;
import com.yuyutechnology.exchange.goldpay.trans4merge.GetGoldpayOrderIdC2S;
import com.yuyutechnology.exchange.goldpay.trans4merge.GetGoldpayOrderIdS2C;
import com.yuyutechnology.exchange.goldpay.trans4merge.GetGoldpayUserC2S;
import com.yuyutechnology.exchange.goldpay.trans4merge.GetGoldpayUserS2C;
import com.yuyutechnology.exchange.goldpay.trans4merge.GoldpayTransactionC2S;
import com.yuyutechnology.exchange.goldpay.trans4merge.GoldpayUserDTO;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.util.HttpClientUtils;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.ResourceUtils;

@Service
public class GoldpayTrans4MergeManagerImpl implements GoldpayTrans4MergeManager {
	
	@Autowired
	UserDAO userDAO;
	
	public static Logger logger = LogManager.getLogger(GoldpayTrans4MergeManagerImpl.class);
	
	@Override
	public GoldpayUserDTO getGoldpayUserInfo(Integer exUserId){
		
		User user = userDAO.getUser(exUserId);
		GetGoldpayUserC2S param = new GetGoldpayUserC2S();
		param.setAreaCode(user.getAreaCode());
		param.setMobile(user.getUserPhone());
		
		String result = HttpClientUtils.sendPost(ResourceUtils.getBundleValue4String("goldpay.url") 
				+ "getMemberInfo",JsonBinder.getInstance().toJson(param));
		
		logger.info("result : {}",result);
		
		GetGoldpayUserS2C getGoldpayUserS2C = JsonBinder.
				getInstanceNonNull().fromJson(result, GetGoldpayUserS2C.class);
		
		return getGoldpayUserS2C.getGoldpayUserDTO();
		
	}
	
	@Override
	public String getGoldpayOrderId(){
		GetGoldpayOrderIdC2S param = new GetGoldpayOrderIdC2S();
		param.setType("3");
		String result = HttpClientUtils.sendPost(ResourceUtils.getBundleValue4String("goldpay.url") 
				+ "getOrderId",JsonBinder.getInstance().toJson(param));
		
		logger.info("result : {}",result);
		
		GetGoldpayOrderIdS2C getGoldpayOrderIdS2C = JsonBinder.
				getInstanceNonNull().fromJson(result, GetGoldpayOrderIdS2C.class);
		return getGoldpayOrderIdS2C.getPayOrderId();
	}
	
	@Override
	public Integer goldpayTransaction(BigDecimal balance,String payOrderId,
			String toAccountNum,String comment,String fromAccountNum){
		
		GoldpayTransactionC2S param = new GoldpayTransactionC2S();
		param.setBalance(balance.intValue());
		param.setPayOrderId(payOrderId);
		param.setFromAccountNum(fromAccountNum);
		param.setToAccountNum(toAccountNum);
		param.setComment(comment);
		
		String result = HttpClientUtils.sendPost(ResourceUtils.getBundleValue4String("goldpay.url") 
				+ "goldpayTransaction",JsonBinder.getInstance().toJson(param));
		
		logger.info("result : {}",result);
		
		ConfirmTransactionS2C confirmTransactionS2C = JsonBinder.
				getInstanceNonNull().fromJson(result, ConfirmTransactionS2C.class);
		
		return confirmTransactionS2C.getRetCode();
		
	}
	

}

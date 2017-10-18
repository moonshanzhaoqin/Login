package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
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
	@Autowired
	WalletDAO walletDAO;
	
	public static Logger logger = LogManager.getLogger(GoldpayTrans4MergeManagerImpl.class);
	
	@Override
	public GoldpayUserDTO getGoldpayUserInfo(Integer exUserId){
		
		User user = userDAO.getUser(exUserId);
		GetGoldpayUserC2S param = new GetGoldpayUserC2S();
		param.setAreaCode(user.getAreaCode());
		param.setMobile(user.getUserPhone());
		
		String result = HttpClientUtils.sendPost(ResourceUtils.getBundleValue4String("goldpay.url") 
				+ "member/getMemberInfo",JsonBinder.getInstance().toJson(param));
		
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
				+ "trans/getOrderId",JsonBinder.getInstance().toJson(param));
		
		logger.info("result : {}",result);
		
		GetGoldpayOrderIdS2C getGoldpayOrderIdS2C = JsonBinder.
				getInstanceNonNull().fromJson(result, GetGoldpayOrderIdS2C.class);
		return getGoldpayOrderIdS2C.getPayOrderId();
	}
	
	@Override
	public Integer goldpayTransaction(String fromAccountNum,String toAccountNum,
			BigDecimal balance,String payOrderId,String comment){
		
		GoldpayTransactionC2S param = new GoldpayTransactionC2S();
		param.setBalance(balance.intValue());
		param.setPayOrderId(payOrderId);
		param.setFromAccountNum(fromAccountNum);
		param.setToAccountNum(toAccountNum);
		param.setComment(comment);
		
		String result = HttpClientUtils.sendPost(ResourceUtils.getBundleValue4String("goldpay.url") 
				+ "trans/goldpayTransaction",JsonBinder.getInstance().toJson(param));
		
		logger.info("result : {}",result);
		
		ConfirmTransactionS2C confirmTransactionS2C = JsonBinder.
				getInstanceNonNull().fromJson(result, ConfirmTransactionS2C.class);
		
		return confirmTransactionS2C.getRetCode();
		
	}

	@Override
	public HashMap<String, String> updateWalletByUserIdAndCurrency(Integer payerId,String currencyOut,
			Integer payeeId,String currencyIn, BigDecimal amount,
			int transferType, String transactionId,boolean isUpdateWallet,String goldpayOrderId) {
		
		HashMap<String, String> result = new HashMap<>();
		
		try {
			
			if(ServerConsts.CURRENCY_OF_GOLDPAY.equals(currencyOut) 
					|| ServerConsts.CURRENCY_OF_GOLDPAY.equals(currencyIn)){
				
				if(!StringUtils.isNotBlank(goldpayOrderId)){
					goldpayOrderId = getGoldpayOrderId();
				}
				//获取交易双方goldpayaccount
				GoldpayUserDTO payerAccount = getGoldpayUserInfo(payerId);
				GoldpayUserDTO payeeIdAccount = getGoldpayUserInfo(payeeId);
				
				Integer retCode = null;
				
				if(ServerConsts.CURRENCY_OF_GOLDPAY.equals(currencyOut)){
					retCode = goldpayTransaction(payerAccount.getAccountNum(),
							payeeIdAccount.getAccountNum(),
							amount,goldpayOrderId,null);
				}else{
					retCode = goldpayTransaction(payeeIdAccount.getAccountNum(),
							payerAccount.getAccountNum(),amount,goldpayOrderId,null);
				}
				
				if(retCode != ServerConsts.GOLDPAY_RETURN_SUCCESS){
					logger.warn("goldpay transaction failed");
					result.put("retCode", RetCodeConsts.EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE);
					result.put("msg", "Insufficient balance");
					return result;
				}
				
				result.put("goldpayOrderId", goldpayOrderId);
			}
			
			//exchange时为false
			if(isUpdateWallet){
				//扣款
				walletDAO.updateWalletByUserIdAndCurrency(payerId,currencyOut, amount, 
						"-", transferType,transactionId);
				//加款
				walletDAO.updateWalletByUserIdAndCurrency(payeeId,currencyIn, amount, 
						"+", transferType,transactionId);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error : {}",e.toString());
			result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			result.put("msg", e.toString());
			return result;
		}
		
		result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		result.put("msg", "sucess");
		
		return result;
	}
	

}

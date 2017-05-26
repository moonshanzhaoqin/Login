package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.CreditCard;
import com.braintreegateway.Customer;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.PayPalTransManager;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.util.ResourceUtils;

@Service
public class PayPalTransManagerImpl implements PayPalTransManager {

	@Autowired
	UserDAO userDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	TransferDAO transferDAO;
	
	@Autowired
	CommonManager commonManager;
	@Autowired
	OandaRatesManager oandaRatesManager;
	
	
	public static Logger logger = LogManager.getLogger(PayPalTransManagerImpl.class);
	
	@Override
	public HashMap<String, Object> paypalTransInit(Integer userId, String currencyLeft, BigDecimal amount) {
		
		HashMap<String, Object> result = new HashMap<>();
		
		//判断条件.币种合法，GDQ数量为整数且大于100
		if(!commonManager.verifyCurrency(currencyLeft)){
			logger.warn("This currency is not a tradable currency");
			result.put("retCode", RetCodeConsts.EXCHANGE_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			result.put("msg", "This currency is not a tradable currency");
			return result;
		}
		
		//计算结果值
		BigDecimal rate = oandaRatesManager.getSingleExchangeRate(currencyLeft, ServerConsts.CURRENCY_OF_GOLDPAY);
		BigDecimal baseAmout = amount.divide(rate, 2, BigDecimal.ROUND_UP);
		logger.info("amount{} / rate {} = baseAmount {}",amount,rate,baseAmout);
		
		//生成TransId
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		//生成订单
		User systemUser = userDAO.getSystemUser();
		Transfer transfer = new Transfer();
		transfer.setTransferId(transferId);
		transfer.setCreateTime(new Date());
		transfer.setCurrency(currencyLeft);
		transfer.setTransferAmount(baseAmout);
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		transfer.setUserFrom(userId);
		transfer.setUserTo(systemUser.getUserId());
		
		transfer.setTransferType(ServerConsts.TRANSFER_TYPE_IN_PAYPAL_RECHAEGE);
		// 保存
		transferDAO.addTransfer(transfer);
		
		//获取PayPal的token
		String accessToken = ResourceUtils.getBundleValue4String("paypal.accessToken", 
				"access_token$sandbox$h32wtjg3dw3jt4kd$e0a3535f2b04517e66258c0cbe9b118d");
		BraintreeGateway gateway = new BraintreeGateway(accessToken);
		String  clientToken = gateway.clientToken().generate();
		
		
		result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		result.put("msg", MessageConsts.RET_CODE_SUCCESS);
		
		result.put("amount", baseAmout);
		result.put("unit", (commonManager.getCurrentCurreny(currencyLeft)).getCurrencyUnit());
		result.put("transId", transferId);
		result.put("token", clientToken);
		
		result.put("createTime", transfer.getCreateTime());
		result.put("expiration", ResourceUtils.getBundleValue4Long("paypal.expiration", 600L));
		
		return result;
	}
	
	
	public HashMap<String, String> paypalTransConfirm(Integer userId,String transId,String nonce){
		
		HashMap<String, String> map = new HashMap<>();
		
		Transaction transaction;
		
		//条件 验证1.transId，amount	
		Transfer transfer = transferDAO.getTransferById(transId);
		if(transfer == null || transfer.getTransferStatus() != ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION){
			logger.warn("Order status exception");
			map.put("retCode", RetCodeConsts.TRANSFER_PAYPALTRANS_ORDER_STATUS_EXCEPTION);
			map.put("msg", "Order status exception");
			return map;
		}
		
		//验证时间过期
		long expirationTime = ResourceUtils.getBundleValue4Long("paypal.expiration", 600L);
		if((new Date().getTime() - transfer.getCreateTime().getTime()) > 1000*expirationTime){
			logger.warn("time out");
			map.put("retCode", RetCodeConsts.TRANSFER_PAYPALTRANS_TIME_OUT);
			map.put("msg", "time out");
			return map;
		}
		
		//paypal
		String accessToken = ResourceUtils.getBundleValue4String("paypal.accessToken", 
				"access_token$sandbox$h32wtjg3dw3jt4kd$e0a3535f2b04517e66258c0cbe9b118d");
		BraintreeGateway gateway = new BraintreeGateway(accessToken);
		
		TransactionRequest request = new TransactionRequest();
		request.amount(transfer.getTransferAmount());
		request.merchantAccountId(transfer.getCurrency());
		request.paymentMethodNonce(nonce);
		request.orderId(transfer.getTransferId());
		
		request.options().paypal().customField("PayPal custom field").description("Description for PayPal email receipt").done();
		
		Result<Transaction> saleResult = gateway.transaction().sale(request);
		
		//验证PayPal回调结果
		if (saleResult.isSuccess()) {
			transaction = saleResult.getTarget();
			logger.info("Success ID: {}",transaction.getId());
			
			CreditCard creditCard = transaction.getCreditCard();
			logger.info("The cardholder name: {}",creditCard.getCardholderName());
			
			Customer customer = transaction.getCustomer();
			logger.info("Name : {} {},Phone : {},Id : {}",customer.getFirstName(),customer.getLastName(),customer.getPhone(),customer.getId());
			
		} else {
			logger.warn("Message: {}",saleResult.getMessage());
			map.put("retCode", RetCodeConsts.TRANSFER_PAYPALTRANS_PAYMENT_FAILED);
			map.put("msg", saleResult.getMessage());
			return map;
		}
		
		//user+GDQ,system-GDQ
		User systemUser = userDAO.getSystemUser();
		// 系统扣款
		walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), transfer.getCurrency(),
				transfer.getTransferAmount(), "-", ServerConsts.TRANSFER_TYPE_IN_PAYPAL_RECHAEGE, transfer.getTransferId());
		// 用户加款
		walletDAO.updateWalletByUserIdAndCurrency(userId, transfer.getCurrency(), transfer.getTransferAmount(),
				"+", ServerConsts.TRANSFER_TYPE_IN_PAYPAL_RECHAEGE, transfer.getTransferId());
		
		//更改transfer状态
		transferDAO.updateTransferStatus(transfer.getTransferId(), ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		
		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		
		return map;
		
	}
	
	

}

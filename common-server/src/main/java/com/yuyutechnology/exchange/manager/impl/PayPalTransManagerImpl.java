package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.PayPalDetails;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.PayPalTransManager;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.User;

@Service
public class PayPalTransManagerImpl implements PayPalTransManager {

	public static Logger logger = LogManager.getLogger(PayPalTransManagerImpl.class);
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	ConfigManager configManager;
	@Autowired
	TransferDAO transferDAO;
	
	@Autowired
	CrmAlarmManager crmAlarmManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	OandaRatesManager oandaRatesManager;

	BraintreeGateway gateway;
	
	@PostConstruct
	public void init() {
		gateway = new BraintreeGateway(configManager.getConfigStringValue(ConfigKeyEnum.PAYPAL_ACCESSTOKEN, ""));
		//初始化accumulatedAmount
		BigDecimal accumulatedAmount = transferDAO.getAccumulatedAmount(ServerConsts.REDISS_KEY_OF_TOTAL_ANMOUT_OF_GDQ);
		if(accumulatedAmount.compareTo(new BigDecimal("0")) == 0 ){
			accumulatedAmount = transferDAO.getTotalPaypalExchange(new Date(), 
					ServerConsts.TRANSFER_TYPE_IN_PAYPAL_RECHAEGE, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
			logger.info("redis AccumulatedAmount is 0,and mysql sum() is {} :",accumulatedAmount);
			transferDAO.updateAccumulatedAmount(ServerConsts.REDISS_KEY_OF_TOTAL_ANMOUT_OF_GDQ, accumulatedAmount);
		}
		
	}

	@Override
	public HashMap<String, Object> paypalTransInit(Integer userId, String currencyLeft, BigDecimal amount) {

		HashMap<String, Object> result = new HashMap<>();

		// 判断条件.币种合法，GDQ数量为整数且大于100
		if (!commonManager.verifyCurrency(currencyLeft)) {
			logger.warn("This currency is not a tradable currency");
			result.put("retCode", RetCodeConsts.EXCHANGE_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			result.put("msg", "This currency is not a tradable currency");
			return result;
		}

		// 计算结果值
		BigDecimal rate = oandaRatesManager.getSingleExchangeRate(currencyLeft, ServerConsts.CURRENCY_OF_GOLDPAY);
		BigDecimal baseAmout = amount.divide(rate, currencyLeft.equals(ServerConsts.CURRENCY_OF_JPY)?0:2, BigDecimal.ROUND_UP);
		logger.info("amount{} / rate {} = baseAmount {}", amount, rate, baseAmout);
		
		if(!isOverlimit(baseAmout)){
			logger.warn("Reach or exceed 100%,The transaction is forbidden");
			result.put("retCode", RetCodeConsts.TRANSFER_PAYPALTRANS_TOTAL_AMOUNT_OF_GDQ);
			result.put("msg", "Reach or exceed 100%,The transaction is forbidden");
			return result;
		}
		

		// 生成TransId
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		// 生成订单
		User systemUser = userDAO.getSystemUser();
		Transfer transfer = new Transfer();
		transfer.setTransferId(transferId);
		transfer.setCreateTime(new Date());
		transfer.setCurrency(ServerConsts.CURRENCY_OF_GOLDPAY);
		transfer.setTransferAmount(amount);
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		transfer.setUserFrom(systemUser.getUserId());
		transfer.setUserTo(userId);
		transfer.setPaypalCurrency(currencyLeft);
		transfer.setPaypalExchange(baseAmout);
		transfer.setTransferType(ServerConsts.TRANSFER_TYPE_IN_PAYPAL_RECHAEGE);
		// 保存
		transferDAO.addTransfer(transfer);
		String clientToken = gateway.clientToken().generate();

		result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		result.put("msg", MessageConsts.RET_CODE_SUCCESS);
		result.put("amount", baseAmout);
		result.put("unit", (commonManager.getCurrentCurreny(currencyLeft)).getCurrencyUnit());
		result.put("transId", transferId);
		result.put("token", clientToken);
		result.put("createTime", transfer.getCreateTime());
		result.put("expiration", configManager.getConfigLongValue(ConfigKeyEnum.PAYPAL_EXPIRATION, 600l));

		return result;
	}

	public HashMap<String, String> paypalTransConfirm(Integer userId, String transId, String nonce) {

		HashMap<String, String> map = new HashMap<>();

		// 条件 验证1.transId，amount
		Transfer transfer = transferDAO.getTranByIdAndStatus(transId, ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if (transfer == null || userId != transfer.getUserTo()) {
			logger.warn("Order status exception");
			map.put("retCode", RetCodeConsts.TRANSFER_PAYPALTRANS_ORDER_STATUS_EXCEPTION);
			map.put("msg", "Order status exception");
			return map;
		}

		// 验证时间过期
		long expirationTime = configManager.getConfigLongValue(ConfigKeyEnum.PAYPAL_EXPIRATION, 600l);
		if ((new Date().getTime() - transfer.getCreateTime().getTime()) > 1000 * expirationTime) {
			logger.warn("time out");
			map.put("retCode", RetCodeConsts.TRANSFER_PAYPALTRANS_TIME_OUT);
			map.put("msg", "time out");
			return map;
		}

		// paypal
		Result<Transaction> saleResult = null;
		try {
			TransactionRequest request = new TransactionRequest();
			request.amount(transfer.getPaypalExchange());
			request.merchantAccountId(transfer.getPaypalCurrency());
			request.paymentMethodNonce(nonce);
			request.orderId(transfer.getTransferId());
			request.options().submitForSettlement(true).storeInVaultOnSuccess(true).done();
			
			saleResult = gateway.transaction().sale(request);

		} catch (Exception e) {
			logger.warn("paypal error !", e);
			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			map.put("msg", "fail");
			return map;
		}

		// 验证PayPal回调结果
		if (saleResult.isSuccess()) {
			Transaction transaction = saleResult.getTarget();
			logger.info("Success ID: {}", transaction.getId());

			PayPalDetails payPalDetails = transaction.getPayPalDetails();
			logger.info("Name : {} {},Email : {}", payPalDetails.getPayerFirstName(), payPalDetails.getPayerLastName(),
					payPalDetails.getPayeeEmail());

			transfer.setGoldpayName(payPalDetails.getPayerFirstName() + " " + payPalDetails.getPayerLastName());
			transfer.setTransferComment(transaction.getId());

		} else {
			logger.warn("Message: {}", saleResult.getMessage());
			map.put("retCode", RetCodeConsts.TRANSFER_PAYPALTRANS_PAYMENT_FAILED);
			map.put("msg", saleResult.getMessage());
			return map;
		}

		// user+GDQ,system-GDQ
		User systemUser = userDAO.getSystemUser();
		// 系统扣款
		walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), transfer.getCurrency(),
				transfer.getTransferAmount(), "-", ServerConsts.TRANSFER_TYPE_IN_PAYPAL_RECHAEGE,
				transfer.getTransferId());
		// 用户加款
		walletDAO.updateWalletByUserIdAndCurrency(userId, transfer.getCurrency(), transfer.getTransferAmount(), "+",
				ServerConsts.TRANSFER_TYPE_IN_PAYPAL_RECHAEGE, transfer.getTransferId());

		// 更改transfer状态
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		transfer.setFinishTime(new Date());
		transferDAO.updateTransfer(transfer);
		
		//预警
		alarmWhileReachLimitOfTotalAmountOfGDQ(transfer.getTransferAmount());
		
		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");

		return map;

	}
	
	
	private boolean isOverlimit(BigDecimal amount){
		
		BigDecimal accumulatedAmount = transferDAO.getAccumulatedAmount(ServerConsts.REDISS_KEY_OF_TOTAL_ANMOUT_OF_GDQ);
		BigDecimal totalGDQCanBeSold = new BigDecimal(configManager.getConfigStringValue(ConfigKeyEnum.TOTALGDQCANBESOLD, "0"));
		BigDecimal percent = (accumulatedAmount).divide(totalGDQCanBeSold,3,BigDecimal.ROUND_DOWN);
		
		logger.info("amount : {}",amount);
		logger.info("accumulatedAmount : {}",accumulatedAmount);
		logger.info("totalGDQCanBeSold : {}",totalGDQCanBeSold);
		logger.info("(accumulatedAmount).divide(totalGDQCanBeSold,3,BigDecimal.ROUND_DOWN) : {}",percent);
		
		if(percent.doubleValue() >= 1){
			logger.warn("Reach or exceed 100%,The transaction is forbidden !");
			return false;
		}
		
		return true;
		
	}
	
	private void alarmWhileReachLimitOfTotalAmountOfGDQ(BigDecimal amount){
		//更新redis值
		transferDAO.updateAccumulatedAmount(ServerConsts.REDISS_KEY_OF_TOTAL_ANMOUT_OF_GDQ, amount);
		//计算百分比
		BigDecimal accumulatedAmount = transferDAO.getAccumulatedAmount(ServerConsts.REDISS_KEY_OF_TOTAL_ANMOUT_OF_GDQ);
		BigDecimal totalGDQCanBeSold = new BigDecimal(configManager.getConfigStringValue(ConfigKeyEnum.TOTALGDQCANBESOLD, "0"));
		BigDecimal percent = (accumulatedAmount).divide(totalGDQCanBeSold,3,BigDecimal.ROUND_DOWN);

		logger.info("accumulatedAmount : {}",accumulatedAmount);
		logger.info("totalGDQCanBeSold : {}",totalGDQCanBeSold);
		logger.info("(accumulatedAmount).divide(totalGDQCanBeSold,3) : {}",percent);
		
		//获取Alarm配置信息
		crmAlarmManager.reachtotalGDQLimitAlarm(accumulatedAmount, percent);
		
	}
	
	
	
	
}

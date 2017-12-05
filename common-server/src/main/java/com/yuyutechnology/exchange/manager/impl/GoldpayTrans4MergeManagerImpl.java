package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.ExchangeDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.goldpay.msg.ConfirmTransactionS2C;
import com.yuyutechnology.exchange.goldpay.msg.CreateGoldpayC2S;
import com.yuyutechnology.exchange.goldpay.msg.CreateGoldpayS2C;
import com.yuyutechnology.exchange.goldpay.msg.GetGoldpayOrderIdC2S;
import com.yuyutechnology.exchange.goldpay.msg.GetGoldpayOrderIdS2C;
import com.yuyutechnology.exchange.goldpay.msg.GetGoldpayUserC2S;
import com.yuyutechnology.exchange.goldpay.msg.GetGoldpayUserS2C;
import com.yuyutechnology.exchange.goldpay.msg.GoldpayTransaction4FeeC2S;
import com.yuyutechnology.exchange.goldpay.msg.GoldpayTransaction4FeeS2C;
import com.yuyutechnology.exchange.goldpay.msg.GoldpayTransactionC2S;
import com.yuyutechnology.exchange.goldpay.msg.GoldpayUserDTO;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.Exchange;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.WalletSeq;
import com.yuyutechnology.exchange.util.HttpClientUtils;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.ResourceUtils;

@Service
public class GoldpayTrans4MergeManagerImpl implements GoldpayTrans4MergeManager {

	@Autowired
	UserDAO userDAO;
	@Autowired
	BindDAO bindDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	WalletSeqDAO walletSeqDAO;
	@Autowired
	ExchangeDAO exchangeDAO;
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	TransDetailsManager transDetailsManager;

	public static Logger logger = LogManager.getLogger(GoldpayTrans4MergeManagerImpl.class);

	@Override
	public GoldpayUserDTO createGoldpay(String areaCode, String userPhone, String userName, boolean newUser) {
		CreateGoldpayC2S createGoldpayRequest = new CreateGoldpayC2S(areaCode, userPhone, userName, newUser);
		String param = JsonBinder.getInstanceNonNull().toJson(createGoldpayRequest);
		logger.info("param==={}", param);
		String result = HttpClientUtils
				.sendPost(ResourceUtils.getBundleValue4String("goldpay.url") + "member/createMember", param);
		logger.info("result==={}", result);
		if (StringUtils.isNotEmpty(result)) {
			CreateGoldpayS2C goldpayInfo = JsonBinder.getInstance().fromJson(result, CreateGoldpayS2C.class);
			if (goldpayInfo != null && (goldpayInfo.getRetCode() == 1 || goldpayInfo.getRetCode() == 100004)) {// 成功
				return goldpayInfo.getGoldpayUserDTO();
			}
		}
		return null;
	}

	@Override
	public GoldpayUserDTO getGoldpayUserInfo(Integer exUserId) {
		Bind bind = bindDAO.getBind(exUserId);
		if (bind == null) {
			return null;
		}
		GetGoldpayUserC2S param = new GetGoldpayUserC2S();
		param.setAccountNum(bind.getGoldpayAcount());
		String result = HttpClientUtils.sendPost(
				ResourceUtils.getBundleValue4String("goldpay.url") + "member/getMemberInfo",
				JsonBinder.getInstance().toJson(param));
		GetGoldpayUserS2C getGoldpayUserS2C = JsonBinder.getInstanceNonNull().fromJson(result, GetGoldpayUserS2C.class);
		return getGoldpayUserS2C.getGoldpayUserDTO();
	}

	@Override
	public String getGoldpayOrderId() {
		logger.info("get Goldpay OrderId -->");
		GetGoldpayOrderIdC2S param = new GetGoldpayOrderIdC2S();
		param.setType("3");
		String result = HttpClientUtils.sendPost(
				ResourceUtils.getBundleValue4String("goldpay.url") + "trans/getOrderId",
				JsonBinder.getInstance().toJson(param));

		logger.info("result : {}", result);

		GetGoldpayOrderIdS2C getGoldpayOrderIdS2C = JsonBinder.getInstanceNonNull().fromJson(result,
				GetGoldpayOrderIdS2C.class);
		return getGoldpayOrderIdS2C.getPayOrderId();
	}

	public GoldpayTransaction4FeeS2C goldpayTransaction4fee(GoldpayTransaction4FeeC2S param) {

		String result = HttpClientUtils.sendPost4Retry(
				ResourceUtils.getBundleValue4String("goldpay.url") + "trans/goldpayTransaction4fee",
				JsonBinder.getInstance().toJson(param));

		logger.info("result : {}", result);

		GoldpayTransaction4FeeS2C goldpayTransaction4FeeS2C = JsonBinder.getInstanceNonNull().fromJson(result,
				GoldpayTransaction4FeeS2C.class);

		return goldpayTransaction4FeeS2C;
	}

//	@Override
//	public HashMap<String, String> updateWallet4FeeTrans(String transferId, String feeTransferId) {
//
//		HashMap<String, String> result = new HashMap<>();
//
//		logger.info("updateWallet4FeeTrans for transfer {},{}", transferId, feeTransferId);
//		Transfer feeTransfer = null;
//		Transfer transfer = transferDAO.getTransferById(transferId);
//		if(StringUtils.isNotBlank(feeTransferId)){
//			feeTransfer = transferDAO.getTransferById(feeTransferId);
//		}
//		
//
//		if (!StringUtils.isNotBlank(transfer.getGoldpayOrderId())) {
//			logger.error("error : Not generated goldpayId");
//			result.put("retCode", RetCodeConsts.TRANSFER_GOLDPAYTRANS_ORDERID_NOT_EXIST);
//			result.put("msg", "Not generated goldpayId");
//			return result;
//		}
//
//		// 获取交易双方goldpayaccount
//		GoldpayUserDTO payerAccount = getGoldpayUserInfo(transfer.getUserFrom());
//		GoldpayUserDTO payeeIdAccount = getGoldpayUserInfo(transfer.getUserTo());
//
//		if (payerAccount == null || payeeIdAccount == null) {
//			logger.error("error :  Account information does not exist");
//			result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
//			result.put("msg", "Account information does not exist");
//			return result;
//		}
//
//		GoldpayTransaction4FeeC2S param = new GoldpayTransaction4FeeC2S();
//
//		param.setPayOrderId(transfer.getGoldpayOrderId());
//		param.setFromAccountNum(payerAccount.getAccountNum());
//		param.setToAccountNum(payeeIdAccount.getAccountNum());
//		param.setBalance(transfer.getTransferAmount().longValue());
//
//		if (feeTransfer != null) {
//
//			GoldpayUserDTO feeAccount = getGoldpayUserInfo(feeTransfer.getUserTo());
//
//			param.setFeePayOrderId(feeTransfer.getGoldpayOrderId());
//			if (feeTransfer.getUserFrom() == transfer.getUserFrom()) {
//				param.setFeeFromAccountNum(payerAccount.getAccountNum());
//			} else {
//				param.setFeeFromAccountNum(payeeIdAccount.getAccountNum());
//			}
//
//			param.setFeeToAccountNum(feeAccount.getAccountNum());
//			param.setFeeBalance(feeTransfer.getTransferAmount().longValue());
//		}
//
//		param.setComment(transfer.getTransferComment());
//
//		GoldpayTransaction4FeeS2C s2c = goldpayTransaction4fee(param);
//
//		if (s2c != null && s2c.getRetCode() != ServerConsts.GOLDPAY_RETURN_SUCCESS) {
//			logger.warn("goldpay transaction failed");
//			result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
//			result.put("msg", "Insufficient balance");
//			return result;
//		}
//
//		// 对于Transfer 扣款
//		walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), transfer.getCurrency(),
//				transfer.getTransferAmount(), "-", transfer.getTransferType(), transfer.getTransferId());
//		// 加款
//		walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserTo(), transfer.getCurrency(),
//				transfer.getTransferAmount(), "+", transfer.getTransferType(), transfer.getTransferId());
//
//		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
//		transfer.setFinishTime(new Date());
//		transferDAO.updateTransfer(transfer);
//
//		// 对于Transfer 扣款
//		if (feeTransfer != null) {
//			walletDAO.updateWalletByUserIdAndCurrency(feeTransfer.getUserFrom(), feeTransfer.getCurrency(),
//					feeTransfer.getTransferAmount(), "-", feeTransfer.getTransferType(), feeTransfer.getTransferId());
//			// 加款
//			walletDAO.updateWalletByUserIdAndCurrency(feeTransfer.getUserTo(), feeTransfer.getCurrency(),
//					feeTransfer.getTransferAmount(), "+", feeTransfer.getTransferType(), feeTransfer.getTransferId());
//
//			feeTransfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
//			feeTransfer.setFinishTime(new Date());
//			transferDAO.updateTransfer(feeTransfer);
//		}
//
//		result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
//		result.put("msg", "success");
//		return result;
//
//	}
	
	@Override
	public HashMap<String, String> updateWallet4FeeTrans(String transferId, String feeTransferId) {

		HashMap<String, String> result = new HashMap<>();
		
		if(StringUtils.isNotBlank(transferId) && StringUtils.isNotBlank(feeTransferId)){
			Transfer transfer = transferDAO.getTransferById(transferId);
			Transfer feeTransfer = transferDAO.getTransferById(feeTransferId);
			if (!StringUtils.isNotBlank(transfer.getGoldpayOrderId())) {
				logger.error("error : Not generated goldpayId");
				result.put("retCode", RetCodeConsts.TRANSFER_GOLDPAYTRANS_ORDERID_NOT_EXIST);
				result.put("msg", "Not generated goldpayId");
				return result;
			}

			// 获取交易双方goldpayaccount
			GoldpayUserDTO payerAccount = getGoldpayUserInfo(transfer.getUserFrom());
			GoldpayUserDTO payeeIdAccount = getGoldpayUserInfo(transfer.getUserTo());

			if (payerAccount == null || payeeIdAccount == null) {
				logger.error("error :  Account information does not exist");
				result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
				result.put("msg", "Account information does not exist");
				return result;
			}

			GoldpayTransaction4FeeC2S param = new GoldpayTransaction4FeeC2S();

			param.setPayOrderId(transfer.getGoldpayOrderId());
			param.setFromAccountNum(payerAccount.getAccountNum());
			param.setToAccountNum(payeeIdAccount.getAccountNum());
			param.setBalance(transfer.getTransferAmount().longValue());
			
			GoldpayUserDTO feeAccount = getGoldpayUserInfo(feeTransfer.getUserTo());
			
			if (!StringUtils.isNotBlank(feeTransfer.getGoldpayOrderId())) {
				logger.error("error : Not generated goldpayId");
				result.put("retCode", RetCodeConsts.TRANSFER_GOLDPAYTRANS_ORDERID_NOT_EXIST);
				result.put("msg", "Not generated goldpayId");
				return result;
			}

			param.setFeePayOrderId(feeTransfer.getGoldpayOrderId());
			if (feeTransfer.getUserFrom() == transfer.getUserFrom()) {
				param.setFeeFromAccountNum(payerAccount.getAccountNum());
			} else {
				param.setFeeFromAccountNum(payeeIdAccount.getAccountNum());
			}

			param.setFeeToAccountNum(feeAccount.getAccountNum());
			param.setFeeBalance(feeTransfer.getTransferAmount().longValue());			
			param.setComment(transfer.getTransferComment());

			GoldpayTransaction4FeeS2C s2c = goldpayTransaction4fee(param);

			if (s2c != null && s2c.getRetCode() != ServerConsts.GOLDPAY_RETURN_SUCCESS) {
				logger.warn("goldpay transaction failed");
				result.put("retCode", RetCodeConsts.TRANSFER_GOLDPAYTRANS_FAIL);
				result.put("msg", "goldpay transaction failed");
				return result;
			}
			// 对于Transfer 扣款
			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), transfer.getCurrency(),
					transfer.getTransferAmount(), "-", transfer.getTransferType(), transfer.getTransferId());
			// 加款
			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserTo(), transfer.getCurrency(),
					transfer.getTransferAmount(), "+", transfer.getTransferType(), transfer.getTransferId());
			//更改订单状态
			transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
			transfer.setFinishTime(new Date());
			transferDAO.updateTransfer(transfer);
			
			walletDAO.updateWalletByUserIdAndCurrency(feeTransfer.getUserFrom(), feeTransfer.getCurrency(),
					feeTransfer.getTransferAmount(), "-", feeTransfer.getTransferType(), feeTransfer.getTransferId());
			// 加款
			walletDAO.updateWalletByUserIdAndCurrency(feeTransfer.getUserTo(), feeTransfer.getCurrency(),
					feeTransfer.getTransferAmount(), "+", feeTransfer.getTransferType(), feeTransfer.getTransferId());
			feeTransfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
			feeTransfer.setFinishTime(new Date());
			transferDAO.updateTransfer(feeTransfer);
		}else if(StringUtils.isNotBlank(transferId) && !StringUtils.isNotBlank(feeTransferId)){
			
			Transfer transfer = transferDAO.getTransferById(transferId);
			
			if (!StringUtils.isNotBlank(transfer.getGoldpayOrderId())) {
				logger.error("error : Not generated goldpayId");
				result.put("retCode", RetCodeConsts.TRANSFER_GOLDPAYTRANS_ORDERID_NOT_EXIST);
				result.put("msg", "Not generated goldpayId");
				return result;
			}

			// 获取交易双方goldpayaccount
			GoldpayUserDTO payerAccount = getGoldpayUserInfo(transfer.getUserFrom());
			GoldpayUserDTO payeeIdAccount = getGoldpayUserInfo(transfer.getUserTo());

			if (payerAccount == null || payeeIdAccount == null) {
				logger.error("error :  Account information does not exist");
				result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
				result.put("msg", "Account information does not exist");
				return result;
			}
			
			Integer retCode = goldpayTransaction(payerAccount.getAccountNum(), payeeIdAccount.getAccountNum(),
					transfer.getTransferAmount(), transfer.getGoldpayOrderId(), null);

			if (retCode != ServerConsts.GOLDPAY_RETURN_SUCCESS) {
				logger.info("goldpay transaction failed");
				result.put("retCode", RetCodeConsts.TRANSFER_GOLDPAYTRANS_FAIL);
				result.put("msg", "goldpay transaction failed");
				return result;
			}
			
			// 对于Transfer 扣款
			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), transfer.getCurrency(),
					transfer.getTransferAmount(), "-", transfer.getTransferType(), transfer.getTransferId());
			// 加款
			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserTo(), transfer.getCurrency(),
					transfer.getTransferAmount(), "+", transfer.getTransferType(), transfer.getTransferId());
			
			//更改订单状态
			transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
			transfer.setFinishTime(new Date());
			transferDAO.updateTransfer(transfer);

		}else{
			result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			result.put("msg", "fail");
			return result;
		}

		result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		result.put("msg", "success");
		return result;

	}

	@Override
	// @Async
	public void updateWallet4GoldpayTrans(String transferId) {
		logger.info("updateWallet4GoldpayTrans for transfer {}", transferId);
		Transfer transfer = transferDAO.getTransferById(transferId);
		logger.info("transfer:{}", transfer);
		HashMap<String, String> result = updateWalletByUserIdAndCurrency(transfer.getUserFrom(), transfer.getUserTo(),
				transfer.getCurrency(), transfer.getTransferAmount(), transfer.getTransferType(),
				transfer.getTransferId(), true, transfer.getGoldpayOrderId());

		if (!RetCodeConsts.RET_CODE_SUCCESS.equals(result.get("retCode"))) {
			logger.info(result.get("msg"));
			transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_PROCESSING);
			transferDAO.updateTransfer(transfer);
		} else {
			transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
			transfer.setFinishTime(new Date());
			transferDAO.updateTransfer(transfer);
		}
	}

	@Override
	public void updateWallet4GoldpayTransList(List<String> transferIds) {
		updateWallet4FeeTrans(transferIds.get(0), transferIds.get(1));
	}

	@Override
	public void updateWallet4GoldpayExchange(String exchangeId, Integer systemUserId) {
		Exchange exchange = exchangeDAO.getExchangeById(exchangeId);

		HashMap<String, String> result = null;

		if (ServerConsts.CURRENCY_OF_GOLDPAY.equals(exchange.getCurrencyIn())) {
			result = updateWalletByUserIdAndCurrency(systemUserId, exchange.getUserId(), exchange.getCurrencyIn(),
					exchange.getAmountIn(), ServerConsts.TRANSFER_TYPE_EXCHANGE, exchange.getExchangeId(), false,
					exchange.getGoldpayOrderId());

//			WalletSeq walletSeq1 = new WalletSeq(systemUserId, ServerConsts.TRANSFER_TYPE_EXCHANGE,
//					exchange.getCurrencyIn(), exchange.getAmountIn().negate(), exchange.getExchangeId(), new Date());
//			walletSeqDAO.addWalletSeq(walletSeq1);
//			WalletSeq walletSeq = new WalletSeq(exchange.getUserId(), ServerConsts.TRANSFER_TYPE_EXCHANGE,
//					exchange.getCurrencyIn(), exchange.getAmountIn(), exchange.getExchangeId(), new Date());
//			walletSeqDAO.addWalletSeq(walletSeq);

		}

		if (ServerConsts.CURRENCY_OF_GOLDPAY.equals(exchange.getCurrencyOut())) {
			result = updateWalletByUserIdAndCurrency(exchange.getUserId(), systemUserId, exchange.getCurrencyOut(),
					exchange.getAmountOut(), ServerConsts.TRANSFER_TYPE_EXCHANGE, exchange.getExchangeId(), false,
					exchange.getGoldpayOrderId());

//			WalletSeq walletSeq1 = new WalletSeq(systemUserId, ServerConsts.TRANSFER_TYPE_EXCHANGE,
//					exchange.getCurrencyOut(), exchange.getAmountOut(), exchange.getExchangeId(), new Date());
//			walletSeqDAO.addWalletSeq(walletSeq1);
//
//			WalletSeq walletSeq = new WalletSeq(exchange.getUserId(), ServerConsts.TRANSFER_TYPE_EXCHANGE,
//					exchange.getCurrencyOut(), exchange.getAmountOut().negate(), exchange.getExchangeId(), new Date());
//			walletSeqDAO.addWalletSeq(walletSeq);
		}
		if (result != null && !RetCodeConsts.RET_CODE_SUCCESS.equals(result.get("retCode"))) {
			logger.info(result.get("msg"));
			exchange.setExchangeStatus(ServerConsts.EXCHANGE_STATUS_OF_INTERRUPTED);
			exchangeDAO.updateExchage(exchange);
			return;
		}

		exchange.setExchangeStatus(ServerConsts.EXCHANGE_STATUS_OF_COMPLETED);
		exchangeDAO.updateExchage(exchange);

	}

	private HashMap<String, String> updateWalletByUserIdAndCurrency(Integer payerId, Integer payeeId, String currency,
			BigDecimal amount, int transferType, String transactionId, boolean isUpdateWallet, String goldpayOrderId) {

		HashMap<String, String> result = new HashMap<>();

		try {

			if (ServerConsts.CURRENCY_OF_GOLDPAY.equals(currency)) {

				if (!StringUtils.isNotBlank(goldpayOrderId)) {
					logger.error("error : Not generated goldpayId");
					result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
					result.put("msg", "Not generated goldpayId");
					return result;
				}
				// 获取交易双方goldpayaccount
				GoldpayUserDTO payerAccount = getGoldpayUserInfo(payerId);
				GoldpayUserDTO payeeIdAccount = getGoldpayUserInfo(payeeId);

				if (payerAccount == null || payeeIdAccount == null) {
					logger.error("error :  Account information does not exist");
					result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
					result.put("msg", "Account information does not exist");
					return result;
				}

				Integer retCode = goldpayTransaction(payerAccount.getAccountNum(), payeeIdAccount.getAccountNum(),
						amount, goldpayOrderId, null);

				if (retCode != ServerConsts.GOLDPAY_RETURN_SUCCESS) {
					logger.info("goldpay transaction failed");
					result.put("retCode", RetCodeConsts.TRANSFER_GOLDPAYTRANS_FAIL);
					result.put("msg", "goldpay transaction failed");
					return result;
				}

				result.put("goldpayOrderId", goldpayOrderId);
			}

			// exchange时为false
			if (isUpdateWallet) {
				// 扣款
				walletDAO.updateWalletByUserIdAndCurrency(payerId, currency, amount, "-", transferType, transactionId);
				// 加款
				walletDAO.updateWalletByUserIdAndCurrency(payeeId, currency, amount, "+", transferType, transactionId);
			}

		} catch (Exception e) {
			logger.error("error : {}", e.toString());
			result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			result.put("msg", e.toString());
			return result;
		}

		result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		result.put("msg", "sucess");

		return result;
	}

	private Integer goldpayTransaction(String fromAccountNum, String toAccountNum, BigDecimal balance,
			String payOrderId, String comment) {

		GoldpayTransactionC2S param = new GoldpayTransactionC2S();
		param.setBalance(balance.intValue());
		param.setPayOrderId(payOrderId);
		param.setFromAccountNum(fromAccountNum);
		param.setToAccountNum(toAccountNum);
		param.setComment(comment);

		String result = HttpClientUtils.sendPost4Retry(
				ResourceUtils.getBundleValue4String("goldpay.url") + "trans/goldpayTransaction",
				JsonBinder.getInstance().toJson(param));

		logger.info("result : {}", result);

		ConfirmTransactionS2C confirmTransactionS2C = JsonBinder.getInstanceNonNull().fromJson(result,
				ConfirmTransactionS2C.class);

		return confirmTransactionS2C.getRetCode();

	}
	
	

}

package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ConfigKeyEnum;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.goldpay.transaction.ClientComfirmPay;
import com.yuyutechnology.exchange.goldpay.transaction.ClientPayOrder;
import com.yuyutechnology.exchange.goldpay.transaction.ClientPin;
import com.yuyutechnology.exchange.goldpay.transaction.MerchantPayOrder;
import com.yuyutechnology.exchange.goldpay.transaction.PayConfirm;
import com.yuyutechnology.exchange.goldpay.transaction.PayModel;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.utils.HttpTookit;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.PasswordUtils;
import com.yuyutechnology.exchange.utils.ResourceUtils;

@Service
public class GoldpayTransManagerImpl implements GoldpayTransManager{
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	BindDAO bindBAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	WalletSeqDAO walletSeqDAO;
	@Autowired
	ConfigManager configManager;
	
	public static Logger logger = LoggerFactory.getLogger(GoldpayTransManagerImpl.class);

	@Override
	public HashMap<String, String> goldpayPurchase(int userId,BigDecimal amount) {
		
		HashMap<String, String> map = new HashMap<>();
		
		User user = userDAO.getUser(userId);
		if(user ==null || user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
			logger.warn("The user does not exist or the account is blocked");
			map.put("retCode", ServerConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			map.put("msg", "The user does not exist or the account is blocked");
			return map;
		}
		Bind bind = bindBAO.getBindByUserId(userId);
		if(bind == null){
			logger.warn("The account is not tied to goldpay");
			map.put("msg", "The account is not tied to goldpay");
			map.put("retCode", ServerConsts.GOLDPAY_NOT_BIND);
			return map;
		}
		//生成TransId
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		ClientPayOrder clientPayOrder = new ClientPayOrder();
		clientPayOrder.setOrderId(transferId);
		clientPayOrder.setPayAmount(amount.intValue());
		clientPayOrder.setFromAccountNum(bind.getGoldpayAcount());
		clientPayOrder.setType(0);
		clientPayOrder.setClientId(configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTID, ""));
		
		String sign = DigestUtils.md5Hex(JsonBinder.getInstanceNonEmpty().toJson(clientPayOrder)
				+configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTKEY, ""));
		clientPayOrder.setSign(sign.toUpperCase());

		String result = HttpTookit.sendPost(ResourceUtils.getBundleValue4String("tpps.url")+"clientPay.do",
				JsonBinder.getInstance().toJson(clientPayOrder));
		
		PayModel payModel;
		
		if(!StringUtils.isEmpty(result)){
			
			logger.info("tpps callback result : {}",result);
			
			payModel = JsonBinder.getInstance().fromJson(result, PayModel.class);
			
			//错误处理
			if(payModel!=null && !payModel.getResultCode().equals(1)){
				map.put("msg", "something wrong!");
				map.put("retCode", ServerConsts.RET_CODE_FAILUE);
				if(payModel.getResultCode().equals(0)){
					logger.warn("goldpayPurchase tpps callback: fail");
				}else if(payModel.getResultCode().equals(-1)){
					logger.warn("goldpayPurchase tpps callback: INVALID SIGN");
				}else if(payModel.getResultCode().equals(-101)){
					logger.warn("goldpayPurchase tpps callback: ORDERID REPEAT");
				}else if(payModel.getResultCode().equals(-102)){
					logger.warn("goldpayPurchase tpps callback: ORDERID_COMPLETE");
				}else if(payModel.getResultCode().equals(200001)){
					logger.warn("goldpayPurchase tpps callback: NOT_ENOUGH_GOLDPAY");
					map.put("msg", "not enough goldpay!");
					map.put("retCode", ServerConsts.TRANSFER_GOLDPAYTRANS_GOLDPAY_NOT_ENOUGH);
				}else if(payModel.getResultCode().equals(1016)){
					logger.warn("goldpay account not vaild phone!");
					map.put("msg", "goldpay account not vaild phone!");
					map.put("retCode", ServerConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
				}
				return map;
			}
			User systemUser = userDAO.getSystemUser();
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
			transfer.setTransferType(ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE);
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


	@Override
	public HashMap<String, String> requestPin(int userId,String transferId) {
		
		HashMap<String, String> map = new HashMap<>();
		
		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if(transfer == null || transfer.getTransferType() != ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE){
			logger.warn("The transaction order does not exist");
			map.put("msg", "The transaction order does not exist");
			map.put("retCode", ServerConsts.TRANSFER_TRANS_ORDERID_NOT_EXIST);
			return map;
		}
		
		ClientPin clientPin = new ClientPin();
		clientPin.setClientId(configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTID, ""));
		clientPin.setPayOrderId(transfer.getTransferComment());
		
		String sign = DigestUtils.md5Hex(JsonBinder.getInstanceNonEmpty().toJson(clientPin)
				+configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTKEY, ""));
		
		clientPin.setSign(sign.toUpperCase());
		
		String result = HttpTookit.sendPost(ResourceUtils.getBundleValue4String("tpps.url")+"clientPin.do",
				JsonBinder.getInstance().toJson(clientPin));
		
		PayModel payModel;
		
		if(!StringUtils.isEmpty(result)){
			
			logger.info("tpps callback result : {}",result);
			payModel = JsonBinder.getInstance().fromJson(result, PayModel.class);
			
			if(payModel != null && !payModel.getResultCode().equals(1)){      //1:成功
				logger.warn("requestPin tpps callback: {}",payModel.getResultMessage());
				map.put("msg", "something wrong!");
				map.put("retCode", ServerConsts.RET_CODE_FAILUE);
				return map;
			}
			
			map.put("retCode", ServerConsts.RET_CODE_SUCCESS);
			map.put("msg", "ok");
			map.put("transferId", transferId);
		}else{
			logger.warn("tpps callback result : null");
			map.put("msg", "something wrong!");
			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
		}

		return map;
	}


	@Override
	public HashMap<String, String> goldpayTransConfirm(int userId,String pin, String transferId) {
		
		HashMap<String, String> map = new HashMap<>();
		
		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if(transfer == null || transfer.getTransferType() != ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE){
			map.put("retCode", ServerConsts.TRANSFER_GOLDPAYTRANS_ORDERID_NOT_EXIST);
			map.put("msg", "Order does not exist or has completed");
			return map;
		}
		
		ClientComfirmPay  clientComfirmPay  = new ClientComfirmPay();
		clientComfirmPay.setClientId(configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTID, ""));
		clientComfirmPay.setPin(StringUtils.defaultString(pin));
		clientComfirmPay.setPayOrderId(transfer.getTransferComment());
		
		String sign = DigestUtils.md5Hex(JsonBinder.getInstanceNonEmpty().toJson(clientComfirmPay)
				+configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTKEY, ""));
		
		clientComfirmPay.setSign(sign.toUpperCase());
		
		String result = HttpTookit.sendPost(ResourceUtils.getBundleValue4String("tpps.url")+"clientComfirmPay.do",
				JsonBinder.getInstance().toJson(clientComfirmPay));
		PayConfirm payConfirm;
		if(StringUtils.isNotBlank(result)){
			logger.info("goldpayTransConfirm tpps callback {} ",result);
			payConfirm = JsonBinder.getInstance().fromJson(result, PayConfirm.class);
			if (payConfirm != null && (payConfirm.getResultCode() == 1 || payConfirm.getResultCode() == 70002)) {
				User systemUser = userDAO.getSystemUser();
				//系统扣款
				walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), 
						transfer.getCurrency(), transfer.getTransferAmount(), "-");
				//用户加款
				walletDAO.updateWalletByUserIdAndCurrency(userId, 
						transfer.getCurrency(), transfer.getTransferAmount(), "+");
				
				//添加Seq记录
				walletSeqDAO.addWalletSeq4Transaction(systemUser.getUserId(), userId, 
						ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE, transferId, 
						transfer.getCurrency(), transfer.getTransferAmount());
				//更改订单状态
				transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
				
				map.put("retCode", ServerConsts.RET_CODE_SUCCESS);
				map.put("msg", "ok");
				return map;
			}else{
				map.put("retCode", ServerConsts.RET_CODE_FAILUE);
				map.put("msg", "fail");
				if (payConfirm != null && payConfirm.getResultCode() == 307) {
					logger.warn("goldpayTransConfirm tpps callback  error ! {}  CHECK_PIN_CODE_FAIL");
					map.put("retCode", ServerConsts.TRANSFER_GOLDPAYTRANS_CHECK_PIN_CODE_FAIL);
					map.put("msg", "CHECK_PIN_CODE_FAIL");
				}
				return map;
			}
//			if(payConfirm == null || (payConfirm.getResultCode() != 1 && payConfirm.getResultCode() != 307)){
//				map.put("retCode", ServerConsts.RET_CODE_FAILUE);
//				map.put("msg", "fail");
//				return map;
//			} else if (payConfirm.getResultCode()==307){
//				logger.warn("goldpayTransConfirm tpps callback  error ! {}  CHECK_PIN_CODE_FAIL");
//				map.put("retCode", ServerConsts.TRANSFER_GOLDPAYTRANS_CHECK_PIN_CODE_FAIL);
//				map.put("msg", "CHECK_PIN_CODE_FAIL");
//				return map;
//			} 
//			else if (payConfirm.getResultCode()==70002){
//				logger.warn("goldpayTransConfirm status has completed");
//				map.put("retCode", ServerConsts.TRANSFER_GOLDPAYTRANS_HAS_COMPLETED);
//				map.put("msg", "CHECK_PIN_CODE_FAIL");
//				return map;
//			} 
		}else{
			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
			map.put("msg", "fail");
			return map;
		}
	
	}


	@Override
	public HashMap<String, String> goldpayWithdraw(int userId, double amount) {
		
		HashMap<String, String> map = new HashMap<String, String>();

		User systemUser = userDAO.getSystemUser();
		User payer = userDAO.getUser(userId);
		if(payer ==null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
			logger.warn("The user does not exist or the account is blocked");
			map.put("retCode", ServerConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			map.put("msg", "The user does not exist or the account is blocked");
			return map;
		}
		Bind bind = bindBAO.getBindByUserId(userId);
		if(bind == null){
			logger.warn("The account is not tied to goldpay");
			map.put("msg", "The account is not tied to goldpay");
			map.put("retCode", ServerConsts.GOLDPAY_NOT_BIND);
			return map;
		}
		
		//判断余额是否足够支付
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, ServerConsts.CURRENCY_OF_GOLDPAY);
		if(wallet == null || wallet.getBalance().compareTo(new BigDecimal(Double.toString(amount))) == -1){
			logger.warn("Current balance is insufficient");
			map.put("retCode", ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			map.put("msg", "Current balance is insufficient");
			return map;
		}
		
		//生成TransId
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		
		Transfer transfer = new Transfer(); 
		transfer.setTransferId(transferId);
		transfer.setCreateTime(new Date());
		transfer.setCurrency(ServerConsts.CURRENCY_OF_GOLDPAY);
		transfer.setTransferAmount(new BigDecimal(Double.toString(amount)));
		transfer.setTransferComment("goldpay withdraw");
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		transfer.setUserFrom(userId);
		transfer.setAreaCode(systemUser.getAreaCode());
		transfer.setPhone(systemUser.getUserPhone());
		transfer.setUserTo(systemUser.getUserId());
		transfer.setTransferType(ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW);
		transfer.setNoticeId(0);
		//保存
		transferDAO.addTransfer(transfer);
		
		map.put("retCode", ServerConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("transferId", transferId);
		
		return map;
	}


//	@Override
//	public HashMap<String, String> withdrawConfirm(int userId, String payPwd, String transferId) {
//		
//		HashMap<String, String> map = new HashMap<String, String>();
//		User systemUser = userDAO.getSystemUser();
//		User user = userDAO.getUser(userId);
//		if(user ==null || user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
//			logger.warn("The user does not exist or the account is blocked");
//			map.put("msg", "The user does not exist or the account is blocked");
//			map.put("retCode", ServerConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
//			return map;
//		}
//		if(!PasswordUtils.check(payPwd, user.getUserPayPwd(), user.getPasswordSalt())){
//			logger.warn("The payment password is incorrect");
//			map.put("msg", "The payment password is incorrect");
//			map.put("retCode", ServerConsts.TRANSFER_PAYMENTPWD_INCORRECT);
//			return map;
//		}
//		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
//		if(transfer == null || transfer.getTransferType() != ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW){
//			logger.warn("The transaction order does not exist");
//			map.put("msg", "The transaction order does not exist");
//			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
//			return map;
//		}
//		Bind bind = bindBAO.getBindByUserId(userId);
//		if(bind == null){
//			logger.warn("The account is not tied to goldpay");
//			map.put("msg", "The account is not tied to goldpay");
//			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
//			return map;
//		}
//		Bind systemBind = bindBAO.getBindByUserId(systemUser.getUserId());
//		if(systemBind == null){
//			logger.warn("System account is not bound to goldpay");
//			map.put("msg", "System account is not bound to goldpay");
//			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
//			return map;
//		}
//		
//		MerchantPayOrder  merchantPayOrder  = new MerchantPayOrder();
//		
//		merchantPayOrder.setFromAccountNum(systemBind.getGoldpayAcount());
//		merchantPayOrder.setFromAccountName(systemBind.getGoldpayName());
//		merchantPayOrder.setFromAccountToken(systemBind.getToken());
//		
//		merchantPayOrder.setToAccountNum(bind.getGoldpayAcount());
//		merchantPayOrder.setOrderId(transfer.getTransferId());
//		merchantPayOrder.setClientId(ResourceUtils.getBundleValue4String("client.id"));
//		merchantPayOrder.setPayAmount(transfer.getTransferAmount().intValue());
//		merchantPayOrder.setType(0);
//		
//		String sign = DigestUtils.md5Hex(JsonBinder.getInstance().toJson(merchantPayOrder)
//				+ResourceUtils.getBundleValue4String("client.key"));
//		
//		merchantPayOrder.setSign(sign.toUpperCase());
//		
//		String result = HttpTookit.sendPost(ResourceUtils.getBundleValue4String("tpps.url")+"merchantPay.do",
//				JsonBinder.getInstance().toJson(merchantPayOrder));
//		
//		PayModel payModel;
//		
//		if(!StringUtils.isEmpty(result)){
//			
//			logger.info("withdrawConfirm tpps callback result : {}",result);
//			payModel = JsonBinder.getInstance().fromJson(result, PayModel.class);
//			
//			if(payModel != null){
//				
//				if(payModel.getResultCode().equals(1)){
//					logger.info("withdrawConfirm tpps callback result :success");
//					
//					//用户扣款
//					int updateCount = walletDAO.updateWalletByUserIdAndCurrency(userId, 
//							transfer.getCurrency(), transfer.getTransferAmount(), "-");
//					
//					if(updateCount == 0){
//						logger.warn("Current balance is insufficient");
//						map.put("retCode", ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
//						return map;
//					}
//					
//					//系统加款
//					walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), 
//							transfer.getCurrency(), transfer.getTransferAmount(), "+");
//					//更改Transfer状态
////					transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
//					
//					transfer.setTransferComment(payModel.getOrderId());
//					transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
//					transfer.setFinishTime(new Date());
//					transferDAO.updateTransfer(transfer);
//					
//					//添加seq记录
//					walletSeqDAO.addWalletSeq4Transaction(userId, systemUser.getUserId(), 
//							ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW, transfer.getTransferId(), 
//							transfer.getCurrency(), transfer.getTransferAmount());	
//					
//					
//					map.put("retCode", ServerConsts.RET_CODE_SUCCESS);
//					map.put("msg", "ok");
//					return map;
//				}else{
//					map.put("msg", "something wrong!");
//					map.put("retCode", ServerConsts.RET_CODE_FAILUE);
//					if(payModel.getResultCode().equals(0)){
//						logger.warn("goldpayPurchase tpps callback: fail");
//					}else if(payModel.getResultCode().equals(-1)){
//						logger.warn("goldpayPurchase tpps callback: INVALID SIGN");
//					}else if(payModel.getResultCode().equals(-101)){
//						logger.warn("goldpayPurchase tpps callback: ORDERID REPEAT");
//					}else if(payModel.getResultCode().equals(-102)){
//						logger.warn("goldpayPurchase tpps callback: ORDERID_COMPLETE");
//					}else if(payModel.getResultCode().equals(200001)){
//						logger.warn("goldpayPurchase tpps callback: NOT_ENOUGH_GOLDPAY");
//						map.put("msg", "not enough goldpay!");
//						map.put("retCode", ServerConsts.TRANSFER_GOLDPAYTRANS_GOLDPAY_NOT_ENOUGH);
//					}else if(payModel.getResultCode().equals(1016)){
//						logger.warn("goldpayPurchase tpps callback: NOT_ENOUGH_GOLDPAY");
//						map.put("msg", "goldpay account not vaild phone!");
//						map.put("retCode", ServerConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
//					}
//					
//					return map;
//				}
//			}else{
//				logger.warn("withdrawConfirm tpps callback result : null");
//				map.put("msg", "something wrong!");
//				map.put("retCode", ServerConsts.RET_CODE_FAILUE);
//				return map;
//			}
//
//		}else{
//			logger.warn("withdrawConfirm tpps callback result : null");
//			map.put("msg", "withdrawConfirm tpps callback result : null");
//			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
//		}
//
//		return map;
//	}
	
	@Override
	public HashMap<String, String> withdrawConfirm1(int userId, String payPwd, String transferId){
		
		HashMap<String, String> map = new HashMap<String, String>();
		User systemUser = userDAO.getSystemUser();
		User user = userDAO.getUser(userId);
		if(user ==null || user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
			logger.warn("The user does not exist or the account is blocked");
			map.put("msg", "The user does not exist or the account is blocked");
			map.put("retCode", ServerConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			return map;
		}
		if(!PasswordUtils.check(payPwd, user.getUserPayPwd(), user.getPasswordSalt())){
			logger.warn("The payment password is incorrect");
			map.put("msg", "The payment password is incorrect");
			map.put("retCode", ServerConsts.TRANSFER_PAYMENTPWD_INCORRECT);
			return map;
		}
		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if(transfer == null || transfer.getTransferType() != ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW){
			logger.warn("The transaction order does not exist");
			map.put("msg", "The transaction order does not exist");
			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
			return map;
		}
		//用户扣款
		int updateCount = walletDAO.updateWalletByUserIdAndCurrency(userId, 
				transfer.getCurrency(), transfer.getTransferAmount(), "-");
		
		if(updateCount == 0){
			logger.warn("Current balance is insufficient");
			map.put("retCode", ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			return map;
		}
		//系统加款
		walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), 
				transfer.getCurrency(), transfer.getTransferAmount(), "+");
		
		//更改Transfer状态
		transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_PROCESSING);
		//添加seq记录
		walletSeqDAO.addWalletSeq4Transaction(userId, systemUser.getUserId(), 
				ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW, transfer.getTransferId(), 
				transfer.getCurrency(), transfer.getTransferAmount());	
		
		map.put("retCode", ServerConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		return map;
		
	}
	
	@Override
	public void withdrawRefund(int userId, String transferId, String transferCurrency, BigDecimal transferAmount){
		User systemUser = userDAO.getSystemUser();
		//系统加款
		walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), transferCurrency, transferAmount, "-");
		//用户扣款
		walletDAO.updateWalletByUserIdAndCurrency(userId,  transferCurrency, transferAmount, "+");
		//更改Transfer状态
		transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_REFUND);
		//添加seq记录
		walletSeqDAO.addWalletSeq4Transaction(systemUser.getUserId(), userId, 
				ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_REFUND, transferId, 
				transferCurrency, transferAmount);	
	}
	
	@Override
	public HashMap<String, String> withdrawConfirm2(int userId, String transferId){
		
		HashMap<String, String> map = new HashMap<String, String>();
		Bind bind = bindBAO.getBindByUserId(userId);
		if(bind == null){
			logger.warn("The account is not tied to goldpay");
			map.put("msg", "The account is not tied to goldpay");
			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
			return map;
		}
//		User systemUser = userDAO.getSystemUser();
//		Bind systemBind = bindBAO.getBindByUserId(systemUser.getUserId());
//		if(systemBind == null){
//			logger.warn("System account is not bound to goldpay");
//			map.put("msg", "System account is not bound to goldpay");
//			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
//			return map;
//		}

		Transfer transfer = transferDAO.getTransferById(transferId);
		
		if(transfer.getTransferStatus() != ServerConsts.TRANSFER_STATUS_OF_PROCESSING){
			logger.warn("The transaction status is not processing!");
			map.put("msg", "The transaction order does not exist");
			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
			return map;
		}

		MerchantPayOrder merchantPayOrder  = new MerchantPayOrder();
		merchantPayOrder.setFromAccountToken(configManager.getConfigStringValue(ConfigKeyEnum.TPPSTRANSTOKEN, ""));
		merchantPayOrder.setToAccountNum(bind.getGoldpayAcount());
		merchantPayOrder.setOrderId(transfer.getTransferId());
		merchantPayOrder.setClientId(configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTID, ""));
		merchantPayOrder.setPayAmount(transfer.getTransferAmount().intValue());
		merchantPayOrder.setType(0);
		
		String sign = DigestUtils.md5Hex(JsonBinder.getInstanceNonEmpty().toJson(merchantPayOrder)
				+configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTKEY, ""));
		
		merchantPayOrder.setSign(sign.toUpperCase());
		
		String result = HttpTookit.sendPost(ResourceUtils.getBundleValue4String("tpps.url")+"merchantPay.do",
				JsonBinder.getInstance().toJson(merchantPayOrder));
		
		PayModel payModel;
		
		if(!StringUtils.isEmpty(result)){
			
			logger.info("withdrawConfirm tpps callback result : {}",result);
			payModel = JsonBinder.getInstance().fromJson(result, PayModel.class);
			
			if(payModel != null){
				
				if(payModel.getResultCode().equals(1) || payModel.getResultCode().equals(-102)){
					logger.info("withdrawConfirm tpps callback result :success");
					
					transfer.setTransferComment(payModel.getPayOrderId());
					transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
					transfer.setFinishTime(new Date());
					transferDAO.updateTransfer(transfer);

					map.put("retCode", ServerConsts.RET_CODE_SUCCESS);
					map.put("msg", "ok");
					return map;
				}else{
					map.put("msg", "something wrong!");
					map.put("retCode", ServerConsts.RET_CODE_FAILUE);
					if(payModel.getResultCode().equals(0)){
						logger.warn("goldpayPurchase tpps callback: fail");
					}else if(payModel.getResultCode().equals(-1)){
						logger.warn("goldpayPurchase tpps callback: INVALID SIGN");
					}else if(payModel.getResultCode().equals(-101)){
						logger.warn("goldpayPurchase tpps callback: ORDERID REPEAT");
					}
//					else if(payModel.getResultCode().equals(-102)){
//						logger.warn("goldpayPurchase tpps callback: ORDERID_COMPLETE");
//					}
					else if(payModel.getResultCode().equals(200001)){
						logger.warn("goldpayPurchase tpps callback: NOT_ENOUGH_GOLDPAY");
						map.put("msg", "not enough goldpay!");
						map.put("retCode", ServerConsts.TRANSFER_GOLDPAYTRANS_GOLDPAY_NOT_ENOUGH);
					}else if(payModel.getResultCode().equals(1016)){
						logger.warn("goldpayPurchase tpps callback: GOLDPAY_PHONE_IS_NOT_EXIST");
						map.put("msg", "goldpay account not vaild phone!");
						map.put("retCode", ServerConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
					}
					return map;
				}
			}else{
				logger.warn("withdrawConfirm tpps callback result : null");
				map.put("msg", "something wrong!");
				map.put("retCode", ServerConsts.RET_CODE_FAILUE);
				return map;
			}

		}else{
			logger.warn("withdrawConfirm tpps callback result : null");
			map.put("msg", "withdrawConfirm tpps callback result : null");
			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
		}
		return map;
	}


	@Override
	public List<Transfer> findGoldpayWithdrawByTimeBefore(Date date) {
		return transferDAO.findTransferByStatusAndTimeBefore(ServerConsts.TRANSFER_STATUS_OF_PROCESSING, ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW, date);
	}
	
}

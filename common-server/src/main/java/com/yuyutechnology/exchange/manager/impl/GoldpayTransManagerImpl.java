package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.dto.CheckPwdResult;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.goldpay.transaction.CalculateCharge;
import com.yuyutechnology.exchange.goldpay.transaction.CalculateChargeReturnModel;
import com.yuyutechnology.exchange.goldpay.transaction.ClientComfirmPay;
import com.yuyutechnology.exchange.goldpay.transaction.ClientPayOrder;
import com.yuyutechnology.exchange.goldpay.transaction.ClientPin;
import com.yuyutechnology.exchange.goldpay.transaction.MerchantPayOrder;
import com.yuyutechnology.exchange.goldpay.transaction.PayConfirm;
import com.yuyutechnology.exchange.goldpay.transaction.PayModel;
import com.yuyutechnology.exchange.manager.AccountingManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.push.PushManager;
import com.yuyutechnology.exchange.util.HttpTookit;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.ResourceUtils;
import com.yuyutechnology.exchange.util.page.PageBean;

@Service
public class GoldpayTransManagerImpl implements GoldpayTransManager {

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
	@Autowired
	UserManager userManager;
	@Autowired
	RedisDAO redisDAO;
	@Autowired
	AccountingManager accountingManager;
	@Autowired
	PushManager pushManager;
	@Autowired
	CrmAlarmDAO crmAlarmDAO;
	@Autowired
	CrmAlarmManager crmAlarmManager;

	private final String GOLDPAY_WITHDRAW_FORBIDDEN = "goldpayWithdrawForbidden";

	public static Logger logger = LogManager.getLogger(GoldpayTransManagerImpl.class);

	private   SimpleDateFormat simpleDateFormat =   new SimpleDateFormat( "yyyy-MM-dd" );
	
	
	@Override
	public HashMap<String, String> goldpayPurchase(int userId, BigDecimal amount) {

		HashMap<String, String> map = new HashMap<>();

		User user = userDAO.getUser(userId);
		if (user == null || user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
			logger.warn("The user does not exist or the account is blocked");
			map.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			map.put("msg", "The user does not exist or the account is blocked");
			return map;
		}
		Bind bind = bindBAO.getBindByUserId(userId);
		if (bind == null) {
			logger.warn("The account is not tied to goldpay");
			map.put("msg", "The account is not tied to goldpay");
			map.put("retCode", RetCodeConsts.GOLDPAY_NOT_BIND);
			return map;
		}
		// 生成TransId
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		ClientPayOrder clientPayOrder = new ClientPayOrder();
		clientPayOrder.setOrderId(transferId);
		clientPayOrder.setPayAmount(amount.intValue());
		clientPayOrder.setFromAccountNum(bind.getGoldpayAcount());
		clientPayOrder.setType(0);
		clientPayOrder.setClientId(configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTID, ""));

		String sign = DigestUtils.md5Hex(JsonBinder.getInstance().toJson(clientPayOrder)
				+ configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTKEY, ""));
		clientPayOrder.setSign(sign.toUpperCase());

		String result = HttpTookit.sendPost(ResourceUtils.getBundleValue4String("tpps.url") + "clientPay.do",
				JsonBinder.getInstance().toJson(clientPayOrder));

		PayModel payModel;

		if (!StringUtils.isEmpty(result)) {

			logger.info("tpps callback result : {}", result);

			payModel = JsonBinder.getInstance().fromJson(result, PayModel.class);

			// 错误处理
			if (payModel != null && !payModel.getResultCode().equals(1)) {
				map.put("msg", "something wrong!");
				map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
				if (payModel.getResultCode().equals(0)) {
					logger.warn("goldpayPurchase tpps callback: fail");
				} else if (payModel.getResultCode().equals(-1)) {
					logger.warn("goldpayPurchase tpps callback: INVALID SIGN");
				} else if (payModel.getResultCode().equals(-101)) {
					logger.warn("goldpayPurchase tpps callback: ORDERID REPEAT");
				} else if (payModel.getResultCode().equals(-102)) {
					logger.warn("goldpayPurchase tpps callback: ORDERID_COMPLETE");
				} else if (payModel.getResultCode().equals(200001) || payModel.getResultCode().equals(200008)) {
					logger.warn("goldpayPurchase tpps callback: NOT_ENOUGH_GOLDPAY");
					map.put("msg", "not enough goldpay!");
					map.put("retCode", RetCodeConsts.TRANSFER_GOLDPAYTRANS_GOLDPAY_NOT_ENOUGH);
				} else if (payModel.getResultCode().equals(1016)) {
					logger.warn("goldpay account not vaild phone!");
					map.put("msg", "goldpay account not vaild phone!");
					map.put("retCode", RetCodeConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
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
			
			//add by Niklaus at 2017-06-07
			transfer.setGoldpayName(bind.getGoldpayName());
			transfer.setGoldpayAcount(bind.getGoldpayAcount());
			
			// 保存
			transferDAO.addTransfer(transfer);

			map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
			map.put("msg", "ok");
			map.put("transferId", transferId);
		} else {
			map.put("msg", "something wrong!");
			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
		}

		return map;
	}

	@Override
	public HashMap<String, String> requestPin(int userId, String transferId) {

		HashMap<String, String> map = new HashMap<>();

		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,
				ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if (transfer == null || transfer.getTransferType() != ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE) {
			logger.warn("The transaction order does not exist");
			map.put("msg", "The transaction order does not exist");
			map.put("retCode", RetCodeConsts.TRANSFER_TRANS_ORDERID_NOT_EXIST);
			return map;
		}

		ClientPin clientPin = new ClientPin();
		clientPin.setClientId(configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTID, ""));
		clientPin.setPayOrderId(transfer.getTransferComment());

		String sign = DigestUtils.md5Hex(JsonBinder.getInstance().toJson(clientPin)
				+ configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTKEY, ""));

		clientPin.setSign(sign.toUpperCase());

		String result = HttpTookit.sendPost(ResourceUtils.getBundleValue4String("tpps.url") + "clientPin.do",
				JsonBinder.getInstance().toJson(clientPin));

		PayModel payModel;

		if (!StringUtils.isEmpty(result)) {

			logger.info("tpps callback result : {}", result);
			payModel = JsonBinder.getInstance().fromJson(result, PayModel.class);

			if (payModel != null && !payModel.getResultCode().equals(1)) { // 1:成功
				logger.warn("requestPin tpps callback: {}", payModel.getResultMessage());
				map.put("msg", "something wrong!");
				map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
				return map;
			}

			map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
			map.put("msg", "ok");
			map.put("transferId", transferId);
		} else {
			logger.warn("tpps callback result : null");
			map.put("msg", "something wrong!");
			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
		}

		return map;
	}

	@Override
	public HashMap<String, String> goldpayTransConfirm(int userId, String pin, String transferId) {

		HashMap<String, String> map = new HashMap<>();

		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,
				ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if (transfer == null || transfer.getTransferType() != ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE) {
			map.put("retCode", RetCodeConsts.TRANSFER_GOLDPAYTRANS_ORDERID_NOT_EXIST);
			map.put("msg", "Order does not exist or has completed");
			return map;
		}

		ClientComfirmPay clientComfirmPay = new ClientComfirmPay();
		clientComfirmPay.setClientId(configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTID, ""));
		clientComfirmPay.setPin(StringUtils.defaultString(pin));
		clientComfirmPay.setPayOrderId(transfer.getTransferComment());

		String sign = DigestUtils.md5Hex(JsonBinder.getInstance().toJson(clientComfirmPay)
				+ configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTKEY, ""));

		clientComfirmPay.setSign(sign.toUpperCase());

		String result = HttpTookit.sendPost(ResourceUtils.getBundleValue4String("tpps.url") + "clientComfirmPay.do",
				JsonBinder.getInstance().toJson(clientComfirmPay));
		PayConfirm payConfirm;
		if (StringUtils.isNotBlank(result)) {
			logger.info("goldpayTransConfirm tpps callback {} ", result);
			payConfirm = JsonBinder.getInstance().fromJson(result, PayConfirm.class);
			if (payConfirm != null && (payConfirm.getResultCode() == 1 || payConfirm.getResultCode() == 70002)) {
				User systemUser = userDAO.getSystemUser();
				// 系统扣款
				walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), transfer.getCurrency(),
						transfer.getTransferAmount(), "-", ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE, transferId);
				// 用户加款
				walletDAO.updateWalletByUserIdAndCurrency(userId, transfer.getCurrency(), transfer.getTransferAmount(),
						"+", ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE, transferId);

				// 更改订单状态
				transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);

				map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
				map.put("msg", "ok");
				return map;
			} else {
				map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
				map.put("msg", "fail");
				if (payConfirm != null && payConfirm.getResultCode() == 307) {
					logger.warn("goldpayTransConfirm tpps callback  error ! {}  CHECK_PIN_CODE_FAIL");
					map.put("retCode", RetCodeConsts.TRANSFER_GOLDPAYTRANS_CHECK_PIN_CODE_FAIL);
					map.put("msg", "CHECK_PIN_CODE_FAIL");
				}
				return map;
			}
		} else {
			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			map.put("msg", "fail");
			return map;
		}

	}

	// 对通过审核的提现进行goldpay划账
	@Override
	public HashMap<String, String> goldpayWithdraw(int userId, double amount) {

		HashMap<String, String> map = new HashMap<String, String>();

		User systemUser = userDAO.getSystemUser();
		User payer = userDAO.getUser(userId);
		if (payer == null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
			logger.warn("The user does not exist or the account is blocked");
			map.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			map.put("msg", "The user does not exist or the account is blocked");
			return map;
		}
		Bind bind = bindBAO.getBindByUserId(userId);
		if (bind == null) {
			logger.warn("The account is not tied to goldpay");
			map.put("msg", "The account is not tied to goldpay");
			map.put("retCode", RetCodeConsts.GOLDPAY_NOT_BIND);
			return map;
		}

		// 判断余额是否足够支付
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, ServerConsts.CURRENCY_OF_GOLDPAY);
		if (wallet == null || wallet.getBalance().compareTo(new BigDecimal(Double.toString(amount))) == -1) {
			logger.warn("Current balance is insufficient");
			map.put("retCode", RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			map.put("msg", "Current balance is insufficient");
			return map;
		}

		map.put("charge", "0");
		CalculateCharge calculateCharge = new CalculateCharge();
		calculateCharge.setClientId(configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTID, ""));
		calculateCharge.setAmount(Double.valueOf(amount).longValue());
		calculateCharge.setAccountNum(bind.getGoldpayAcount());
		calculateCharge.setTo(true);
		String sign = DigestUtils.md5Hex(JsonBinder.getInstance().toJson(calculateCharge)
				+ configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTKEY, ""));
		calculateCharge.setSign(sign.toUpperCase());

		String result = HttpTookit.sendPost(ResourceUtils.getBundleValue4String("tpps.url") + "calculateCharge.do",
				JsonBinder.getInstance().toJson(calculateCharge));
		CalculateChargeReturnModel calculateChargeReturnModel;
		if (StringUtils.isNotBlank(result)) {
			logger.info("calculateCharge tpps callback {} ", result);
			calculateChargeReturnModel = JsonBinder.getInstance().fromJson(result, CalculateChargeReturnModel.class);
			if (calculateChargeReturnModel != null && calculateChargeReturnModel.getResultCode() == 1
					&& calculateChargeReturnModel.getChargeType() == 2) {
				map.put("charge", calculateChargeReturnModel.getChargeAmount() + "");
			} else {
				map.put("charge", "0");
			}
		}

		// 生成TransId
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);

		Transfer transfer = new Transfer();
		transfer.setTransferId(transferId);
		transfer.setCreateTime(new Date());
		transfer.setFinishTime(new Date());
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
		transfer.setGoldpayName(bind.getGoldpayName());
		transfer.setGoldpayAcount(bind.getGoldpayAcount());
		// 保存
		transferDAO.addTransfer(transfer);

		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("transferId", transferId);

		return map;
	}

	@Override
	public HashMap<String, String> withdrawConfirm1(int userId, String payPwd, String transferId) {

		HashMap<String, String> map = new HashMap<String, String>();
		User systemUser = userDAO.getSystemUser();
		User user = userDAO.getUser(userId);
		if (user == null || user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE) {
			logger.warn("The user does not exist or the account is blocked");
			map.put("msg", "The user does not exist or the account is blocked");
			map.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			return map;
		}
		/* 验证支付密码 */
		CheckPwdResult checkPwdResult = userManager.checkPayPassword(userId, payPwd);
		switch (checkPwdResult.getStatus()) {
		case ServerConsts.CHECKPWD_STATUS_FREEZE:
			map.put("msg", String.valueOf(checkPwdResult.getInfo()));
			map.put("retCode", RetCodeConsts.PAY_FREEZE);
			return map;
		case ServerConsts.CHECKPWD_STATUS_INCORRECT:
			logger.warn("payPwd is wrong !");
			map.put("msg", String.valueOf(checkPwdResult.getInfo()));
			map.put("retCode", RetCodeConsts.PAY_PWD_NOT_MATCH);
			return map;
		default:
			break;
		}

		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,
				ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if (transfer == null || transfer.getTransferType() != ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW) {
			logger.warn("The transaction order does not exist");
			map.put("msg", "The transaction order does not exist");
			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			return map;
		}
		// 用户扣款
		int updateCount = walletDAO.updateWalletByUserIdAndCurrency(userId, transfer.getCurrency(),
				transfer.getTransferAmount(), "-", ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW,
				transfer.getTransferId());

		if (updateCount == 0) {
			logger.warn("Current balance is insufficient");
			map.put("retCode", RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
			return map;
		}
		// 系统加款
		walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), transfer.getCurrency(),
				transfer.getTransferAmount(), "+", ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW,
				transfer.getTransferId());

		// 更改Transfer状态
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_PROCESSING);
		transfer.setFinishTime(new Date());
		transferDAO.updateTransfer(transfer);

		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		return map;

	}

	@Override
	public void withdrawRefund(String transferId) {
		User systemUser = userDAO.getSystemUser();
		Transfer transfer = transferDAO.getTransferById(transferId);
		User user = userDAO.getUser(transfer.getUserFrom());

		/* 系统扣款 */
		walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), transfer.getCurrency(),
				transfer.getTransferAmount(), "-", ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_REFUND, transferId);
		/* 用户加款 */
		walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), transfer.getCurrency(),
				transfer.getTransferAmount(), "+", ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_REFUND, transferId);
		/* 更改Transfer状态 */
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_REFUND);
		transfer.setFinishTime(new Date());
		transferDAO.updateTransfer(transfer);

		//  推送：提现退回
		pushManager.push4WithdrawRefund(user.getPushId(), user.getPushTag(), transfer.getTransferAmount());
	}

	@Override
	public HashMap<String, String> goldpayRemit(String transferId) {
		logger.info("goldpayRemit {}==>", transferId);

		HashMap<String, String> map = new HashMap<String, String>();

		Transfer transfer = transferDAO.getTransferById(transferId);

		if (transfer.getTransferStatus() != ServerConsts.TRANSFER_STATUS_OF_AUTOREVIEW_SUCCESS) {
			transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_GOLDPAYREMIT_FAIL);
			transfer.setFinishTime(new Date());
			transferDAO.updateTransfer(transfer);

			logger.warn("The transaction status is not processing!{}", transfer.getTransferStatus());
			map.put("msg", "The transaction order does not exist");
			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			return map;
		}

		MerchantPayOrder merchantPayOrder = new MerchantPayOrder();
		merchantPayOrder.setFromAccountToken(configManager.getConfigStringValue(ConfigKeyEnum.TPPSTRANSTOKEN, ""));
		merchantPayOrder.setToAccountNum(transfer.getGoldpayAcount());
		merchantPayOrder.setOrderId(transfer.getTransferId());
		merchantPayOrder.setClientId(configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTID, ""));
		merchantPayOrder.setPayAmount(transfer.getTransferAmount().intValue());
		merchantPayOrder.setType(0);

		String sign = DigestUtils.md5Hex(JsonBinder.getInstance().toJson(merchantPayOrder)
				+ configManager.getConfigStringValue(ConfigKeyEnum.TPPSCLIENTKEY, ""));

		merchantPayOrder.setSign(sign.toUpperCase());

		String result = HttpTookit.sendPost(ResourceUtils.getBundleValue4String("tpps.url") + "merchantPay.do",
				JsonBinder.getInstance().toJson(merchantPayOrder));

		transfer.setGoldpayResult(result);

		if (!StringUtils.isEmpty(result)) {

			logger.info("withdrawConfirm tpps callback result : {}", result);
			PayModel payModel = JsonBinder.getInstance().fromJson(result, PayModel.class);

			if (payModel != null) {

				if (payModel.getResultCode().equals(1) || payModel.getResultCode().equals(-102)) {
					logger.info("withdrawConfirm tpps callback result :success");

					transfer.setTransferComment(payModel.getPayOrderId());
					transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
					transfer.setFinishTime(new Date());
					transferDAO.updateTransfer(transfer);

					User user = userDAO.getUser(transfer.getUserFrom());
					//  推送：提现成功
					pushManager.push4WithdrawComplete(user.getPushId(), user.getPushTag(),
							transfer.getTransferAmount());

					map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
					map.put("msg", "ok");
					return map;
				} else {
					goldpayRemitFailWarn();
					
					transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_GOLDPAYREMIT_FAIL);
					transferDAO.updateTransfer(transfer);

					map.put("msg", "something wrong!");
					map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
					if (payModel.getResultCode().equals(0)) {
						logger.warn("goldpayPurchase tpps callback: fail");
					} else if (payModel.getResultCode().equals(-1)) {
						logger.warn("goldpayPurchase tpps callback: INVALID SIGN");
					} else if (payModel.getResultCode().equals(-101)) {
						logger.warn("goldpayPurchase tpps callback: ORDERID REPEAT");
					}
					// else if(payModel.getResultCode().equals(-102)){
					// logger.warn("goldpayPurchase tpps callback:
					// ORDERID_COMPLETE");
					// }
					else if (payModel.getResultCode().equals(200001) || payModel.getResultCode().equals(200008)) {
						logger.warn("goldpayPurchase tpps callback: NOT_ENOUGH_GOLDPAY");
						map.put("msg", "not enough goldpay!");
						map.put("retCode", RetCodeConsts.TRANSFER_GOLDPAYTRANS_GOLDPAY_NOT_ENOUGH);
					} else if (payModel.getResultCode().equals(1016)) {
						logger.warn("goldpayPurchase tpps callback: GOLDPAY_PHONE_IS_NOT_EXIST");
						map.put("msg", "goldpay account not vaild phone!");
						map.put("retCode", RetCodeConsts.GOLDPAY_PHONE_IS_NOT_EXIST);
					}
					return map;
				}
			} else {

				transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_GOLDPAYREMIT_FAIL);
				transfer.setFinishTime(new Date());
				transferDAO.updateTransfer(transfer);

				logger.warn("withdrawConfirm tpps callback result : null");
				map.put("msg", "something wrong!");
				map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
				return map;
			}

		} else {

			transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_GOLDPAYREMIT_FAIL);
			transfer.setFinishTime(new Date());
			transferDAO.updateTransfer(transfer);

			logger.warn("withdrawConfirm tpps callback result : null");
			map.put("msg", "withdrawConfirm tpps callback result : null");
			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			return map;
		}

	}

	@Override
	public void withdrawReviewAuto(String transferId) {
		logger.info("withdrawReviewAuto {}==>", transferId);
		Transfer transfer = transferDAO.getTransferById(transferId);
		User user = userDAO.getUser(transfer.getUserFrom());
		if (user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE
				|| !accountingManager.accountingUser(transfer.getUserFrom(), transferId)) {
			transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_AUTOREVIEW_FAIL);
		} else {
			transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_AUTOREVIEW_SUCCESS);
		}
		// 审核成功
		transfer.setFinishTime(new Date());
		transferDAO.updateTransfer(transfer);
	}

	@Override
	public PageBean getWithdrawList(int currentPage, String userPhone, String transferId, String[] transferStatus) {
		logger.info("currentPage={},userPhone={},transferId={},transferStatus={}", currentPage, userPhone, transferId,
				transferStatus);
		
		List<Object> values = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(
				"from Transfer t, User u where t.userFrom = u.userId and t.transferType = ? and t.transferStatus<>? ");
		values.add(ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW);
		values.add(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if (StringUtils.isNotBlank(userPhone)) {
			hql.append(" and u.userPhone = ?");
			values.add(userPhone);
		}
		if (StringUtils.isNotBlank(transferId)) {
			hql.append(" and t.transferId = ?");
			values.add(transferId);
		}
		// System.out.println(transferStatus.length);
		if (transferStatus.length > 0) {
			hql.append(" and ( t.transferStatus = ?");
			values.add(Integer.parseInt(transferStatus[0]));
			for (int i = 1; i < transferStatus.length; i++) {
				hql.append(" or t.transferStatus = ?");
				values.add(Integer.parseInt(transferStatus[i]));
			}
			hql.append(")");
		}
		hql.append(" order by t.transferStatus,t.finishTime desc");
		return transferDAO.searchTransfersByPage(hql.toString(), values, currentPage, 10);
	}

	@Override
	public PageBean getRechargeList(int currentPage, String startTime, String endTime, String transferType) throws ParseException {
		logger.info("currentPage={},startTime={},endTime={},transferType={}", currentPage, startTime, endTime,
				transferType);
		
		List<Object> values = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(
				"from Transfer t, User u where t.userTo = u.userId and t.transferStatus = ? and t.transferType = ? ");
		values.add(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		values.add(Integer.parseInt(transferType));
		if (StringUtils.isNotBlank(startTime)) {
			hql.append(" and t.finishTime >  ?");
			values.add(simpleDateFormat.parse(startTime));
		}
		if (StringUtils.isNotBlank(endTime)) {
			hql.append(" and t.finishTime < ?");
			values.add(simpleDateFormat.parse(endTime));
		}
		hql.append(" order by t.finishTime desc");
		return transferDAO.searchTransfersByPage(hql.toString(), values, currentPage, 10);
	}
	
	@Override
	public List<Transfer> getNeedGoldpayRemitWithdraws() {
		// if (!getGoldpayRemitWithdrawsforbidden()) {
		return transferDAO.getNeedGoldpayRemitWithdraws();
		// }
		// return new ArrayList<Transfer>();
	}

	@Override
	public List<Transfer> getNeedReviewWithdraws() {
		if (!getGoldpayRemitWithdrawsforbidden()) {
			return transferDAO.getNeedReviewWithdraws();
		}
		return new ArrayList<Transfer>();
	}

	@Override
	public PageBean getWithdrawRecordByPage(Integer userId, int currentPage, int pageSize) {
		return transferDAO.getWithdrawRecordByPage(userId, currentPage, pageSize);
	}

	@Override
	public void forbiddenGoldpayRemitWithdraws(String forbidden) {
		redisDAO.saveData(GOLDPAY_WITHDRAW_FORBIDDEN, forbidden);
	}

	@Override
	public boolean getGoldpayRemitWithdrawsforbidden() {
		String goldpSwitch = redisDAO.getValueByKey(GOLDPAY_WITHDRAW_FORBIDDEN);
		return Boolean.parseBoolean(goldpSwitch);
	}

	@Override
	public void withdrawReviewPending(String transferId) {
		Transfer transfer = transferDAO.getTransferById(transferId);
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_PROCESSING);
		transfer.setFinishTime(new Date());
		transferDAO.updateTransfer(transfer);
	}

	@Override
	public void goldpayRemitPending(String transferId) {
		Transfer transfer = transferDAO.getTransferById(transferId);
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_AUTOREVIEW_SUCCESS);
		transfer.setFinishTime(new Date());
		transferDAO.updateTransfer(transfer);
	}
	/**
	 * goldpay划账失败 预警
	 */
	private void goldpayRemitFailWarn(){
		List<CrmAlarm> list = crmAlarmDAO.getConfigListByTypeAndStatus(3, 1);
		if(list != null && !list.isEmpty()){
			logger.info("goldpay remit fail Warn listSize: {}", list.size());
			for (int i = 0; i < list.size(); i++) {
				CrmAlarm crmAlarm = list.get(i);
				logger.info("goldpay remit fail Warn crmAlarm: {}", crmAlarm.getSupervisorIdArr());
				crmAlarmManager.alarmNotice(crmAlarm.getSupervisorIdArr(), "goldpayRemitFailWarning", crmAlarm.getAlarmMode(),null);
			}
		}
	}
}

package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.NotificationDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.TransactionNotification;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.push.PushManager;
import com.yuyutechnology.exchange.sms.SmsManager;
import com.yuyutechnology.exchange.utils.DateFormatUtils;
import com.yuyutechnology.exchange.utils.PasswordUtils;
import com.yuyutechnology.exchange.utils.ResourceUtils;

@Service
public class TransferManagerImpl implements TransferManager{
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	RedisDAO redisDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	CurrencyDAO currencyDAO;
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	WalletSeqDAO walletSeqDAO;
	@Autowired
	UnregisteredDAO unregisteredDAO;
	@Autowired
	NotificationDAO notificationDAO;
	
	@Autowired
	ExchangeRateManager exchangeRateManager;
	@Autowired
	UserManager userManager; 
	@Autowired
	PushManager pushManager;
	@Autowired
	SmsManager smsManager;
	@Autowired
	CommonManager commonManager;
	
	public static Logger logger = LoggerFactory.getLogger(TransferManagerImpl.class);

	@Override
	public HashMap<String, String> transferInitiate(int userId,String areaCode,String userPhone, String currency, 
			BigDecimal amount, String transferComment,int noticeId) {
	
		HashMap<String, String> map = new HashMap<String, String>();
		
		if(!commonManager.verifyCurrency(currency)){
			logger.warn("This currency is not a tradable currency");
			map.put("retCode", ServerConsts.TRANSFER_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			map.put("msg", "This currency is not a tradable currency");
			return map;
		}

		User payer = userDAO.getUser(userId);
		if(payer ==null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
			logger.warn("The user does not exist or the account is blocked");
			map.put("retCode", ServerConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			map.put("msg", "The user does not exist or the account is blocked");
			return map;
		}
		User receiver = userDAO.getUserByUserPhone(areaCode, userPhone);
		//不用给自己转账
		if(receiver!= null && userId == receiver.getUserId()){
			logger.warn("Prohibit transfers to yourself");
			map.put("retCode", ServerConsts.TRANSFER_PROHIBIT_TRANSFERS_TO_YOURSELF);
			map.put("msg", "Prohibit transfers to yourself");
			return map;
		}
		
		//判断余额是否足够支付
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
		if(wallet == null || wallet.getBalance().compareTo(amount) == -1){
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
		transfer.setCurrency(currency);
		transfer.setTransferAmount(amount);
		transfer.setTransferComment(transferComment);
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		transfer.setUserFrom(userId);
		transfer.setAreaCode(areaCode);
		transfer.setPhone(userPhone);
		//判断接收人是否是已注册账号
		if(receiver != null){
			transfer.setUserTo(receiver.getUserId());
			transfer.setTransferType(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		}else{
			transfer.setUserTo(0);
			transfer.setTransferType(ServerConsts.TRANSFER_TYPE_OUT_INVITE);
		}
		transfer.setNoticeId(noticeId);
		//保存
		transferDAO.addTransfer(transfer);
		
		map.put("retCode", ServerConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("transferId", transferId);
		
		return map;
	}

	@Override
	public String payPwdConfirm(int userId, String transferId, String userPayPwd) {
		
		User user = userDAO.getUser(userId);
		if(user ==null || user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
			logger.warn("The user does not exist or the account is blocked");
			return ServerConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED;
		}
		if(!PasswordUtils.check(userPayPwd, user.getUserPayPwd(), user.getPasswordSalt())){
			return ServerConsts.TRANSFER_PAYMENTPWD_INCORRECT;
		}
		Transfer transfer = transferDAO.getTransferByIdAndUserId(transferId,userId);
		if(transfer == null){
			logger.warn("The transaction order does not exist");
			return ServerConsts.TRANSFER_TRANS_ORDERID_NOT_EXIST;
		}
		
		Currency standardCurrency = currencyDAO.getStandardCurrency();
		
		//总账大于设置安全基数，弹出需要短信验证框===============================================
		BigDecimal totalBalance =  exchangeRateManager.getTotalBalance(userId);
		BigDecimal totalBalanceMax =  standardCurrency.getAssetThreshold();
		//当天累计转出总金额大于设置安全基数，弹出需要短信验证框
		BigDecimal accumulatedAmount =  transferDAO.getAccumulatedAmount(userId+"");
		BigDecimal accumulatedAmountMax =  standardCurrency.getTransferMax();
		//单笔转出金额大于设置安全基数，弹出需要短信验证框
		BigDecimal AmountofSingleTransfer =  standardCurrency.getTransferLarge();
		
		if(totalBalance.compareTo(totalBalanceMax) == 1 || 
				( accumulatedAmount.compareTo(accumulatedAmountMax) == 1 || 
				transfer.getTransferAmount().compareTo(AmountofSingleTransfer) == 1)){
			logger.warn("The transaction amount exceeds the limit");
			return ServerConsts.TRANSFER_REQUIRES_PHONE_VERIFICATION;
			
		}else{
			transferConfirm(userId,transferId);
		}
		
		return ServerConsts.RET_CODE_SUCCESS;
	}

	@Override
	public String transferConfirm(int userId,String transferId) {
		
		Transfer transfer = transferDAO.getTransferByIdAndUserId(transferId,userId);
		if(transfer == null){
			logger.warn("The transaction order does not exist");
			return ServerConsts.TRANSFER_TRANS_ORDERID_NOT_EXIST;
		}
		
		User payer = userDAO.getUser(userId);
		if(payer ==null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
			logger.warn("The user does not exist or the account is blocked");
			return ServerConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED;
		}
		
		if(transfer.getUserTo() == 0){  	//交易对象没有注册账号
			
			//获取系统账号
			User systemUser = userDAO.getSystemUser();
			//扣款
			Integer updateCount = walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "-");
			
			if(updateCount == 0){
				return ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT;
			}
			
			//加款
			walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "+");
			//添加gift记录
			Unregistered unregistered = new Unregistered();
			unregistered.setCreateTime(new Date());
			unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_PENDING);
			unregistered.setTransferId(transfer.getTransferId());
			unregistered.setAreaCode(transfer.getAreaCode());
			unregistered.setUserPhone(transfer.getPhone());
			unregistered.setCurrency(transfer.getCurrency());
			unregistered.setAmount(transfer.getTransferAmount());
			
			unregisteredDAO.addUnregistered(unregistered);
			//增加seq记录
			walletSeqDAO.addWalletSeq4Transaction(transfer.getUserFrom(), systemUser.getUserId(), 
					ServerConsts.TRANSFER_TYPE_OUT_INVITE, transfer.getTransferId(), 
					transfer.getCurrency(), transfer.getTransferAmount());
			
			//更改Transfer状态
			transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
			
			//向未注册用户发送短信
			smsManager.sendSMS4Transfer(transfer.getAreaCode(), transfer.getPhone(), payer,
					transfer.getCurrency(), transfer.getTransferAmount());
			
		
		}else{	//交易对象注册账号,交易正常进行，无需经过系统账户							
			
			//扣款
			Integer updateCount = walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "-");
			
			if(updateCount == 0){
				return ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT;
			}
			
			//加款
			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserTo(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "+");
			
			//添加seq记录
			walletSeqDAO.addWalletSeq4Transaction(transfer.getUserFrom(), transfer.getUserTo(), 
					ServerConsts.TRANSFER_TYPE_TRANSACTION, transfer.getTransferId(), 
					transfer.getCurrency(), transfer.getTransferAmount());	
			
			//如果是请求转账还需要更改消息通知中的状态
			if(transfer.getNoticeId() != 0){
				TransactionNotification notification =  notificationDAO.getNotificationById(transfer.getNoticeId());
				notification.setTradingStatus(ServerConsts.NOTIFICATION_STATUS_OF_ALREADY_PAID);
				notificationDAO.updateNotification(notification);
			}
			//推送到账通知

			User payee = userDAO.getUser(transfer.getUserTo());
			pushManager.push4Transfer(payer, payee, transfer.getCurrency(), transfer.getTransferAmount());
			
		}
		//更改Transfer状态
		transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);

		//转换金额
		BigDecimal exchangeResult = exchangeRateManager.getExchangeResult(transfer.getCurrency(),transfer.getTransferAmount());
		transferDAO.updateAccumulatedAmount(transfer.getUserFrom()+"", exchangeResult);
		
		return ServerConsts.RET_CODE_SUCCESS;
	}
	

	@Override
	public void systemRefund(Unregistered unregistered) {
		
		Transfer transfer = transferDAO.getTransferById(unregistered.getTransferId());
		User systemUser = userDAO.getSystemUser();
		
		//系统扣款
		walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), 
				transfer.getCurrency(), transfer.getTransferAmount(), "-");
		//用户加款
		walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
				transfer.getCurrency(), transfer.getTransferAmount(), "+");
		//添加Seq记录
		walletSeqDAO.addWalletSeq4Transaction(systemUser.getUserId(), transfer.getUserFrom(), 
				ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND, unregistered.getTransferId(), 
				transfer.getCurrency(), transfer.getTransferAmount());
		///////////////////////////生成transfer系统退款订单////////////////////////////
		Transfer transfer2 = new Transfer();
		//生成TransId
		String transferId2 = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		transfer2.setTransferId(transferId2);
		transfer2.setUserFrom(systemUser.getUserId());
		transfer2.setUserTo(transfer.getUserFrom());
		transfer2.setAreaCode(unregistered.getAreaCode());
		transfer2.setPhone(unregistered.getUserPhone());
		transfer2.setCurrency(transfer.getCurrency());
		transfer2.setTransferAmount(transfer.getTransferAmount());
		transfer2.setTransferComment(unregistered.getUserPhone()+"对方逾期未注册,系统退款");
		transfer2.setTransferType(ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND);
		transfer2.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		transfer2.setCreateTime(new Date());
		transfer2.setFinishTime(new Date());
		transfer2.setNoticeId(0);
		
		transferDAO.addTransfer(transfer2);
		///////////////////////////end////////////////////////////
		//修改gift记录
		unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_BACK);
		unregisteredDAO.updateUnregistered(unregistered);
		
		//发送推送
		User payee = userDAO.getUser(transfer.getUserFrom());
		pushManager.push4Refund(payee, payee.getAreaCode(),transfer.getAreaCode(),
				transfer.getPhone(), transfer.getTransferAmount());
		
	}
	
	@Override
	public void systemRefundBatch() {
		
		//获取所有未完成的订单
		List<Unregistered> list = unregisteredDAO.getAllUnfinishedTransaction();
		if(list.isEmpty()){
			return;
		}
		for (Unregistered unregistered : list) {
			//:TODO
			//判断是否超过期限
			long deadline = (new Integer(ResourceUtils.getBundleValue("refund.time")))*24*60*60*1000;
			if(new Date().getTime() - unregistered.getCreateTime().getTime() >= deadline){
				systemRefund(unregistered);
			} 
		}
	}
	

	@Override
	public String makeRequest(int userId, String payerAreaCode, String payerPhone, String currency, BigDecimal amount) {
		
		User payer = userDAO.getUserByUserPhone(payerAreaCode, payerPhone);
		if(payer != null){
			TransactionNotification transactionNotification = new TransactionNotification();
			transactionNotification.setSponsorId(userId);
			transactionNotification.setPayerId(payer.getUserId());
			transactionNotification.setCurrency(currency);
			transactionNotification.setAmount(amount);
			transactionNotification.setCreateAt(new Date());
			transactionNotification.setRemarks("");
			transactionNotification.setNoticeStatus(0);
			transactionNotification.setTradingStatus(ServerConsts.NOTIFICATION_STATUS_OF_PENDING);
			
			notificationDAO.addNotification(transactionNotification);
			
			//推送请求付款
			User payee = userDAO.getUser(userId);
			pushManager.push4TransferRuquest(payee, payer, currency, amount);
			
			
			return ServerConsts.RET_CODE_SUCCESS;
			
		}
		return ServerConsts.RET_CODE_FAILUE;
	}


	
	@Override
	public HashMap<String, Object> getTransactionRecordByPage(String period, 
			int userId,int currentPage, int pageSize) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "SELECT "+
				"t1.user_from,t1.currency,t1.transfer_amount, "+
				"CONCAT(t1.area_code,t1.phone), "+
				"t1.transfer_comment,t1.finish_time,t1.transfer_type ";
		StringBuilder sb = new StringBuilder(
				"FROM `e_transfer` t1 LEFT JOIN `e_user` t2  "+
				"ON  "+
				"t1.user_from = t2.user_id  "+
				"where t1.transfer_status=? "+
				"and (t1.user_from = ? or t1.user_to = ?) ");
		
		List<Object> values = new ArrayList<Object>();
		values.add(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		values.add(userId);
		values.add(userId);
		
		if(!period.equals("all")){
			switch (period) {
				case "today":
					sb.append("and t1.finish_time > ?");
					values.add(DateFormatUtils.getStartTime(sdf.format(new Date())));
					break;
					
				case "lastMonth":
					sb.append("and t1.finish_time > ?");
					Date date = DateFormatUtils.getpreDays(-30);
					values.add(DateFormatUtils.getStartTime(sdf.format(date)));
					break;
				case "last3Month":
					sb.append("and t1.finish_time > ?");
					date = DateFormatUtils.getpreDays(-90);
					values.add(DateFormatUtils.getStartTime(sdf.format(date)));		
					break;
				case "lastYear":
					sb.append("and t1.finish_time > ?");
					date = DateFormatUtils.getpreDays(-365);
					values.add(DateFormatUtils.getStartTime(sdf.format(date)));
					break;
				case "aYearAgo":
					sb.append("and t1.finish_time < ?");
					date = DateFormatUtils.getpreDays(-365);
					values.add(DateFormatUtils.getStartTime(sdf.format(date)));
					break;
		
				default:
					break;
			}
		}
		
		
		sb.append(" order by t1.finish_time desc");

		HashMap<String, Object> map = transferDAO.getTransactionRecordByPage(sql+sb.toString(),
				sb.toString(),values,currentPage, pageSize);
		return map;
	}

	@Override
	public HashMap<String, Object> getNotificationRecordsByPage(int userId, int currentPage, int pageSize) {
		String sql = "SELECT t1.notice_id,t2.area_code,t2.user_phone,"
				+ "t1.currency,t1.amount,t1.create_at,t1.trading_status ";
		StringBuilder sb = new StringBuilder(
				"FROM `e_transaction_notification` t1,`e_user` t2 "+ 
				"where t1.sponsor_id = t2.user_id and t1.payer_id = ?");
		
		List<Object> values = new ArrayList<Object>();
		values.add(userId);
		
		sb.append(" order by t1.create_at desc");

		HashMap<String, Object> map = notificationDAO.getNotificationRecordsByPage(sql+sb.toString(),
				sb.toString(),values,currentPage, pageSize);
		return map;
	}
	
	@Override
	public HashMap<String, String> respond2Request(int userId,String areaCode,String userPhone, 
			String currency,BigDecimal amount, String transferComment,int noticeId){
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		TransactionNotification notification = notificationDAO.getNotificationById(noticeId);
		
		if(notification == null){
			logger.warn("Can not find the corresponding notification information");
			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
			map.put("msg", "Can not find the corresponding notification information");
			return map;
		}else if(notification.getCurrency().equals(ServerConsts.CURRENCY_OF_GOLDPAY)
				&& notification.getAmount().compareTo(new BigDecimal(0))==0){
			logger.warn("The requestor does not enter the specified currency information");
			notification.setAmount(amount);
			notification.setCurrency(currency);
		}else if(!notification.getCurrency().equals(currency) || notification.getAmount().compareTo(amount) != 0){
			logger.warn("The input and order information do not match");
			map.put("retCode", ServerConsts.RET_CODE_FAILUE);
			map.put("msg", "The input and order information do not match");
			return map;
		}
		if(!commonManager.verifyCurrency(notification.getCurrency())){
			logger.warn("This currency is not a tradable currency");
			map.put("retCode", ServerConsts.TRANSFER_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			map.put("msg", "This currency is not a tradable currency");
			return map;
		}

		//账号冻结
		User payer = userDAO.getUser(userId);
		if(payer ==null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
			logger.warn("The user does not exist or the account is blocked");
			map.put("retCode", ServerConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			map.put("msg", "The user does not exist or the account is blocked");
			return map;
		}
		
		User receiver = userDAO.getUserByUserPhone(areaCode, userPhone);
		//不用给自己转账
		if(receiver!= null && userId == receiver.getUserId()){
			logger.warn("Prohibit transfers to yourself");
			map.put("retCode", ServerConsts.TRANSFER_PROHIBIT_TRANSFERS_TO_YOURSELF);
			map.put("msg", "Prohibit transfers to yourself");
			return map;
		}
		
		//判断余额是否足够支付
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
		if(wallet == null || wallet.getBalance().compareTo(amount) == -1){
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
		transfer.setCurrency(notification.getCurrency());
		transfer.setTransferAmount(notification.getAmount());
		transfer.setTransferComment(transferComment);
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		transfer.setUserFrom(userId);
		transfer.setUserTo(notification.getSponsorId());
		transfer.setAreaCode(areaCode);
		transfer.setPhone(userPhone);
		transfer.setTransferType(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		transfer.setNoticeId(noticeId);
		//保存
		transferDAO.addTransfer(transfer);
		
		map.put("retCode", ServerConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("transferId", transferId);
		
		return map;
		
	}
}

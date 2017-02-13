package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.NotificationDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.TransactionNotification;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.push.PushManager;
import com.yuyutechnology.exchange.sms.SmsManager;
import com.yuyutechnology.exchange.utils.DateFormatUtils;
import com.yuyutechnology.exchange.utils.PasswordUtils;

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
	@Autowired
	ConfigManager configManager;
	
	public static Logger logger = LoggerFactory.getLogger(TransferManagerImpl.class);

	@SuppressWarnings("serial")
	@Override
	public HashMap<String, String> transferInitiate(final int userId,String areaCode,String userPhone, final String currency, 
			final BigDecimal amount, String transferComment,int noticeId) {

		//干扰条件过滤
		HashMap<String, Object> args = new HashMap<>();		
		args.put("isTradableCurrency", currency);
		args.put("isAccountFrozened", userId);
		args.put("isInsufficientBalance",new HashMap<String,Object>(){{
			put("userId", userId);
			put("currency", currency);
			put("amount", amount);
		}} );
		HashMap<String, String> map = test(args);
		if(!map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)){
			return map;
		}
		//end
		
		User receiver = userDAO.getUserByUserPhone(areaCode, userPhone);
		//不用给自己转账
		if(receiver!= null && userId == receiver.getUserId()){
			logger.warn("Prohibit transfers to yourself");
			map.put("retCode", RetCodeConsts.TRANSFER_PROHIBIT_TRANSFERS_TO_YOURSELF);
			map.put("msg", "Prohibit transfers to yourself");
			return map;
		}
	
//		if(!commonManager.verifyCurrency(currency)){
//			logger.warn("This currency is not a tradable currency");
//			map.put("retCode", RetCodeConsts.TRANSFER_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
//			map.put("msg", "This currency is not a tradable currency");
//			return map;
//		}
//
//		User payer = userDAO.getUser(userId);
//		if(payer ==null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
//			logger.warn("The user does not exist or the account is blocked");
//			map.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
//			map.put("msg", "The user does not exist or the account is blocked");
//			return map;
//		}
		//判断余额是否足够支付
//		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
//		logger.info("wallet info , balance : {}, userId : {}, transAmount : {}", new Object[]{wallet.getBalance().doubleValue(), wallet.getUserId(), amount});
//		if(wallet == null || wallet.getBalance().compareTo(amount) == -1){
//			logger.warn("Current balance is insufficient");
//			map.put("retCode", RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
//			map.put("msg", "Current balance is insufficient");
//			return map;
//		}
		
		//每次支付金额限制
		BigDecimal transferLimitPerPay =  BigDecimal.valueOf(configManager.
				getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 100000d));
		
		if(amount.compareTo(transferLimitPerPay) == 1){
			logger.warn("Exceeds the maximum amount of each transaction");
			map.put("retCode", RetCodeConsts.TRANSFER_LIMIT_PER_PAY);
			map.put("msg", "Exceeds the maximum amount of each transaction");
			return map;
		}

		//每天累计金额限制
		BigDecimal transferLimitDailyPay =  BigDecimal.valueOf(configManager.
				getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITDAILYPAY, 100000d));
		BigDecimal accumulatedAmount =  transferDAO.getAccumulatedAmount(userId+"");
		
		if((accumulatedAmount.add(amount)).compareTo(transferLimitDailyPay) == 1){
			logger.warn("More than the maximum daily transaction limit");
			map.put("retCode", RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY);
			map.put("msg", "More than the maximum daily transaction limit");
			return map;
		}
		//每天累计给付次数限制
		Double transferLimitNumOfPayPerDay =  configManager.
				getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITNUMBEROFPAYPERDAY, 100000d);
		Integer dayTradubgVolume = transferDAO.getDayTradubgVolume(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		
		if(transferLimitNumOfPayPerDay <= new Double(dayTradubgVolume)){
			logger.warn("Exceeds the maximum number of transactions per day");
			map.put("retCode", RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY);
			map.put("msg", "Exceeds the maximum number of transactions per day");
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
			//判断对方是否有该种货币
			commonManager.checkAndUpdateWallet(receiver.getUserId(), currency);
			
		}else{
			User systemUser = userDAO.getSystemUser();
			transfer.setUserTo(systemUser.getUserId());
			transfer.setTransferType(ServerConsts.TRANSFER_TYPE_OUT_INVITE);
		}
		transfer.setNoticeId(noticeId);
		//保存
		transferDAO.addTransfer(transfer);
		
		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("transferId", transferId);
		
		return map;
	}

	@Override
	public String payPwdConfirm(int userId, String transferId, String userPayPwd) {
		
		User user = userDAO.getUser(userId);
		if(user ==null || user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
			logger.warn("The user does not exist or the account is blocked");
			return RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED;
		}
		if(!PasswordUtils.check(userPayPwd, user.getUserPayPwd(), user.getPasswordSalt())){
			return RetCodeConsts.TRANSFER_PAYMENTPWD_INCORRECT;
		}
		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if(transfer == null){
			logger.warn("The transaction order does not exist");
			return RetCodeConsts.TRANSFER_TRANS_ORDERID_NOT_EXIST;
		}
		
		//总账大于设置安全基数，弹出需要短信验证框===============================================
		BigDecimal totalBalance =  exchangeRateManager.getTotalBalance(userId);
		BigDecimal totalBalanceMax =  BigDecimal.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TOTALBALANCETHRESHOLD, 100000d));
		//当天累计转出总金额大于设置安全基数，弹出需要短信验证框
		BigDecimal accumulatedAmount =  transferDAO.getAccumulatedAmount(userId+"");
		BigDecimal accumulatedAmountMax =  BigDecimal.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.DAILYTRANSFERTHRESHOLD, 100000d));
		//单笔转出金额大于设置安全基数，弹出需要短信验证框
		BigDecimal singleTransferAmount = exchangeRateManager.getExchangeResult(transfer.getCurrency(), transfer.getTransferAmount());
		BigDecimal singleTransferAmountMax = BigDecimal.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.EACHTRANSFERTHRESHOLD, 100000d));

		logger.info("totalBalance : {},totalBalanceMax: {} ",totalBalance,totalBalanceMax);
		logger.info("accumulatedAmount : {},accumulatedAmountMax: {} ",accumulatedAmount,accumulatedAmountMax);
		logger.info("singleTransferAmount : {},singleTransferAmountMax: {} ",singleTransferAmount,singleTransferAmountMax);
		
		if(totalBalance.compareTo(totalBalanceMax) == 1 || 
				( accumulatedAmount.compareTo(accumulatedAmountMax) == 1 || 
						singleTransferAmount.compareTo(singleTransferAmountMax) == 1)){
			logger.warn("The transaction amount exceeds the limit");
			return RetCodeConsts.TRANSFER_REQUIRES_PHONE_VERIFICATION;
			
		}else{
			return transferConfirm(userId,transferId);
		}
	}

	@Override
	public String transferConfirm(int userId,String transferId) {
		
		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if(transfer == null){
			logger.warn("The transaction order does not exist");
			return RetCodeConsts.TRANSFER_TRANS_ORDERID_NOT_EXIST;
		}
		
		User payer = userDAO.getUser(userId);
		if(payer ==null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
			logger.warn("The user does not exist or the account is blocked");
			return RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED;
		}
		
		//获取系统账号
		User systemUser = userDAO.getSystemUser();
		
		if(transfer.getUserTo() == systemUser.getUserId()){  	//交易对象没有注册账号
			//扣款
			Integer updateCount = walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "-");
			
			if(updateCount == 0){
				return RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT;
			}
			
			//加款
			walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "+");
			
			//添加gift记录
			Unregistered unregistered = unregisteredDAO.getUnregisteredByTransId(transfer.getTransferId());
			
			if(unregistered == null){
				unregistered = new Unregistered();
				unregistered.setCreateTime(new Date());
				unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_PENDING);
				unregistered.setTransferId(transfer.getTransferId());
				unregistered.setAreaCode(transfer.getAreaCode());
				unregistered.setUserPhone(transfer.getPhone());
				unregistered.setCurrency(transfer.getCurrency());
				unregistered.setAmount(transfer.getTransferAmount());
				unregisteredDAO.addUnregistered(unregistered);
			}

			//增加seq记录
			walletSeqDAO.addWalletSeq4Transaction(transfer.getUserFrom(), systemUser.getUserId(), 
					ServerConsts.TRANSFER_TYPE_OUT_INVITE, transfer.getTransferId(), 
					transfer.getCurrency(), transfer.getTransferAmount());
			
			//更改Transfer状态
			transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
			
			//向未注册用户发送短信
			smsManager.sendSMS4Transfer(transfer.getAreaCode(), transfer.getPhone(), payer,
					transfer.getCurrency(), 
					amountFormatByCurrency(transfer.getCurrency(),transfer.getTransferAmount()));
			
		
		}else{	//交易对象注册账号,交易正常进行，无需经过系统账户							
			
			//扣款
			Integer updateCount = walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "-");
			
			if(updateCount == 0){
				return RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT;
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
			pushManager.push4Transfer(payer, payee, transfer.getCurrency(), 
					amountFormatByCurrency(transfer.getCurrency(),transfer.getTransferAmount()));
		}
		//更改Transfer状态
		transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);

		//转换金额
		BigDecimal exchangeResult = exchangeRateManager.getExchangeResult(transfer.getCurrency(),transfer.getTransferAmount());
		transferDAO.updateAccumulatedAmount(transfer.getUserFrom()+"", exchangeResult.setScale(2, BigDecimal.ROUND_FLOOR));
		
		return RetCodeConsts.RET_CODE_SUCCESS;
	}
	

	@Override
	public void systemRefund(Unregistered unregistered) {
		
		Transfer transfer = transferDAO.getTransferById(unregistered.getTransferId());
		if(transfer == null || transfer.getTransferStatus() != ServerConsts.TRANSFER_STATUS_OF_COMPLETED){
			logger.warn("Did not find the corresponding transfer information");
			return ;
		}
		User systemUser = userDAO.getSystemUser();
		//系统扣款
		walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), 
				transfer.getCurrency(), transfer.getTransferAmount(), "-");
		//用户加款
		walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
				transfer.getCurrency(), transfer.getTransferAmount(), "+");

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
		//添加Seq记录
		walletSeqDAO.addWalletSeq4Transaction(systemUser.getUserId(), transfer.getUserFrom(), 
				ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND,transferId2 , 
				transfer.getCurrency(), transfer.getTransferAmount());
		//修改gift记录
		unregistered.setUnregisteredStatus(ServerConsts.UNREGISTERED_STATUS_OF_BACK);
		unregistered.setRefundTransId(transferId2);
		unregisteredDAO.updateUnregistered(unregistered);
		
		//发送推送
		User payee = userDAO.getUser(transfer.getUserFrom());
		
		pushManager.push4Refund(payee, transfer.getAreaCode(),transfer.getPhone(),
				transfer.getCurrency(), amountFormatByCurrency(transfer.getCurrency(),transfer.getTransferAmount()));
		
	}
	
	@Override
	public void systemRefundBatch() {
		
		//获取所有未完成的订单
		List<Unregistered> list = unregisteredDAO.getAllUnfinishedTransaction();
		if(list.isEmpty()){
			return;
		}
		for (Unregistered unregistered : list) {
			//判断是否超过期限
			long deadline = configManager.getConfigLongValue(ConfigKeyEnum.REFUNTIME, 3l)*24*60*60*1000;
			if(new Date().getTime() - unregistered.getCreateTime().getTime() >= deadline){
				
				logger.info("Invitation ID: {}, The invitee has not registered for the due "
						+ "date and the system is being refunded",unregistered.getUnregisteredId());
				
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
			pushManager.push4TransferRuquest( payer,payee, currency, amountFormatByCurrency(currency,amount));
			return RetCodeConsts.RET_CODE_SUCCESS;
			
		}
		return RetCodeConsts.RET_CODE_FAILUE;
	}


	
	@Override
	public HashMap<String, Object> getTransactionRecordByPage(String period, 
			int userId,int currentPage, int pageSize) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "SELECT "+
				"t1.user_from,"
				+ "t1.currency,"
				+ "t1.transfer_amount,"
				+ "CONCAT(t1.area_code,t1.phone),"
				+ "CONCAT(t2.area_code,t2.user_phone),"
				+ "t1.transfer_comment,"
				+ "t1.finish_time,"
				+ "t1.transfer_type ";
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
		//读交易标记
		commonManager.readMsgFlag(userId, 1);
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
		//读请求标记
		commonManager.readMsgFlag(userId, 0);
		return map;
	}
	
	@SuppressWarnings("serial")
	@Override
	public HashMap<String, String> respond2Request(final int userId,final String areaCode,final String userPhone, 
			final String currency,final BigDecimal amount, String transferComment,int noticeId){

		HashMap<String, Object> args = new HashMap<>();		
		final TransactionNotification notification = notificationDAO.getNotificationById(noticeId);
		args.put("verifyNotificationStatus", new HashMap<String,Object>(){{
			put("notification", notification);
			put("userId", userId);
			put("currency", currency);
			put("amount", amount);
		}});
		args.put("isTradableCurrency", currency);
		args.put("isAccountFrozened", userId);
		args.put("verifyRecevierStatus",new HashMap<String,Object>(){{
			put("sponsorId", notification.getSponsorId());
			put("userId", userId);
			put("areaCode", areaCode);
			put("userPhone", userPhone);
		}} );
		args.put("isInsufficientBalance",new HashMap<String,Object>(){{
			put("userId", userId);
			put("currency", currency);
			put("amount", amount);
		}} );
		
		HashMap<String, String> map = test(args);
		
		if(!map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)){
			return map;
		}
		
//		User receiver = userDAO.getUser(notification.getSponsorId());
//		//不用给自己转账
//		if(receiver!= null && userId == receiver.getUserId()){
//			logger.warn("Prohibit transfers to yourself");
//			map.put("retCode", RetCodeConsts.TRANSFER_PROHIBIT_TRANSFERS_TO_YOURSELF);
//			map.put("msg", "Prohibit transfers to yourself");
//			return map;
//		}
//		if(!receiver.getAreaCode().equals(areaCode) || !receiver.getUserPhone().equals(userPhone)){
//			logger.warn("Payee phone information does not match");
//			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
//			map.put("msg", "Payee phone information does not match");
//			return map;
//		}
//		
//		//判断余额是否足够支付
//		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
//		if(wallet == null || wallet.getBalance().compareTo(amount) == -1){
//			logger.warn("Current balance is insufficient");
//			map.put("retCode", RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
//			map.put("msg", "Current balance is insufficient");
//			return map;
//		}
		
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
		transfer.setUserTo(notification.getSponsorId());
		transfer.setAreaCode(areaCode);
		transfer.setPhone(userPhone);
		transfer.setTransferType(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		transfer.setNoticeId(noticeId);
		//保存
		transferDAO.addTransfer(transfer);
		
		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("transferId", transferId);
		
		return map;
		
	}
	
	private BigDecimal amountFormatByCurrency(String currency,BigDecimal amoumt){
		DecimalFormat df1;
		if(StringUtils.isNotBlank(currency) && currency.equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
			df1 = new DecimalFormat("0");  
		}else{
			df1 = new DecimalFormat("0.00");  
		}
		
		return new BigDecimal(df1.format(amoumt));  
	}
	
	public HashMap<String, String> test(HashMap<String, Object> map){
		
		HashMap<String, String> result = new HashMap<>();
		
		for (Entry<String, Object> entry : map.entrySet()) {
			
			switch (entry.getKey()) {
				//检验传入Currency是否可用
				case "isTradableCurrency":
					if(!commonManager.verifyCurrency((String) entry.getValue())){
						logger.warn("This currency is not a tradable currency");
						result.put("retCode", RetCodeConsts.TRANSFER_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
						result.put("msg", "This currency is not a tradable currency");
						return result;
					}
					break;
				//检验当前用的账户是否被冻结
				case "isAccountFrozened":
					User payer = userDAO.getUser((Integer) entry.getValue());
					if(payer ==null || payer.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
						logger.warn("The user does not exist or the account is blocked");
						result.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
						result.put("msg", "The user does not exist or the account is blocked");
						return result;
					}
					break;
				//检验Notification的状态
				case "verifyNotificationStatus":
					@SuppressWarnings("unchecked")
					HashMap<String, Object> args = (HashMap<String, Object>) entry.getValue();
					
					TransactionNotification notification = (TransactionNotification) args.get("notification");
					int userId = (int) args.get("userId");
					String currency = (String) args.get("currency");
					BigDecimal amount = (BigDecimal) args.get("amount");
					
					if(notification == null){
						logger.warn("Can not find the corresponding notification information");
						result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
						result.put("msg", "Can not find the corresponding notification information");
						return result;
					}
					if(notification.getPayerId()!=userId || notification.getTradingStatus() 
							== ServerConsts.NOTIFICATION_STATUS_OF_ALREADY_PAID){
						logger.warn("Order status exception");
						result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
						result.put("msg", "Order status exception");
						return result;
					}
					if(StringUtils.isBlank(currency) || amount.compareTo(new BigDecimal("0"))==0){
						logger.warn("The requestor currency and amount information is blank");
						result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
						result.put("msg", "The requestor currency and amount information is blank");
						return result;
					}
					if((StringUtils.isNotBlank(notification.getCurrency())
							&& notification.getAmount().compareTo(new BigDecimal("0"))!=0) 
							&& (!notification.getCurrency().equals(currency) || notification.getAmount().compareTo(amount) != 0)){
						logger.warn("The input and order information do not match");
						result.put("retCode", RetCodeConsts.TRANSFER_REQUEST_INFORMATION_NOT_MATCH);
						result.put("msg", "The input and order information do not match");
						return result;
					}
					break;
				//检验接收人的状态	
				case "verifyRecevierStatus":
					@SuppressWarnings("unchecked")
					HashMap<String, Object>  recevierArgs = (HashMap<String, Object>) entry.getValue();
					
					int sponsorId = (int) recevierArgs.get("sponsorId");
					userId = (int) recevierArgs.get("userId");
					String areaCode = (String) recevierArgs.get("areaCode");
					String userPhone = (String) recevierArgs.get("userId");
					
					User receiver = userDAO.getUser(sponsorId);
					//不用给自己转账
					if(receiver!= null && userId == receiver.getUserId()){
						logger.warn("Prohibit transfers to yourself");
						result.put("retCode", RetCodeConsts.TRANSFER_PROHIBIT_TRANSFERS_TO_YOURSELF);
						result.put("msg", "Prohibit transfers to yourself");
						return result;
					}
					if(!receiver.getAreaCode().equals(areaCode) || !receiver.getUserPhone().equals(userPhone)){
						logger.warn("Payee phone information does not match");
						result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
						result.put("msg", "Payee phone information does not match");
						return result;
					}
					break;
				//判断余额是否足够支付
				case "isInsufficientBalance":
					@SuppressWarnings("unchecked")
					HashMap<String, Object>  insufficientBalanceArgs = (HashMap<String, Object>) entry.getValue();
					userId = (int) insufficientBalanceArgs.get("userId");
					currency = (String) insufficientBalanceArgs.get("currency");
					amount = (BigDecimal) insufficientBalanceArgs.get("amount");
					Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
					if(wallet == null || wallet.getBalance().compareTo(amount) == -1){
						logger.warn("Current balance is insufficient");
						result.put("retCode", RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT);
						result.put("msg", "Current balance is insufficient");
						return result;
					}
					break;
					
				default:
					break;
			}
			
		}
		result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		return result;
		
	}
}

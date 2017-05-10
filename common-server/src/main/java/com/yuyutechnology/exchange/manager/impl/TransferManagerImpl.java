package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.FriendDAO;
import com.yuyutechnology.exchange.dao.NotificationDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.dto.CheckPwdResult;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Friend;
import com.yuyutechnology.exchange.pojo.TransactionNotification;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.push.PushManager;
import com.yuyutechnology.exchange.sms.SmsManager;
import com.yuyutechnology.exchange.util.DateFormatUtils;

@Service
public class TransferManagerImpl implements TransferManager{
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	RedisDAO redisDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	FriendDAO friendDAO;
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	CurrencyDAO currencyDAO;
	@Autowired
	WalletSeqDAO walletSeqDAO;
	@Autowired
	UnregisteredDAO unregisteredDAO;
	@Autowired
	NotificationDAO notificationDAO;
	@Autowired
	CrmAlarmDAO crmAlarmDAO;
	@Autowired
	OandaRatesManager oandaRatesManager;
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
	@Autowired
	CrmAlarmManager crmAlarmManager;
	
	public static Logger logger = LogManager.getLogger(TransferManagerImpl.class);

	@SuppressWarnings("serial")
	@Override
	public HashMap<String, String> transferInitiate(final int userId,String areaCode,String userPhone, final String currency, 
			final BigDecimal amount, String transferComment,int noticeId) {

		//干扰条件过滤
		LinkedHashMap<String, Object> args = new LinkedHashMap<>();		
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
		
		map = checkTransferLimit(currency, amount, userId);
		if (!map.isEmpty()) {
			return map;
		}
		
		User receiver = userDAO.getUserByUserPhone(areaCode, userPhone);
		//不用给自己转账
		if(receiver!= null && userId == receiver.getUserId()){
			logger.warn("Prohibit transfers to yourself");
			map.put("retCode", RetCodeConsts.TRANSFER_PROHIBIT_TRANSFERS_TO_YOURSELF);
			map.put("msg", "Prohibit transfers to yourself");
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
	public HashMap<String, String> payPwdConfirm(int userId, String transferId, String userPayPwd) {
		
		HashMap<String, String> result = new HashMap<>();		
		User user = userDAO.getUser(userId);
		if(user ==null || user.getUserAvailable() == ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE){
			logger.warn("The user does not exist or the account is blocked");
			result.put("msg", "The user does not exist or the account is blocked");
			result.put("retCode", RetCodeConsts.TRANSFER_USER_DOES_NOT_EXIST_OR_THE_ACCOUNT_IS_BLOCKED);
			return result;
		}

		//验证支付密码
		CheckPwdResult checkPwdResult = userManager.checkPayPassword(userId, userPayPwd);
		switch (checkPwdResult.getStatus()) {
			case ServerConsts.CHECKPWD_STATUS_FREEZE:
				result.put("msg", String.valueOf(checkPwdResult.getInfo()));
				result.put("retCode", RetCodeConsts.PAY_FREEZE);
				return result;

			case ServerConsts.CHECKPWD_STATUS_INCORRECT:
				logger.warn("payPwd is wrong !");
				result.put("msg", String.valueOf(checkPwdResult.getInfo()));
				result.put("retCode", RetCodeConsts.PAY_PWD_NOT_MATCH);
				return result;
				
			default :
				break;
		}
				
		Transfer transfer = transferDAO.getTranByIdAndStatus(transferId,ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		if(transfer == null){
			logger.warn("The transaction order does not exist");
			result.put("msg", "The transaction order does not exist");
			result.put("retCode", RetCodeConsts.TRANSFER_TRANS_ORDERID_NOT_EXIST);
			return result;
		}
		
		//总账大于设置安全基数，弹出需要短信验证框===============================================
		BigDecimal totalBalance =  oandaRatesManager.getTotalBalance(userId);
		BigDecimal totalBalanceMax =  BigDecimal.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TOTALBALANCETHRESHOLD, 100000d));
		//当天累计转出总金额大于设置安全基数，弹出需要短信验证框
		BigDecimal accumulatedAmount =  transferDAO.getAccumulatedAmount("transfer_"+userId);
		BigDecimal accumulatedAmountMax =  BigDecimal.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.DAILYTRANSFERTHRESHOLD, 100000d));
		//单笔转出金额大于设置安全基数，弹出需要短信验证框
		BigDecimal singleTransferAmount = oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(), transfer.getTransferAmount());
		BigDecimal singleTransferAmountMax = BigDecimal.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.EACHTRANSFERTHRESHOLD, 100000d));

		logger.info("totalBalance : {},totalBalanceMax: {} ",totalBalance,totalBalanceMax);
		logger.info("accumulatedAmount : {},accumulatedAmountMax: {} ",accumulatedAmount,accumulatedAmountMax);
		logger.info("singleTransferAmount : {},singleTransferAmountMax: {} ",singleTransferAmount,singleTransferAmountMax);
		
		if(totalBalance.compareTo(totalBalanceMax) == 1 || 
				( accumulatedAmount.compareTo(accumulatedAmountMax) == 1 || 
						singleTransferAmount.compareTo(singleTransferAmountMax) == 1)){
			logger.warn("The transaction amount exceeds the limit");
			result.put("msg", "The transaction amount exceeds the limit");
			result.put("retCode", RetCodeConsts.TRANSFER_REQUIRES_PHONE_VERIFICATION);
			return result;
		}else{
			String retCode = transferConfirm(userId,transferId);
			result.put("msg", retCode);
			result.put("retCode", retCode);
			return result;
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
		
		if(userId != transfer.getUserFrom()){
			logger.warn("userId is different from UserFromId");
			return RetCodeConsts.RET_CODE_FAILUE; 
		}
		
		
		//每次支付金额限制
		BigDecimal transferLimitPerPay =  BigDecimal.valueOf(configManager.
				getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 100000d));
		logger.warn("transferLimitPerPay : {}",transferLimitPerPay);
		if((oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(), transfer.getTransferAmount())).compareTo(transferLimitPerPay) == 1){
			logger.warn("Exceeds the maximum amount of each transaction");
			return RetCodeConsts.TRANSFER_LIMIT_EACH_TIME;
		}

		//每天累计金额限制
		BigDecimal transferLimitDailyPay =  BigDecimal.valueOf(configManager.
				getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITDAILYPAY, 100000d));
		BigDecimal accumulatedAmount =  transferDAO.getAccumulatedAmount("transfer_"+userId);
		logger.warn("transferLimitDailyPay : {},accumulatedAmount : {} ",transferLimitDailyPay,accumulatedAmount);
		if((accumulatedAmount.add(oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(), transfer.getTransferAmount()))).compareTo(transferLimitDailyPay) == 1){
			logger.warn("More than the maximum daily transaction limit");
			return RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY;
		}
		//每天累计给付次数限制
		Double transferLimitNumOfPayPerDay =  configManager.
				getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITNUMBEROFPAYPERDAY, 100000d);
//				Integer dayTradubgVolume = transferDAO.getDayTradubgVolume(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		Integer dayTradubgVolume = transferDAO.getCumulativeNumofTimes("transfer_"+userId);
		logger.warn("transferLimitNumOfPayPerDay : {},dayTradubgVolume : {} ",transferLimitNumOfPayPerDay,dayTradubgVolume);
		if(transferLimitNumOfPayPerDay <= new Double(dayTradubgVolume)){
			logger.warn("Exceeds the maximum number of transactions per day");
			return RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY;
		}
		
		
		//获取系统账号
		User systemUser = userDAO.getSystemUser();
		
		if(transfer.getUserTo() == systemUser.getUserId()){  	//交易对象没有注册账号
			//扣款
			Integer updateCount = walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "-", ServerConsts.TRANSFER_TYPE_OUT_INVITE, transfer.getTransferId());
			
			if(updateCount == 0){
				return RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT;
			}
			
			//加款
			walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "+", ServerConsts.TRANSFER_TYPE_OUT_INVITE, transfer.getTransferId());
			
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
//			walletSeqDAO.addWalletSeq4Transaction(transfer.getUserFrom(), systemUser.getUserId(), 
//					ServerConsts.TRANSFER_TYPE_OUT_INVITE, transfer.getTransferId(), 
//					transfer.getCurrency(), transfer.getTransferAmount());
			
			//更改Transfer状态
			transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
			
			//向未注册用户发送短信
			smsManager.sendSMS4Transfer(transfer.getAreaCode(), transfer.getPhone(), payer,
					transfer.getCurrency(), 
					amountFormatByCurrency(transfer.getCurrency(),transfer.getTransferAmount()));
			
		
		}else{	//交易对象注册账号,交易正常进行，无需经过系统账户							
			
			//扣款
			Integer updateCount = walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "-", ServerConsts.TRANSFER_TYPE_TRANSACTION, transfer.getTransferId());
			
			if(updateCount == 0){
				return RetCodeConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT;
			}
			
			//加款
			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserTo(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "+", ServerConsts.TRANSFER_TYPE_TRANSACTION, transfer.getTransferId());
			
			//添加seq记录
//			walletSeqDAO.addWalletSeq4Transaction(transfer.getUserFrom(), transfer.getUserTo(), 
//					ServerConsts.TRANSFER_TYPE_TRANSACTION, transfer.getTransferId(), 
//					transfer.getCurrency(), transfer.getTransferAmount());	
			
			//如果是请求转账还需要更改消息通知中的状态
			if(transfer.getNoticeId() != 0){
				TransactionNotification notification =  notificationDAO.getNotificationById(transfer.getNoticeId());
				notification.setTradingStatus(ServerConsts.NOTIFICATION_STATUS_OF_ALREADY_PAID);
				notificationDAO.updateNotification(notification);
			}
			//推送到账通知

			User payee = userDAO.getUser(transfer.getUserTo());
			pushManager.push4Transfer(transfer.getTransferId(),payer, payee, transfer.getCurrency(), 
					amountFormatByCurrency(transfer.getCurrency(),transfer.getTransferAmount()));
		}
		//更改Transfer状态
		transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);

		//转换金额
		BigDecimal exchangeResult = oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(),transfer.getTransferAmount());
		transferDAO.updateAccumulatedAmount("transfer_"+transfer.getUserFrom(), exchangeResult.setScale(2, BigDecimal.ROUND_FLOOR));
		//更改累计次数
		transferDAO.updateCumulativeNumofTimes("transfer_"+transfer.getUserFrom(), new BigDecimal("1"));
		
		//预警
		largeTransWarn(payer,transfer);

		return RetCodeConsts.RET_CODE_SUCCESS;
	}
	

	@Override
	public void systemRefund(Unregistered unregistered) {
		
		Transfer transfer = transferDAO.getTransferById(unregistered.getTransferId());
		if(transfer == null || transfer.getTransferStatus() != ServerConsts.TRANSFER_STATUS_OF_COMPLETED){
			logger.warn("Did not find the corresponding transfer information");
			return ;
		}
		String transferId2 = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_TRANSACTION);
		User systemUser = userDAO.getSystemUser();
		//系统扣款
		walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), 
				transfer.getCurrency(), transfer.getTransferAmount(), "-", ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND,transferId2);
		//用户加款
		walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
				transfer.getCurrency(), transfer.getTransferAmount(), "+", ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND,transferId2);

		///////////////////////////生成transfer系统退款订单////////////////////////////
		Transfer transfer2 = new Transfer();
		//生成TransId
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
//		walletSeqDAO.addWalletSeq4Transaction(systemUser.getUserId(), transfer.getUserFrom(), 
//				ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND,transferId2 , 
//				transfer.getCurrency(), transfer.getTransferAmount());
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
				
				logger.info("deadline : {},Difference : {}",deadline,new Date().getTime() - unregistered.getCreateTime().getTime());
				
				logger.info("Invitation ID: {}, The invitee has not registered for the due "
						+ "date and the system is being refunded ,{}",unregistered.getUnregisteredId(),new SimpleDateFormat("HH:mm:ss").format(new Date()));
				
				systemRefund(unregistered);
			} 
		}
	}
	

	@Override
	public HashMap<String,Object> makeRequest(int userId, String payerAreaCode, String payerPhone, String currency, BigDecimal amount) {
		
		HashMap<String,Object> result = new HashMap<>();
		
		BigDecimal transferLimitPerPay =  BigDecimal.valueOf(configManager.
				getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 100000d));
		
		if(StringUtils.isNotBlank(currency)&&StringUtils.isNotBlank(amount.toString())){
			if((oandaRatesManager.getDefaultCurrencyAmount(currency, amount)).compareTo(transferLimitPerPay) == 1){
				
				Currency unit = commonManager.getCurreny(ServerConsts.CURRENCY_OF_USD);
				
				logger.warn("Exceeds the maximum amount of each transaction");
				result.put("retCode", RetCodeConsts.TRANSFER_LIMIT_EACH_TIME);
				result.put("msg", "Exceeds the maximum amount of each transaction");
				result.put("transferLimitPerPay",transferLimitPerPay.toString());
				result.put("unit",unit.getCurrencyUnit());
				return result;
			}
		}
		
		//请求对方不是己方好友
		
		//Currency不在激活列表
		if(StringUtils.isNotBlank(currency) && !commonManager.verifyCurrency(currency)){
			logger.warn("This currency is not a tradable currency");
			result.put("retCode", RetCodeConsts.TRANSFER_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			result.put("msg", "This currency is not a tradable currency");
			return result;
		}

		//Goldpay不是整数
		if((StringUtils.isNotBlank(currency) && currency.equals(ServerConsts.CURRENCY_OF_GOLDPAY)) && (amount.doubleValue()%1 > 0 )){
			logger.warn("The GDQ must be an integer value");
			result.put("retCode", RetCodeConsts.TRANSFER_LESS_THAN_MINIMUM_AMOUNT);
			result.put("msg", "The GDQ must be an integer value");
			return result;
		}
		
		User payer = userDAO.getUserByUserPhone(payerAreaCode, payerPhone);
		if(payer != null){
			
			if(userId == payer.getUserId()){
				logger.info("bu neng gei zi ji fa qi qingqiu");
				result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
				result.put("msg", "Sharing failed");
				return result;
			}
			
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
			result.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
			result.put("msg", MessageConsts.RET_CODE_SUCCESS);
			return result;
			
		}
		result.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
		result.put("msg", MessageConsts.RET_CODE_FAILUE);
		return result;
	}


	
	@Override
	public HashMap<String, Object> getTransactionRecordByPage(String period,String type, 
			int userId,int currentPage, int pageSize) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "SELECT "+
				"t1.user_from,"
				+ "t1.currency,"
				+ "t1.transfer_amount,"
				+ "CONCAT(t1.area_code,' ',t1.phone),"
				+ "CONCAT(t2.area_code,' ',t2.user_phone),"
				+ "t1.transfer_comment,"
				+ "t1.finish_time,"
				+ "t1.transfer_type,t1.transfer_id  ";
		StringBuilder sb = new StringBuilder(
				"FROM `e_transfer` t1 "+
				"LEFT JOIN `e_user` t2  "+
				"ON  "+
				"t1.user_from = t2.user_id  "+
				"LEFT JOIN `e_wallet_seq` t3  "+
				"ON  "+
				"t1.transfer_id = t3.transaction_id  "+
				"where " +
				"t1.transfer_status=? "+
				"and (t1.user_from = ? or t1.user_to = ?) "+ 
				"and t3.user_id = ?  ");
		
		List<Object> values = new ArrayList<Object>();
		values.add(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		values.add(userId);
		values.add(userId);
		values.add(userId);
		
		if(StringUtils.isNotBlank(type)){
			
			switch(type){
				case "expenses"://支出
					sb.append("and t3.amount < 0 and t1.transfer_type in (0,?) ");
					values.add(ServerConsts.TRANSFER_TYPE_OUT_INVITE+"");
					break;
				case "income"://收入
					sb.append("and t3.amount > 0 and t1.transfer_type in (0,?) ");
					values.add(ServerConsts.TRANSFER_TYPE_IN_SYSTEM_REFUND+"");
					break;
				case "withdraw"://体现
					sb.append("and t1.transfer_type = ? ");
					values.add(ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW+"");
					break;
				case "recharge"://充值
					sb.append("and t1.transfer_type = ? ");
					values.add(ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE+"");
					break;
				default:
					break;	
				
			}

		}
		
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

		LinkedHashMap<String, Object> args = new LinkedHashMap<>();		
		HashMap<String, String> map  = new HashMap<>();
		final TransactionNotification notification = notificationDAO.getNotificationById(noticeId);
		if(notification == null){
			logger.warn("Can not find the corresponding notification information");
			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			map.put("msg", "Can not find the corresponding notification information");
			return map;
		}
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
		
		map = test(args);
		
		if(!map.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)){
			return map;
		}
		
		map = checkTransferLimit(currency, amount, userId);
		if (!map.isEmpty()) {
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
	
	private HashMap<String, String> test(Map<String, Object> map){
		
		HashMap<String, String> result = new HashMap<>();
		
		for (Entry<String, Object> entry : map.entrySet()) {
			
			switch (entry.getKey()) {
				//检验传入Currency是否可用
				case "isTradableCurrency":
					logger.info("Currency : {}", (String) entry.getValue());
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
					String userPhone = (String) recevierArgs.get("userPhone");
					
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
	
	@SuppressWarnings("serial")
//	@Async
	private void largeTransWarn(final User payer,final Transfer transfer){
		BigDecimal transferLimitPerPay =  BigDecimal.valueOf(configManager.
				getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 100000d));
		BigDecimal percentage = (oandaRatesManager.getDefaultCurrencyAmount(transfer.getCurrency(), transfer.getTransferAmount()))
				.divide(transferLimitPerPay,5,RoundingMode.DOWN).multiply(new BigDecimal("100"));
		
		logger.info("percentage : {}",percentage);
		
		List<CrmAlarm> list = crmAlarmDAO.getConfigListByTypeAndStatus(1, 1);
		
		if(list!= null && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				CrmAlarm crmAlarm = list.get(i);
				
				if((crmAlarm.getLowerLimit().compareTo(percentage)==0 || 
						crmAlarm.getLowerLimit().compareTo(percentage)==-1) && 
						crmAlarm.getUpperLimit().compareTo(percentage) == 1){
					
					crmAlarmManager.alarmNotice(crmAlarm.getSupervisorIdArr(),"largeTransactionWarning",
							crmAlarm.getAlarmMode(),new HashMap<String,Object>(){
						{
							put("payerMobile", payer.getAreaCode()+payer.getUserPhone());
							put("payeeMobile", transfer.getAreaCode()+transfer.getPhone());
							put("amount", transfer.getTransferAmount().toString());
							put("currency", transfer.getCurrency());
						}
					});
					
					
				}
				
			}
		}
	}

	@Override
	public HashMap<String, String> regenerateQRCode(String currency,BigDecimal amount) {
		
		HashMap<String, String> map = new HashMap<>();
		
		BigDecimal transferLimitPerPay =  BigDecimal.valueOf(configManager.
				getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 100000d));
		
		Currency unit = commonManager.getCurreny("USD");
		
		if(unit == null){
			map.put("retCode", RetCodeConsts.RET_CODE_FAILUE);
			map.put("msg", "fail");
			return map;
		}

		logger.warn("transferLimitPerPay : {}",transferLimitPerPay);
		if((oandaRatesManager.getDefaultCurrencyAmount(currency, amount)).compareTo(transferLimitPerPay) == 1){
			logger.warn("Exceeds the maximum amount of each transaction");
			map.put("retCode", RetCodeConsts.TRANSFER_LIMIT_EACH_TIME);
			map.put("msg", transferLimitPerPay.setScale(2).toString());
			map.put("unit", unit.getCurrencyUnit());
			return map;
		}
		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "OK");
		return map;
	}

	@Override
	public Object getTransfer(String transferId) {
		return transferDAO.getTransferByIdJoinUser(transferId);
	}
	
	private HashMap<String, String> checkTransferLimit(String currency, BigDecimal amount, int userId){
		HashMap<String, String> map = new HashMap<>();
		Currency unit = commonManager.getCurreny("USD");
		//每次支付金额限制
		BigDecimal transferLimitPerPay =  BigDecimal.valueOf(configManager.
				getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 100000d));
		logger.warn("transferLimitPerPay : {}",transferLimitPerPay);
		if((oandaRatesManager.getDefaultCurrencyAmount(currency, amount)).compareTo(transferLimitPerPay) == 1){
			logger.warn("Exceeds the maximum amount of each transaction");
			map.put("retCode", RetCodeConsts.TRANSFER_LIMIT_EACH_TIME);
			map.put("msg", transferLimitPerPay.setScale(2).toString());
			map.put("unit", unit.getCurrencyUnit());
			return map;
		}

		//每天累计金额限制
		BigDecimal transferLimitDailyPay =  BigDecimal.valueOf(configManager.
				getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITDAILYPAY, 100000d));
		BigDecimal accumulatedAmount =  transferDAO.getAccumulatedAmount("transfer_"+userId);
		logger.warn("transferLimitDailyPay : {},accumulatedAmount : {} ",transferLimitDailyPay,accumulatedAmount);
		if((accumulatedAmount.add(oandaRatesManager.getDefaultCurrencyAmount(currency, amount))).compareTo(transferLimitDailyPay) == 1){
			logger.warn("More than the maximum daily transaction limit");
			map.put("retCode", RetCodeConsts.TRANSFER_LIMIT_DAILY_PAY);
			map.put("msg", transferLimitDailyPay.setScale(2).toString());
			map.put("thawTime",DateFormatUtils.getIntervalDay(new Date(),1).getTime()+"");
			map.put("unit", unit.getCurrencyUnit());
			return map;
		}
		//每天累计给付次数限制
		Double transferLimitNumOfPayPerDay =  configManager.
				getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITNUMBEROFPAYPERDAY, 100000d);
		Integer dayTradubgVolume = transferDAO.getCumulativeNumofTimes("transfer_"+userId);
		logger.warn("transferLimitNumOfPayPerDay : {},dayTradubgVolume : {} ",transferLimitNumOfPayPerDay,dayTradubgVolume);
		if(transferLimitNumOfPayPerDay <= new Double(dayTradubgVolume)){
			logger.warn("Exceeds the maximum number of transactions per day");
			map.put("retCode", RetCodeConsts.TRANSFER_LIMIT_NUM_OF_PAY_PER_DAY);
			map.put("msg", (transferLimitNumOfPayPerDay).intValue()+"");
			map.put("thawTime",DateFormatUtils.getIntervalDay(new Date(),1).getTime()+"");
			return map;
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getTransDetails(String transferId, int userId) {
		
		HashMap<String, Object> map = new HashMap<>();
		
		User user;
		Integer friendId;
		
		Transfer transfer = transferDAO.getTransferById(transferId);
		User systemUser = userDAO.getSystemUser();
		
		if(transfer.getUserFrom()!= userId && transfer.getUserTo() != userId){
			return map;
		}
		
		//amount正负号标识
		map.put("isPlus",true);
		
		if(transfer.getUserFrom() == userId){
			if(transfer.getUserTo() == systemUser.getUserId()){
				map.put("areaCode", transfer.getAreaCode());
				map.put("phone", transfer.getPhone());	
				
				//如果当时交易对象中有System，查看交易未注册一方此时此时是否已经注册
				User trander = userDAO.getUserByUserPhone(transfer.getAreaCode(), transfer.getPhone());
				if(trander != null){
					friendId = trander.getUserId();
				}else {
					friendId = -1;
				}
				
				user = null;
			}else{
				user = userDAO.getUser(transfer.getUserTo()); 
				friendId = transfer.getUserTo();
				map.put("areaCode", transfer.getAreaCode());
				map.put("phone", transfer.getPhone());
			}
			map.put("isPlus",false);
		}else if(transfer.getUserFrom() == systemUser.getUserId()){
			map.put("areaCode", transfer.getAreaCode());
			map.put("phone", transfer.getPhone());	
			//如果当时交易对象中有System，查看交易未注册一方此时此时是否已经注册
			User trander = userDAO.getUserByUserPhone(transfer.getAreaCode(), transfer.getPhone());
			if(trander != null){
				friendId = trander.getUserId();
			}else {
				friendId = -1;
			}
			user = null;
		}else{
			user = userDAO.getUser(transfer.getUserFrom());
			friendId = transfer.getUserFrom();
			map.put("areaCode", user.getAreaCode());
			map.put("phone", user.getUserPhone());
		}
		
		//判断是否已经是好友
		Friend friend = friendDAO.getFriendByUserIdAndFrindId(userId, friendId);
		if(friend == null){
			map.put("isFriend", false);
		}else{
			map.put("isFriend", true);
		}
		
		//获取单位
		Currency currency = currencyDAO.getCurrency(transfer.getCurrency());
		
		map.put("user", user);
		map.put("transfer", transfer);
		map.put("unit", currency.getCurrencyUnit());
		
		return map;

	}
}

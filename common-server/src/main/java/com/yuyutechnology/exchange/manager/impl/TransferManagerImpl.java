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
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.manager.TransferManager;
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
	PushManager pushManager;
	@Autowired
	SmsManager smsManager;
	
	public static Logger logger = LoggerFactory.getLogger(TransferManagerImpl.class);

	@Override
	public String transferInitiate(int userId,String areaCode,String userPhone, String currency, 
			BigDecimal amount, String transferComment,int noticeId) {
		
		User receiver = userDAO.getUserByUserPhone(areaCode, userPhone);
		
		if(receiver!= null && userId == receiver.getUserId()){
			logger.warn("Prohibit transfers to yourself");
			return ServerConsts.TRANSFER_PROHIBIT_TRANSFERS_TO_YOURSELF;
		}

		//判断余额是否足够支付
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
		if(wallet == null || wallet.getBalance().compareTo(amount) == -1){
			logger.warn("Current balance is insufficient");
			return ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT;
		}
//		//当日累加金额
//		BigDecimal accumulatedAmount =  transferDAO.getAccumulatedAmount(userId+"");
//		//当日最大金额===================================================================
//		BigDecimal dayMaxAmount =  new BigDecimal(20000);
//		//判断是否超过当日累加金额
//		if(accumulatedAmount.add(amount).compareTo(dayMaxAmount) == 1){
//			logger.warn("Exceeded the day's transaction limit");
//			return ServerConsts.TRANSFER_EXCEEDED_TRANSACTION_LIMIT;
//		}
		
		//生成TransId
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_OF_TRANSACTION);
		
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
			transfer.setTransferType(ServerConsts.TRANSFER_TYPE_OF_TRANSACTION);
		}else{
			transfer.setUserTo(0);
			transfer.setTransferType(ServerConsts.TRANSFER_TYPE_OF_GIFT_OUT);
		}
		transfer.setNoticeId(noticeId);
		//保存
		transferDAO.addTransfer(transfer);
		
		return transferId;
	}

	@Override
	public String payPwdConfirm(int userId, String transferId, String userPayPwd) {
		
		User user = userDAO.getUser(userId);
		Transfer transfer = transferDAO.getTransferById(transferId);
		
		if(!PasswordUtils.check(userPayPwd, user.getUserPayPwd(), user.getPasswordSalt())){
			return ServerConsts.TRANSFER_PAYMENTPWD_INCORRECT;
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
			transferConfirm(transferId);
		}
		
		return ServerConsts.RET_CODE_SUCCESS;
	}

	@Override
	public String transferConfirm(String transferId) {
		Transfer transfer = transferDAO.getTransferById(transferId);
		User payer = userDAO.getUser(transfer.getUserFrom());
		
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
					ServerConsts.TRANSFER_TYPE_OF_GIFT_OUT, transfer.getTransferId(), 
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
					ServerConsts.TRANSFER_TYPE_OF_TRANSACTION, transfer.getTransferId(), 
					transfer.getCurrency(), transfer.getTransferAmount());	
			
			//如果是请求转账还需要更改消息通知中的状态
			if(transfer.getNoticeId() != 0){
				TransactionNotification notification =  notificationDAO.getNotificationById(transfer.getNoticeId());
				notification.setTradingStatus(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
				notificationDAO.updateNotification(notification);
			}
			//推送到账通知

			User payee = userDAO.getUser(transfer.getUserTo());
//			pushManager.push4Transfer(payer, payee, transfer.getCurrency(), transfer.getTransferAmount());
			
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
				ServerConsts.TRANSFER_TYPE_OF_SYSTEM_REFUND, unregistered.getTransferId(), 
				transfer.getCurrency(), transfer.getTransferAmount());
		///////////////////////////生成transfer系统退款订单////////////////////////////
		Transfer transfer2 = new Transfer();
		//生成TransId
		String transferId2 = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_OF_TRANSACTION);
		transfer2.setTransferId(transferId2);
		transfer2.setUserFrom(systemUser.getUserId());
		transfer2.setUserTo(transfer.getUserFrom());
		transfer2.setAreaCode(unregistered.getAreaCode());
		transfer2.setPhone(unregistered.getUserPhone());
		transfer2.setCurrency(transfer.getCurrency());
		transfer2.setTransferAmount(transfer.getTransferAmount());
		transfer2.setTransferComment(unregistered.getUserPhone()+"对方逾期未注册,系统退款");
		transfer2.setTransferType(ServerConsts.TRANSFER_TYPE_OF_SYSTEM_REFUND);
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
//		pushManager.push4Refund(payee, payee.getAreaCode(),transfer.getAreaCode(),
//				transfer.getPhone(), transfer.getTransferAmount());
		
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
			long deadline = 15*24*60*60*1000;
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
			transactionNotification.setTradingStatus(0);
			
			notificationDAO.addNotification(transactionNotification);
			
			//推送请求付款
			User payee = userDAO.getUser(userId);
//			pushManager.push4TransferRuquest(payee, payer, currency, amount);
			
			
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
				"FROM `transfer` t1 LEFT JOIN `user` t2  "+
				"ON  "+
				"t1.user_from = t2.user_id  "+
				"and t1.transfer_status=? "+
				"and (t1.user_from = ? or t1.user_to = ?) ");
		
		List<Object> values = new ArrayList<Object>();
		values.add(ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
		values.add(userId);
		values.add(userId);
		
		if(!period.equals("all")){
			switch (period) {
				case "today":
					sb.append("where t1.finish_time > ?");
					values.add(DateFormatUtils.getStartTime(sdf.format(new Date())));
					break;
					
				case "lastMonth":
					sb.append("where t1.finish_time > ?");
					Date date = DateFormatUtils.getpreDays(-30);
					values.add(DateFormatUtils.getStartTime(sdf.format(date)));
					break;
				case "last3Month":
					sb.append("where t1.finish_time > ?");
					date = DateFormatUtils.getpreDays(-90);
					values.add(DateFormatUtils.getStartTime(sdf.format(date)));		
					break;
				case "lastYear":
					sb.append("where t1.finish_time > ?");
					date = DateFormatUtils.getpreDays(-365);
					values.add(DateFormatUtils.getStartTime(sdf.format(date)));
					break;
				case "aYearAgo":
					sb.append("where t1.finish_time < ?");
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
		String sql = "SELECT t1.notice_id,t2.area_code,t2.user_phone,t1.currency,t1.amount,t1.create_at,t1.trading_status ";
		StringBuilder sb = new StringBuilder(
				"FROM `transaction_notification` t1,`user` t2 "+ 
				"where t1.sponsor_id = t2.user_id and t1.payer_id = ?");
		
		List<Object> values = new ArrayList<Object>();
		values.add(userId);
		
		sb.append(" order by t1.create_at desc");

		HashMap<String, Object> map = notificationDAO.getNotificationRecordsByPage(sql+sb.toString(),
				sb.toString(),values,currentPage, pageSize);
		return map;
	}
}

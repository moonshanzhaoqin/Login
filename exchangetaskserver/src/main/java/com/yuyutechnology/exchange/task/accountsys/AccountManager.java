/**
 * 
 */
package com.yuyutechnology.exchange.task.accountsys;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionManager;

/**
 * @author silent.sun
 *
 */
@Service
public class AccountManager {
	
	private static Logger logger = LogManager.getLogger(AccountManager.class);
	
	@Autowired
	AccountDAO accountDAO;
	
	@Autowired
	WalletDAO walletDAO;
	
	@Autowired
	SessionManager sessionManager;
	
	@Autowired
	CrmUserInfoManager crmUserInfoManager;
	
	@Autowired
	UserManager userManager;
	
	private void freezeUser (int userId) {
		SessionData sessionData = sessionManager.getByUserid(userId);
		if (sessionData != null) {
			sessionManager.cleanSession(sessionData.getSessionId());
		}
		sessionManager.delLoginToken(userId);
		userManager.logout(userId);
		userManager.userFreeze(userId, ServerConsts.LOGIN_AVAILABLE_OF_UNAVAILABLE);
	}
	
	public void accountingAll() {
		Date startDate = accountDAO.getLastAccountingTime();
		Date endDate = new Date();
		accounting(startDate, endDate);
		accountDAO.saveLastAccountingTime(endDate);
	}
	
	public void accountingUser(int userId, String currency) {
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
		Object[] accountInfo = accountDAO.calculatorWalletSeqByUserIdAndCurrency(wallet.getUpdateSeqId(), userId, currency);
		if (accountInfo != null && accountInfo[0] != null && accountInfo[1] != null) {
			BigDecimal sumAmount = (BigDecimal) accountInfo[0];
			BigDecimal balance = (BigDecimal) accountInfo[1];
			Date startTime = (Date) accountInfo[2];
			logger.info("accountingUser , userId : {}, currency : {}, sumAmount : {}, balanceBefore : {}, balanceNow : {}, startTime : {}", userId, currency, sumAmount, balance, wallet.getBalance(), startTime);
			if (sumAmount.add(balance).compareTo(wallet.getBalance()) != 0) {
				accountDAO.saveBadAccount(userId, currency, sumAmount, balance, wallet.getBalance(), startTime, new Date());
				//TODO: 账号冻结, 停止提现定时任务
				freezeUser(userId);
			}
		}
	}
	
	public void accounting(Date startDate, Date endDate) {
		int updateRows = accountDAO.snapshotWalletToNow(startDate, endDate);
		logger.info("accounting new rows, size : {}", updateRows);
		if (updateRows > 0) {
			int startId = accountDAO.getMAXSeqId4WalletBefore();
			int endId = accountDAO.getMAXSeqId4WalletNow();
			logger.info("accounting wallet_seq from {} to {}", startId, endId);
			if (startId < endId) {
				updateRows = accountDAO.accountingWalletSeq(startId, endId, startDate, endDate);
			}else{
				updateRows = 0;
			}
			logger.info("accounting finsh , bad account user size {}", updateRows);
			snapshotToBefore();
		}
	}
	
	public void freezeUsers () {
		
	}
	
	private void snapshotToBefore () {
		int updateRows = accountDAO.snapshotWalletNowToHistory();
		logger.info("accounting copy new to history, size : {}", updateRows);
		accountDAO.cleanSnapshotWalletNow();
		logger.info("accounting clean new ok ");
	}
}

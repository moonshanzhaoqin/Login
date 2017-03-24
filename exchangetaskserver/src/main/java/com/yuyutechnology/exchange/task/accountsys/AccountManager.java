/**
 * 
 */
package com.yuyutechnology.exchange.task.accountsys;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.BadAccountDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.BadAccount;
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
	BadAccountDAO badAccountDAO;
	
	@Autowired
	WalletDAO walletDAO;
	
	@Autowired
	SessionManager sessionManager;
	
	@Autowired
	GoldpayTransManager goldpayTransManager;
	
	@Autowired
	UserManager userManager;
	
	private void freezeUser(int userId) {
		logger.info("badAccount freezeUser, userId : {}", userId);
		SessionData sessionData = sessionManager.getByUserid(userId);
		if (sessionData != null) {
			sessionManager.cleanSession(sessionData.getSessionId());
		}
		sessionManager.delLoginToken(userId);
		userManager.logout(userId);
		userManager.userFreeze(userId, ServerConsts.LOGIN_AVAILABLE_OF_UNAVAILABLE);
	}
	
	public int accountingAll() {
		Date startDate = accountDAO.getLastAccountingTime();
		Date endDate = new Date();
		int userSize = accounting(startDate, endDate);
		accountDAO.saveLastAccountingTime(endDate);
		return userSize;
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
				goldpayTransManager.forbiddenGoldpayRemitWithdraws();
				freezeUser(userId);
				BadAccount badAccount = new BadAccount();
				badAccount.setUserId(userId);
				badAccount.setCurrency(currency);
				badAccount.setSumAmount(sumAmount);
				badAccount.setBalanceBefore(balance);
				badAccount.setBalanceNow(wallet.getBalance());
				badAccount.setStartTime(startTime);
				badAccount.setEndTime(new Date());
				badAccount.setBadAccountStatus(ServerConsts.BAD_ACCOUNT_STATUS_FREEZE_USER);
				badAccountDAO.saveBadAccount(badAccount);
			}
		}
	}
	
	public int accounting(Date startDate, Date endDate) {
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
			if (updateRows != 0) {
				return updateRows;
			}
			snapshotToBefore();
		}
		return 0;
	}
	
	public void freezeUsers () {
		List<BadAccount> badAccounts = badAccountDAO.findBadAccountList(ServerConsts.BAD_ACCOUNT_STATUS_DEFAULT);
		if (badAccounts != null && badAccounts.isEmpty()) {
			goldpayTransManager.forbiddenGoldpayRemitWithdraws();
			for (BadAccount badAccount : badAccounts) {
				try {
					freezeUser(badAccount.getUserId());
				} catch (Exception e) {
				}
			}
		}
	}
	
	private void snapshotToBefore () {
		int updateRows = accountDAO.snapshotWalletNowToHistory();
		logger.info("accounting copy new to history, size : {}", updateRows);
		accountDAO.cleanSnapshotWalletNow();
		logger.info("accounting clean new ok ");
	}
}

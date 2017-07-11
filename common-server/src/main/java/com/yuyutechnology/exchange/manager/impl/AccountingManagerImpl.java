/**
 * 
 */
package com.yuyutechnology.exchange.manager.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.AccountingDAO;
import com.yuyutechnology.exchange.dao.BadAccountDAO;
import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.AccountingManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionManager;

/**
 * @author silent.sun
 *
 */
@Service
public class AccountingManagerImpl implements AccountingManager {

	private static Logger logger = LogManager.getLogger(AccountingManagerImpl.class);

	@Autowired
	AccountingDAO accountingDAO;

	@Autowired
	BadAccountDAO badAccountDAO;

	@Autowired
	WalletDAO walletDAO;

	@Autowired
	CrmAlarmDAO crmAlarmDAO;

	@Autowired
	CrmAlarmManager crmAlarmManager;

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
		userManager.userFreeze(userId, ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE);
	}

	public int accountingAll() {
		if (goldpayTransManager.getGoldpayRemitWithdrawsforbidden()) {
			return 0;
		}
		Date startDate = accountingDAO.getLastAccountingTime();
		Date endDate = new Date();
		int userSize = accounting(startDate, endDate);
		accountingDAO.saveLastAccountingTime(endDate);
		return userSize;
	}

	public boolean accountingUser(int userId, String transferId) {
		Date startDate = accountingDAO.getLastAccountingTime();
		Date endDate = new Date();
		long startSeqId = accountingDAO.getMAXSeqId4WalletBeforeByUserId(userId);
		long endSeqId = accountingDAO.getMAXSeqId4WalletUserId(userId);
		if (startSeqId <= endSeqId) {
			int size = accountingDAO.calculatorWalletSeqByUserId(startSeqId, endSeqId, startDate, endDate, userId,
					transferId);
			if (size >= 1) {
				goldpayTransManager.forbiddenGoldpayRemitWithdraws("true");
				badAccountWarn();
				freezeUser(userId);
				return false;
			}
		}
		return true;
	}

	public int accounting(Date startDate, Date endDate) {
		int updateRows = accountingDAO.snapshotWalletToNow();
		logger.info("accounting new rows, size : {}", updateRows);
		if (updateRows > 0) {
			long startId = accountingDAO.getMAXSeqId4WalletBefore();
			long endId = accountingDAO.getMAXSeqId4WalletNow();
			logger.info("accounting wallet_seq from {} to {}", startId, endId);
			if (startId <= endId) {
				updateRows = accountingDAO.accountingWalletSeq(startId, endId, startDate, endDate);
			} else {
				updateRows = 0;
			}
			logger.info("accounting finsh , bad account user size {}", updateRows);
			if (updateRows == 0) {
				int updateRows2 = accountingDAO.snapshotWalletNowToHistory();
				logger.info("accounting copy new to history, size : {}", updateRows2);
			}
			accountingDAO.cleanSnapshotWalletNow();
			logger.info("accounting clean new ok ");
			return updateRows;
		}
		return 0;
	}

	public void freezeUsers() {
		List<Integer> badAccountUserIds = badAccountDAO.findBadAccountList(ServerConsts.BAD_ACCOUNT_STATUS_DEFAULT);
		if (badAccountUserIds != null && !badAccountUserIds.isEmpty()) {
			goldpayTransManager.forbiddenGoldpayRemitWithdraws("true");
			badAccountWarn();
			for (Integer badAccountUserId : badAccountUserIds) {
				try {
					freezeUser(badAccountUserId);
					badAccountDAO.updateBadAccountStatus(ServerConsts.BAD_ACCOUNT_STATUS_FREEZE_USER, badAccountUserId);
				} catch (Exception e) {
				}
			}
		}
	}

	public void snapshotToBefore(int userId) {
		accountingDAO.snapshotWalletToBeforeByUser(userId);
	}

	private void badAccountWarn() {
		List<CrmAlarm> list = crmAlarmDAO.getConfigListByTypeAndStatus(3, 1);
		if (list != null && !list.isEmpty()) {
			logger.info("accounting badAccountWarn listSize: {}", list.size());
			for (int i = 0; i < list.size(); i++) {
				CrmAlarm crmAlarm = list.get(i);
				logger.info("accounting badAccountWarn crmAlarm: {}", crmAlarm.getSupervisorIdArr());
				crmAlarmManager.alarmNotice(crmAlarm.getSupervisorIdArr(), "badAccountWarning", crmAlarm.getAlarmMode(),
						null);
			}
		}
	}
}
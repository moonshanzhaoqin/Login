/**
 * 
 */
package com.yuyutechnology.exchange.task.accountsys;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.pojo.Wallet;

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
	
	public void accountingAll() {
		Date startDate = accountDAO.getLastAccountingTime();
		Date endDate = new Date();
		accounting(startDate, endDate);
		accountDAO.saveLastAccountingTime(endDate);
	}
	
	public void accountingUser(int userId, String currency) {
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
		Object object = accountDAO.calculatorWalletSeqByUserIdAndCurrency(wallet.getUpdateSeqId(), userId, currency);
	}
	
	public void accounting(Date startDate, Date endDate) {
		int updateRows = accountDAO.snapshotWalletToNow(startDate, endDate);
		logger.info("accounting new rows, size : {}", updateRows);
		if (updateRows > 0) {
			int startId = accountDAO.getMAXSeqId4WalletBefore();
			int endId = accountDAO.getMAXSeqId4WalletNow();
			logger.info("accounting wallet_seq from {} to {}", startId, endId);
			updateRows = accountDAO.accountingWalletSeq(startId, endId, startDate, endDate);
			logger.info("accounting finsh , bad account user size {}", updateRows);
			snapshotToBefore();
		}
	}
	
	private void snapshotToBefore () {
		int updateRows = accountDAO.snapshotWalletNowToHistory();
		logger.info("accounting copy new to history, size : {}", updateRows);
		accountDAO.cleanSnapshotWalletNow();
		logger.info("accounting clean new ok ");
	}

}

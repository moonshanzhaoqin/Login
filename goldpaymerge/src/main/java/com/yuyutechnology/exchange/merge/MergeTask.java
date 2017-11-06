/**
 * 
 */
package com.yuyutechnology.exchange.merge;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;

/**
 * @author silent.sun
 *
 */
@Component
public class MergeTask {

	@Autowired
	ExanytimeMergeManager exanytimeMergeManager;

	@Autowired
	GoldpayMergeManager goldpayMergeManager;

	@Autowired
	UserDAO userDAO;

	@Autowired
	WalletDAO walletDAO;

	public static Logger logger = LogManager.getLogger(MergeTask.class);

	@PostConstruct
	public void mergeTask() {
		logger.info("START merge users======================================================================");
		
		BigDecimal totalTransGoldpay = BigDecimal.ZERO;
		
		/* Ex -> Goldpay */
		List<User> exUsers = userDAO.listAllUser();
		for (User user : exUsers) {
			if (user.getUserType() != ServerConsts.USER_TYPE_OF_SYSTEM) {
				try {
					totalTransGoldpay = totalTransGoldpay.add(exanytimeMergeManager.mergeExUserGoldpayToGoldpayServer(
							user.getUserId(), user.getAreaCode(), user.getUserPhone()));
				} catch (Exception e) {
					logger.error("Ex -> Goldpay : error!  " + user.getAreaCode() + user.getUserPhone(), e);
					// System.exit(0);
				}
			}
		}
		
		/* Goldpay -> Ex */
		List<Map<String, Object>> goldpayUsers = goldpayMergeManager.getAllGoldpayUser();
		for (Map<String, Object> map : goldpayUsers) {
			try {
				goldpayMergeManager.mergeGoldpayUserToExServer(map.get("user_id").toString(),
						map.get("username").toString(), map.get("account_id").toString(),
						map.get("area_code").toString(), map.get("mobile").toString());
			} catch (Exception e) {
				logger.error("Goldpay -> Ex : error!  " + map.get("user_id").toString() + " "
						+ map.get("username").toString() + " " + map.get("account_id").toString() + " "
						+ map.get("area_code").toString() + " " + map.get("mobile").toString(), e);
			}
		}

		User systemUser = userDAO.getSystemUser();
		Wallet systemWallet = walletDAO.getWalletByUserIdAndCurrency(systemUser.getUserId(),
				ServerConsts.CURRENCY_OF_GOLDPAY);
		logger.info("END merge users ========================== total transGoldpay : {} , systemGoldpay : {} ",
				totalTransGoldpay.longValue(), systemWallet.getBalance().longValue());
		if (BigDecimal.ZERO.compareTo(totalTransGoldpay.add(systemWallet.getBalance())) == 0) {
			walletDAO.emptyWallet(systemUser.getUserId(), ServerConsts.CURRENCY_OF_GOLDPAY);
			logger.info("***mergeTask success!***");
		} else {
			logger.error("mergeTask error!  please run again!");
		}
		System.exit(0);
	}
}

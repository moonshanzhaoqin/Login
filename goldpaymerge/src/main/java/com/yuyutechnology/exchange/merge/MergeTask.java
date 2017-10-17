/**
 * 
 */
package com.yuyutechnology.exchange.merge;

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
		logger.info("start merge users======================================================================");
		/*Ex -> Goldpay*/
		Long totalTransGoldpay = 0L;
		List<User> exUsers = userDAO.getUserList();
		for (User user : exUsers) {
			if (user.getUserType() == 0) {
				try {
					totalTransGoldpay = totalTransGoldpay + exanytimeMergeManager.mergeExUserGoldpayToGoldpayServer(user.getUserId(), user.getAreaCode(),
							user.getUserPhone());
				} catch (Exception e) {
					logger.error("mergeExUserGoldpayToGoldpayServer error , "+user.getAreaCode()+user.getUserPhone(), e);
				}
			}
		}
		/* Goldpay -> Ex */
		List<Map<String, Object>> goldpayUsers = goldpayMergeManager.getAllGoldpayUser();
		for (Map<String, Object> map : goldpayUsers) {
			try {
				goldpayMergeManager.mergeGoldpayUserToExServer(map.get("user_id").toString(),
						map.get("username").toString(), map.get("account_id").toString(), map.get("area_code").toString(),
						map.get("mobile").toString());
			} catch (Exception e) {
				logger.error("mergeGoldpayUserToExServer error , "+map.get("user_id").toString()+ " " +
						map.get("username").toString()+ " " + map.get("account_id").toString()+ " " +map.get("area_code").toString()+ " " +
						map.get("mobile").toString(), e);
			}
		}
		Wallet systemWallet = walletDAO.getWalletByUserIdAndCurrency(1, ServerConsts.CURRENCY_OF_GOLDPAY);
		logger.info("end merge users==================================================total transGoldpay : {} , systemGoldpay : {} ", totalTransGoldpay, systemWallet.getBalance().longValue());
		if (totalTransGoldpay+systemWallet.getBalance().longValue() == 0) {
			walletDAO.emptyWallet(1, ServerConsts.CURRENCY_OF_GOLDPAY);
		}else{
			logger.error("mergeTask error ! please run again !");
		}
		System.exit(0);
	}
}

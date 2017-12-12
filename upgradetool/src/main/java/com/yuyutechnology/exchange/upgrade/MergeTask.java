/**
 * 
 */
package com.yuyutechnology.exchange.upgrade;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.util.PinyinUtils;

/**
 * @author silent.sun
 *
 */
@Component
public class MergeTask {

	 @Autowired
	 UpgradeManager upgradeManager;

	public static Logger logger = LogManager.getLogger(MergeTask.class);

	// public static String feeAccount = "10001";
	// public static String recoveryAccount = "10002";
	// public static String frozenAccount = "10003";

	@PostConstruct
	public void upgradeTask() {
		// //手续费账号 type 2
		// logger.info("create feeAccount");
		// upgradeManager.registerAccount("+86", feeAccount, "feeAccount", feeAccount,
		// ServerConsts.USER_TYPE_OF_FEE);
		// //回收账号 type 3
		// logger.info("create recoveryAccount");
		// upgradeManager.registerAccount("+86", recoveryAccount, "recoveryAccount",
		// recoveryAccount, ServerConsts.USER_TYPE_OF_RECOVERY);
		// //冻结账户 type 4
		// logger.info("create frozenAccount");
		// upgradeManager.registerAccount("+86", frozenAccount, "frozenAccount",
		// frozenAccount, ServerConsts.USER_TYPE_OF_FROZEN);

		upgradeManager.userNameToPinyin();

		System.exit(0);
	}
}

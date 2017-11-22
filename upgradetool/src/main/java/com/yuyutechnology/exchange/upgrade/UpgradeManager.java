/**
 * 
 */
package com.yuyutechnology.exchange.upgrade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.Api;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.goldpay.msg.GoldpayUserDTO;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.util.LanguageUtils.Language;
import com.yuyutechnology.exchange.util.MathUtils;
import com.yuyutechnology.exchange.util.PasswordUtils;

/**
 * @author silent.sun
 *
 */
@Component
public class UpgradeManager {
	
	public static Logger logger = LogManager.getLogger(UpgradeManager.class);

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	WalletDAO walletDAO;
	
	@Autowired
	BindDAO bindDAO;
	
	@Autowired
	CommonManager commonManager;
	
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;
	
	public void registerAccount(String areaCode, String userPhone, String userName, String userPassword, int userType) {
		if (userType == ServerConsts.USER_TYPE_OF_FEE && userDAO.getFeeUser() != null) {
			return;
		}
		if (userType == ServerConsts.USER_TYPE_OF_FROZEN && userDAO.getFrozenUser() != null) {
			return;
		}
		if (userType == ServerConsts.USER_TYPE_OF_RECOVERY && userDAO.getRecoveryUser() != null) {
			return;
		}
		/* 随机生成盐值 */
		String passwordSalt = DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(6));
		logger.info("Randomly generated salt values : salt={}", passwordSalt);
		/* 添加用户 */
		Integer userId = userDAO.addUser(new User(areaCode, userPhone, userName,
				PasswordUtils.encrypt(userPassword, passwordSalt), new Date(), userType,
				ServerConsts.USER_AVAILABLE_OF_AVAILABLE, ServerConsts.LOGIN_AVAILABLE_OF_AVAILABLE,
				ServerConsts.PAY_AVAILABLE_OF_AVAILABLE, passwordSalt, Language.en_US));
		logger.info("Add user complete!");
		/* 添加钱包信息 */
		logger.info("New wallets for newly registered user {}==>", userId);
		List<Currency> currencies = commonManager.getCurrentCurrencies();
		for (Currency currency : currencies) {
			walletDAO.addwallet(new Wallet(currency, userId, BigDecimal.ZERO, new Date(), 0));
		}
		/* 创建Goldpay账号 */
		GoldpayUserDTO goldpayUser = goldpayTrans4MergeManager.createGoldpay(areaCode, userPhone, userName, true);
		bindDAO.updateBind(
				new Bind(userId, goldpayUser.getId() + "", goldpayUser.getUsername(), goldpayUser.getAccountNum()));
	}
}

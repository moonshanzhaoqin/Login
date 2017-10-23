/**
 * 
 */
package com.yuyutechnology.exchange.merge;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.goldpay.GoldpayManager;
import com.yuyutechnology.exchange.goldpay.GoldpayUser;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.util.LanguageUtils;
import com.yuyutechnology.exchange.util.MathUtils;
import com.yuyutechnology.exchange.util.PasswordUtils;

/**
 * @author silent.sun
 *
 */
@Service
public class GoldpayMergeManager {

	public static Logger logger = LogManager.getLogger(GoldpayMergeManager.class);

	@Resource
	JdbcTemplate jdbcTemplate;
	@Autowired
	BindDAO bindDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	GoldpayManager goldpayManager;
	@Autowired
	RedisDAO redisDAO;
	@Autowired
	CommonManager commonManager;

	public List<Map<String, Object>> getAllGoldpayUser() {
		List<Map<String, Object>> users = jdbcTemplate.queryForList(
				"SELECT account.user_id, account.account_id,guser.username,guser.`area_code`,guser.`mobile` FROM `goldq_account` account LEFT JOIN `goldq_user` guser ON guser.id = account.user_id  WHERE guser.`area_code` IS NOT NULL AND guser.`area_code` != '+00' AND account.balance > 0;");
		logger.info("getAllGoldpayUser size : " + users.size());
		return users;
	}

	public void mergeGoldpayUserToExServer(String goldpayUserId, String username, String accountNumber, String areaCode,
			String userPhone) {
		Integer userId = null;
		/* 查找对应的Ex账号 */
		User user = userDAO.getUserByUserPhone(areaCode, userPhone);
		if (user == null) {
			/* 创建新的EX账号 */
			userId = register(areaCode, userPhone, username, DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(8)),
					"en_US");
		} else {
			userId = user.getUserId();
		}
		if (userId == null) {
			logger.warn("Goldpay -> Ex:{} fail!---Can not register Ex.", accountNumber);
			return;
		}

		/* 绑定goldpay */
		Bind bind = bindDAO.getBindByUserId(userId);
		if (bind == null) {
			bindDAO.updateBind(new Bind(userId, goldpayUserId, username, accountNumber));
		} else if (!StringUtils.equals(bind.getGoldpayAcount(), accountNumber)) {
			bind.setGoldpayId(goldpayUserId);
			bind.setGoldpayName(username);
			bind.setGoldpayAcount(accountNumber);
			bindDAO.updateBind(bind);
		}
		logger.info("Goldpay -> Ex:{}  SUCCESS!", accountNumber);
		return;
	}

	private Integer register(String areaCode, String userPhone, String userName, String userPassword, String language) {
		/* 随机生成盐值 */
		String passwordSalt = DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(6));
		/* 添加用户 */
		Integer userId = userDAO.addUser(new User(areaCode, userPhone, userName,
				PasswordUtils.encrypt(userPassword, passwordSalt), new Date(), ServerConsts.USER_TYPE_OF_CUSTOMER,
				ServerConsts.USER_AVAILABLE_OF_AVAILABLE, ServerConsts.LOGIN_AVAILABLE_OF_AVAILABLE,
				ServerConsts.PAY_AVAILABLE_OF_AVAILABLE, passwordSalt, LanguageUtils.standard(language)));
		logger.info("Add user complete! userId = {} ", userId);
		/* 记录换绑手机时间 */
		redisDAO.saveData("changephonetime" + userId, new Date().getTime());
		/* 添加钱包信息 */
		logger.info("New wallets for newly registered user {} ->", userId);
		List<Currency> currencies = commonManager.getCurrentCurrencies();
		for (Currency currency : currencies) {
			walletDAO.addwallet(new Wallet(currency, userId, new BigDecimal(0), new Date(), 0));
		}
		return userId;
	}

}

/**
 * 
 */
package com.yuyutechnology.exchange.merge;

import java.math.BigDecimal;
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
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.goldpay.GoldpayManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.util.MathUtils;

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
	UserManager userManager;
	@Autowired
	BindDAO bindDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	GoldpayManager goldpayManager;

	public List<Map<String, Object>> getAllGoldpayUser() {
		List<Map<String, Object>> users = jdbcTemplate.queryForList(
				"SELECT account.user_id, account.account_id,guser.username,guser.`area_code`,guser.`mobile` FROM `goldq_account` account LEFT JOIN `goldq_user` guser ON guser.id = account.user_id  WHERE guser.`area_code` IS NOT NULL AND guser.`area_code` != '+00' AND account.balance > 0;");
		logger.info("getAllGoldpayUser size : " + users.size());
		return users;
	}

	public void mergeGoldpayUserToExServer(String goldpayUserId, String username, String accountNumber, String areaCode,
			String userPhone) {
		/* 查找对应的Ex账号 */
		User user = userDAO.getUserByUserPhone(areaCode, userPhone);
		Integer userId = null;
		if (user == null) {
			/* 创建新的EX账号 */
			userId = userManager.register(areaCode, userPhone, username,
					DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(8)), "en_US");
		} else {
			userId = user.getUserId();
			Bind bind = bindDAO.getBindByUserId(userId);
			if (bind == null || StringUtils.equals(bind.getGoldpayAcount(), accountNumber)) {
				/* 绑定goldpay */
				if (bind == null)
					bind = new Bind();
				bind.setUserId(userId);
				bind.setGoldpayId(goldpayUserId);
				bind.setGoldpayName(username);
				bind.setGoldpayAcount(accountNumber);
				bindDAO.updateBind(bind);
				// bindDAO.updateBind(new Bind(userId, goldpayUserId, username, accountNumber));
			}
		}
		if (userId == null) {
			logger.warn("Goldpay -> Ex:{} fail!---Can not register Ex.", accountNumber);
			return;
		}
		logger.info("Goldpay -> Ex:{}  SUCCESS!", accountNumber);
		return;
	}
}

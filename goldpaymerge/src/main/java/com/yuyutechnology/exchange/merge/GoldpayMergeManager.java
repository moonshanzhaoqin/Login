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
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.goldpay.GoldpayManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Bind;
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
	GoldpayManager goldpayManager;

	public List<Map<String, Object>> getAllGoldpayUser() {
		List<Map<String, Object>> users = jdbcTemplate.queryForList(
				"SELECT account.user_id, account.account_id,guser.username,guser.`area_code`,guser.`mobile` FROM `goldq_account` account LEFT JOIN `goldq_user` guser ON guser.id = account.user_id  WHERE guser.`area_code` IS NOT NULL AND account.balance > 0;");
		logger.info("getAllGoldpayUser size : " + users.size());
		return users;
	}

	public void mergeGoldpayUserToExServer(String goldpayUserId, String username, String accountNumber, String areaCode,
			String phoneNumber) {
		/* 查找对应的Ex账号 */
		Integer userId = userManager.getUserId(areaCode, phoneNumber);
		if (userId == null) {
			/* 创建新的EX账号 */
			userId = userManager.register(areaCode, phoneNumber, username,
					DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(8)), "en_US");
		} else {
			Bind bind = bindDAO.getBindByUserId(userId);
			if (bind == null || StringUtils.equals(bind.getGoldpayAcount(), accountNumber)) {
				/* 绑定goldpay */
				bindDAO.updateBind(new Bind(userId, goldpayUserId, username, accountNumber));
			}

			/* 将EX的GDQ转到Goldpay中 */
			Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, ServerConsts.CURRENCY_OF_GOLDPAY);
			if (wallet.getBalance().compareTo(BigDecimal.ZERO) > 0) {
				if (goldpayManager.transferGDQ2Goldpay(bind.getGoldpayAcount(), wallet.getBalance())) {
					walletDAO.emptyWallet(userId, ServerConsts.CURRENCY_OF_GOLDPAY);
				} else {
					logger.warn("mergeGoldpayUserToExServer:{} fail!---Can not transfer GDQ from Ex to Goldpay.",
							accountNumber);
					return;
				}
			}
		}
		if (userId==null) {
			logger.warn("mergeGoldpayUserToExServer:{} fail!---Can not register Ex.",
					accountNumber);
			return;
		}
		logger.info("mergeGoldpayUserToExServer:{}  success!",accountNumber);
		return;
	}
}

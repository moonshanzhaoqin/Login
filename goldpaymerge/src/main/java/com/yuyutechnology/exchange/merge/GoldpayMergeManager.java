/**
 * 
 */
package com.yuyutechnology.exchange.merge;

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

import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Bind;
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

	public List<Map<String, Object>> getAllGoldpayUser() {
		List<Map<String, Object>> users = jdbcTemplate.queryForList(
				"SELECT account.user_id, account.account_id,guser.username,guser.`area_code`,guser.`mobile` FROM `goldq_account` account LEFT JOIN `goldq_user` guser ON guser.id = account.user_id  WHERE guser.`area_code` IS NOT NULL AND account.balance > 0;");
		logger.info("getAllGoldpayUser size : " + users.size());
		return users;
	}

	public void mergeGoldpayUserToExServer(String goldpayUserId, String username, String accountNumber,
			String areaCode, String phoneNumber) {
		/* 查找对应的Ex账号 */
		Integer userId = userManager.getUserId(areaCode, phoneNumber);
		if (userId == null) {
			/*创建新的EX账号*/
			userManager.register(areaCode, phoneNumber, username, DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(8)),
					"en_US");
		} else {
			Bind bind = bindDAO.getBindByUserId(userId);
			if (bind == null || StringUtils.equals(bind.getGoldpayAcount(),accountNumber)) {
				/* 绑定goldpay*/
				bindDAO.updateBind(new Bind(userId, goldpayUserId, username, accountNumber));
			} 

			// TODO 将EX的GDQ转到Goldpay中
		}

	}
}

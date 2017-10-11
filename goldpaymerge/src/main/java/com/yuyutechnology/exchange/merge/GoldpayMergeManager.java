/**
 * 
 */
package com.yuyutechnology.exchange.merge;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author silent.sun
 *
 */
@Service
public class GoldpayMergeManager {

	public static Logger logger = LogManager.getLogger(GoldpayMergeManager.class);
	
	@Resource
	JdbcTemplate jdbcTemplate;
	
	public void getAllGoldpayUser() {
		List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT account.balance,account.account_id,guser.email,guser.username,guser.`area_code`,guser.`mobile` FROM `goldq_account` account LEFT JOIN `goldq_user` guser ON guser.id = account.user_id  WHERE guser.`area_code` IS NOT NULL AND account.balance > 0;");
		logger.info("getAllGoldpayUser size : " + users.size());
	}
	
	public void mergeGoldpayUserToExServer (Integer goldppayUserId, String username, String accountNumber, String areaCode, String phoneNumber) {
		//TODO:
	}
}

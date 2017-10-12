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

import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.User;

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

	public static Logger logger = LogManager.getLogger(MergeTask.class);

	@PostConstruct
	public void mergeTask() {
		logger.info("start merge users======================================================================");
		/*Ex->Goldpay*/
		List<User> exUsers = userDAO.getUserList();
		for (User user : exUsers) {
			exanytimeMergeManager.mergeExUserGoldpayToGoldpayServer(user.getUserId(), user.getAreaCode(),
					user.getUserPhone());
		}

		/* Goldpay->Ex */
		List<Map<String, Object>> goldpayUsers = goldpayMergeManager.getAllGoldpayUser();
		for (Map<String, Object> map : goldpayUsers) {
			// TODO
			goldpayMergeManager.mergeGoldpayUserToExServer(map.get("user_id").toString(),
					map.get("username").toString(), map.get("account_id").toString(), map.get("area_code").toString(),
					map.get("mobile").toString());
		}

		logger.info("end merge users======================================================================");
	}
}

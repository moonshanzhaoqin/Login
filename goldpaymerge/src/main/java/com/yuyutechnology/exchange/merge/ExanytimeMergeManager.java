/**
 * 
 */
package com.yuyutechnology.exchange.merge;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.goldpay.GoldpayManager;
import com.yuyutechnology.exchange.goldpay.GoldpayUser;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.User;

/**
 * @author silent.sun
 *
 */
@Service
public class ExanytimeMergeManager {

	public static Logger logger = LogManager.getLogger(ExanytimeMergeManager.class);

	@Resource
	HibernateTemplate hibernateTemplate;

	@Autowired
	UserDAO userDAO;
	@Autowired
	BindDAO bindDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	UserManager userManager;
	@Autowired
	GoldpayTransManager goldpayTransManager;
	@Autowired
	GoldpayManager goldpayManager;

	public void mergeExUserGoldpayToGoldpayServer(Integer userId, String areaCode, String userPhone) {

		/* 用手机号创建Goldpay账号 */
		GoldpayUser goldpayUser = goldpayManager.createGoldpay(areaCode, userPhone, false);
		if (goldpayUser == null) {
			logger.warn("mergeExUserGoldpayToGoldpayServer:{}  fail!---Can not creat Glodpay.", userId);
			return;
		} else {
			/* 查找绑定信息 */
			Bind bind = bindDAO.getBindByUserId(userId);
			if (bind == null || !StringUtils.equals(bind.getGoldpayAcount(), goldpayUser.getAccountNum())) {
				/* 绑定goldpay */
				bindDAO.updateBind(
						new Bind(userId, goldpayUser.getId(), goldpayUser.getUsername(), goldpayUser.getAccountNum()));
			}

			// TODO 将EX的GDQ转到Goldpay中

		}

	}

}

/**
 * 
 */
package com.yuyutechnology.exchange.merge;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.goldpay.GoldpayManager;
import com.yuyutechnology.exchange.goldpay.GoldpayUser;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.Wallet;

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
	GoldpayManager goldpayManager;

	public BigDecimal mergeExUserGoldpayToGoldpayServer(Integer userId, String areaCode, String userPhone) {

		/* 用手机号创建Goldpay账号 */
		GoldpayUser goldpayUser = goldpayManager.createGoldpay(areaCode, userPhone, false);
		if (goldpayUser == null) {
			logger.warn("Ex -> Goldpay: Ex : {},{} FAIL!---Can not create Goldpay.", userId, areaCode + userPhone);
		} else {
			/* 查找绑定信息 */
			Bind bind = bindDAO.getBindByUserId(userId);
			/* 绑定goldpay */
			if (bind == null) {
				bind=new Bind(userId, goldpayUser.getId(), goldpayUser.getUsername(), goldpayUser.getAccountNum());
				bindDAO.updateBind(bind);
			} else if (!StringUtils.equals(bind.getGoldpayAcount(), goldpayUser.getAccountNum())) {
				bind.setGoldpayId(goldpayUser.getId());
				bind.setGoldpayName(goldpayUser.getUsername());
				bind.setGoldpayAcount(goldpayUser.getAccountNum());
				bindDAO.updateBind(bind);
			}
			logger.info("Ex -> Goldpay: create {},{} new Goldpay : {} , bind : {}", userId, areaCode + userPhone,
					goldpayUser.getAccountNum(), bind.getGoldpayAcount());

			/* 将EX的GDQ转到Goldpay中 */
			Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, ServerConsts.CURRENCY_OF_GOLDPAY);
			if (wallet.getBalance().compareTo(BigDecimal.ZERO) > 0) {
				logger.info("Ex -> Goldpay: to transfer {},{} to {}, balance : {} -->", userId, areaCode + userPhone,
						goldpayUser.getAccountNum(), wallet.getBalance());
				if (goldpayManager.transferGDQ2Goldpay(bind.getGoldpayAcount(), wallet.getBalance())) {
					walletDAO.emptyWallet(userId, ServerConsts.CURRENCY_OF_GOLDPAY);
					logger.info("Ex -> Goldpay: Ex : {},{}  Goldpay : {} SUCCESS!", userId, areaCode + userPhone,
							goldpayUser.getAccountNum());
					return wallet.getBalance();
				} else {
					logger.warn("Ex -> Goldpay: Ex : {},{} FAIL!---Can not transfer GDQ from Ex to Goldpay.", userId,
							areaCode + userPhone);
				}
			}
		}
		return BigDecimal.ZERO;
	}

}

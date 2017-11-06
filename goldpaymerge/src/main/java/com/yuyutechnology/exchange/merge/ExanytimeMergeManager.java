/**
 * 
 */
package com.yuyutechnology.exchange.merge;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
import com.yuyutechnology.exchange.goldpay.msg.GoldpayUserDTO;
import com.yuyutechnology.exchange.goldpay.msg.Transfer2GoldpayC2S;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.util.HttpClientUtils;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.ResourceUtils;

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
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;

	public BigDecimal mergeExUserGoldpayToGoldpayServer(Integer userId, String areaCode, String userPhone, String userName) {

		/* 用手机号创建Goldpay账号 */
		GoldpayUserDTO goldpayUser = goldpayTrans4MergeManager.createGoldpay(areaCode, userPhone, userName, false);
		if (goldpayUser == null) {
			logger.warn("Ex -> Goldpay: Ex : {},{} FAIL!---Can not create Goldpay.", userId, areaCode + userPhone);
		} else {
			/* 查找绑定信息 */
			Bind bind = bindDAO.getBindByUserId(userId);
			/* 绑定goldpay */
			if (bind == null) {
				bind = new Bind(userId, goldpayUser.getId() + "", goldpayUser.getUsername(),
						goldpayUser.getAccountNum());
				bindDAO.updateBind(bind);
			} else if (!StringUtils.equals(bind.getGoldpayAcount(), goldpayUser.getAccountNum())) {
				bind.setGoldpayId(goldpayUser.getId() + "");
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
				if (transferGDQ2Goldpay(bind.getGoldpayAcount(), wallet.getBalance())) {
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

	@SuppressWarnings("unchecked")
	public boolean transferGDQ2Goldpay(String accountNum, BigDecimal balance) {
		Transfer2GoldpayC2S transfer2GoldpayRequset = new Transfer2GoldpayC2S();
		transfer2GoldpayRequset.setBalance(balance.longValue());
		transfer2GoldpayRequset.setComment("");
		transfer2GoldpayRequset
				.setFromAccountNum(ResourceUtils.getBundleValue4String("goldpay.merchant.fromAccountNum"));
		transfer2GoldpayRequset.setOrderType("3");
		transfer2GoldpayRequset.setPayOrderId("");
		transfer2GoldpayRequset.setToAccountNum(accountNum);
		transfer2GoldpayRequset.setToken(ResourceUtils.getBundleValue4String("goldpay.merchant.token"));

		String param = JsonBinder.getInstance().toJson(transfer2GoldpayRequset);
		logger.info("param==={}", param);
		String result = HttpClientUtils.sendPost(
				ResourceUtils.getBundleValue4String("goldpay.url") + "trans/payConfirmTransaction4Merchant", param);
		logger.info("result==={}", result);
		if (StringUtils.isNotEmpty(result)) {
			Map resultMap = JsonBinder.getInstance().fromJson(result, HashMap.class);
			if ((Integer) resultMap.get("retCode") == 1) {
				return true;
			} else {

			}
		}
		return false;
	}
}

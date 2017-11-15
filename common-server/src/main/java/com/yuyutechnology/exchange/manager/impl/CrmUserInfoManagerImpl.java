package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CrmUserInfoDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.CrmUserInfoManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.pojo.CrmUserInfo;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.util.DateFormatUtils;
import com.yuyutechnology.exchange.util.page.PageBean;

@Service
public class CrmUserInfoManagerImpl implements CrmUserInfoManager {

	@Autowired
	UserDAO userDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	CrmUserInfoDAO crmUserInfoDAO;
	@Autowired
	RedisDAO redisDAO;

	@Autowired
	OandaRatesManager oandaRatesManager;

	public static Logger logger = LogManager.getLogger(CrmUserInfoManagerImpl.class);

	@Override
	public void updateUserInfo(User user) {
		CrmUserInfo crmUserInfo = new CrmUserInfo(user);
		BigDecimal totalBalance = oandaRatesManager.getTotalBalance(user.getUserId());
		logger.info("current time : {} , user Id : {} ,totalBalance : {}",
				new Object[] { new Date(), user.getUserId(), totalBalance });
		crmUserInfo.setUserTotalAssets(totalBalance);
		crmUserInfo.setUpdateAt(new Date());
		// crmUserInfo.setLoginTime(user.getLoginTime());
		crmUserInfoDAO.updateUserInfo(crmUserInfo);
	}

	@Override
	public HashMap<String, BigDecimal> getSystemAccountTotalAssets() {

		HashMap<String, BigDecimal> map = new HashMap<>();

		User systemUser = userDAO.getSystemUser();
		if (systemUser == null) {
			return map;
		}
		// 获取系统账户各个币种的资产
		List<Wallet> list = walletDAO.getWalletsByUserId(systemUser.getUserId());
		if (list.isEmpty()) {
			return map;
		}
		for (Wallet wallet : list) {
			map.put(wallet.getCurrency().getCurrency(), wallet.getBalance());
		}

		// 计算系统账户总资产
		BigDecimal totalAssets = BigDecimal.ZERO;
		for (Entry<String, BigDecimal> entry : map.entrySet()) {
			if (entry.getKey().equals(ServerConsts.STANDARD_CURRENCY)) {
				totalAssets = totalAssets.add(entry.getValue());
				logger.info("SystemAccountTotalAssets : {}", totalAssets);
			} else {
				totalAssets = totalAssets
						.add(oandaRatesManager.getDefaultCurrencyAmount(entry.getKey(), entry.getValue()));
				logger.info("SystemAccountTotalAssets : {}", totalAssets);
			}
		}
		map.put("totalAssets", totalAssets.setScale(4, RoundingMode.DOWN));

		logger.info("System Account Total Assets : {}", map.toString());
		return map;
	}

	@Override
	public HashMap<String, BigDecimal> getUserAccountTotalAssets() {

		HashMap<String, BigDecimal> map = new HashMap<>();

		// 获取各个币种的用户资产总和
		User systemUser = userDAO.getSystemUser();
		if (systemUser == null) {
			logger.error("SYSTEM ACCOUNT DOES NOT EXIST!!!");
			return map;
		} else {
			map = walletDAO.getUserAccountTotalAssets(systemUser.getUserId());
		}

		// 计算用户账户总金额
		BigDecimal totalAssets = BigDecimal.ZERO;
		if (map.isEmpty()) {
			logger.warn("get all users' total assets by currency FAILURE!!!");
		} else {
			for (Entry<String, BigDecimal> entry : map.entrySet()) {
				if (entry.getKey().equals(ServerConsts.STANDARD_CURRENCY)) {
					totalAssets = totalAssets.add(entry.getValue());
					logger.info("UserAccountTotalAssets : {}", totalAssets);
				} else {
					totalAssets = totalAssets
							.add(oandaRatesManager.getDefaultCurrencyAmount(entry.getKey(), entry.getValue()));
					logger.info("UserAccountTotalAssets : {}", totalAssets);
				}
			}
		}
		map.put("totalAssets", totalAssets.setScale(4, RoundingMode.DOWN));

		logger.info("User Account Total Assets : {}", map.toString());
		return map;

	}

	@Override
	public HashMap<String, Object> getUserAccountInfoListByPage(String userPhone, String userName, int userAvailable,
			int loginAvailable, int payAvailable, BigDecimal upperLimit, BigDecimal lowerLimit, int currentPage,
			int pageSize) {

		List<Object> values = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("from CrmUserInfo where userType = 0 ");

		if (!StringUtils.isEmpty(userPhone)) {
			sb.append("and userPhone like '" + userPhone + "%' ");
		}
		if (userAvailable != 3) {
			sb.append("and userAvailable = ? ");
			values.add(userAvailable);
		}
		if (loginAvailable != 3) {
			sb.append("and loginAvailable = ? ");
			values.add(loginAvailable);
		}
		if (payAvailable != 3) {
			sb.append("and payAvailable = ? ");
			values.add(payAvailable);
		}
		if (upperLimit != null && upperLimit.compareTo(BigDecimal.ZERO) != -1) {
			sb.append("and userTotalAssets <= ? ");
			values.add(upperLimit);
		}
		if (lowerLimit != null && lowerLimit.compareTo(BigDecimal.ZERO) != -1) {
			sb.append("and userTotalAssets >= ? ");
			values.add(lowerLimit);
		}
		if (!StringUtils.isEmpty(userName)) {
			sb.append("and userName like '%" + userName + "%'");

		}

		sb.append(" order by userTotalAssets desc, userPhone");

		HashMap<String, Object> result = crmUserInfoDAO.getUserAccountInfoListByPage(sb.toString(), values, currentPage,
				pageSize);

		return result;
	}

	@Override
	public void updateImmediately() {

		redisDAO.saveData("updateImmediately", 1);

		List<User> list = userDAO.listAllUser();
		if (list.isEmpty()) {
			return;
		}

		for (User user : list) {
			updateUserInfo(user);
		}

		redisDAO.saveData("updateImmediately", 0);
	}

	@Override
	public int getUpdateFlag() {
		String updateFlag = redisDAO.getValueByKey("updateImmediately");

		if (StringUtils.isNotBlank(updateFlag)) {
			return Integer.parseInt(updateFlag);
		}
		return 0;
	}

	@Override
	public PageBean getUserInfoByPage(int currentPage, String userPhone, String userName, String startTime,
			String endTime) {
		logger.info("currentPage={},userPhone={},userName={}  {}->{}", currentPage, userPhone, userName, startTime,
				endTime);

		List<Object> values = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from CrmUserInfo where 1 = 1");
		if (StringUtils.isNotBlank(userPhone)) {
			hql.append("and userPhone = ?");
			values.add(userPhone);
		}
		if (StringUtils.isNotBlank(userName)) {
			hql.append("and userName like ?");
			values.add("%" + userName + "%");
		}
		if (StringUtils.isNotBlank(startTime)) {
			hql.append("and createTime >   ?");
			values.add(DateFormatUtils.getStartTime(startTime));
		}
		if (StringUtils.isNotBlank(endTime)) {
			hql.append("and  createTime < ?");
			values.add(DateFormatUtils.getEndTime(endTime));
		}
		hql.append(" order by loginTime desc");
		return crmUserInfoDAO.getUserInfoByPage(hql.toString(), values, currentPage, 10);
	}

}

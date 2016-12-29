package com.yuyutechnology.exchange.manager.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.dao.AdminDAO;
import com.yuyutechnology.exchange.manager.AdminManager;
import com.yuyutechnology.exchange.pojo.Admin;
import com.yuyutechnology.exchange.utils.PasswordUtils;

@Service
public class AdminManagerImpl implements AdminManager {
	private static Logger logger = LoggerFactory.getLogger(AdminManagerImpl.class);

	@Autowired
	AdminDAO adminDAO;

	@Override
	public int login(String adminName, String adminPassword) {
		Admin admin = adminDAO.getAdminByName(adminName);
		if (admin == null) {
			logger.info("no admin!");
			return RetCodeConsts.ADMIN_NOT_EXIST;
		} else if (PasswordUtils.check(adminPassword, admin.getAdminPassword(), admin.getPasswordSalt())) {
			logger.info("login successs");
			return RetCodeConsts.SUCCESS;
		} else {
			logger.info("password not match name");
			return RetCodeConsts.PASSWORD_NOT_MATCH_NAME;
		}
	}

}

package com.yuyutechnology.exchange.manager.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.AdminDAO;
import com.yuyutechnology.exchange.manager.AdminManager;
import com.yuyutechnology.exchange.pojo.Admin;
import com.yuyutechnology.exchange.utils.MathUtils;
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

	@Override
	public void addAdmin(String adminName) {
		Admin admin = adminDAO.getAdminByName(adminName);
		if (admin == null) {
			// 随机生成盐值
			String passwordSalt = DigestUtils.md5Hex(MathUtils.randomFixedLengthStr(6));
			logger.info("Randomly generated salt values===salt={}", passwordSalt);
			adminDAO.updateAdmin(new Admin(adminName,
					PasswordUtils.encrypt(ServerConsts.ADMIN_DEFAULT_PASSWORD, passwordSalt), passwordSalt));
			logger.info("{} add success, default password: {}", adminName, ServerConsts.ADMIN_DEFAULT_PASSWORD);
		} else {
			logger.info("{} already exists", adminName);
		}
	}

	@Override
	public int modifyPassword(Integer adminId,String oldPassword, String newPassword) {
		Admin admin = adminDAO.getAdmin(adminId);
		if (PasswordUtils.check(oldPassword, admin.getAdminPassword(), admin.getPasswordSalt())) {
			admin.setAdminPassword(PasswordUtils.encrypt(newPassword, admin.getPasswordSalt()));
			logger.info("modifyPassword sucess");
			return RetCodeConsts.SUCCESS;
		}else{
			logger.info("oldPassword is wrong");
			return RetCodeConsts.PASSWORD_NOT_MATCH_NAME;
		}
	}
	
	
	

}

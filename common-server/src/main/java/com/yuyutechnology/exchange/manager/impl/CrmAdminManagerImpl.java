package com.yuyutechnology.exchange.manager.impl;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.AdminDAO;
import com.yuyutechnology.exchange.manager.CrmAdminManager;
import com.yuyutechnology.exchange.pojo.Admin;
import com.yuyutechnology.exchange.utils.MathUtils;
import com.yuyutechnology.exchange.utils.PasswordUtils;

@Service
public class CrmAdminManagerImpl implements CrmAdminManager {
	private static Logger logger = LogManager.getLogger(CrmAdminManagerImpl.class);

	@Autowired
	AdminDAO adminDAO;

	@Override
	public String login(String adminName, String adminPassword) {
		Admin admin = adminDAO.getAdminByName(adminName);
		if (admin == null) {
			logger.info("no admin!");
			return RetCodeConsts.ADMIN_NOT_EXIST;
		} else if (PasswordUtils.check(adminPassword, admin.getAdminPassword(), admin.getPasswordSalt())) {
			logger.info("login successs");
			return RetCodeConsts.RET_CODE_SUCCESS;
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
	public String modifyPassword(String adminName,String oldPassword, String newPassword) {
		Admin admin = adminDAO.getAdminByName(adminName);
		if (PasswordUtils.check(oldPassword, admin.getAdminPassword(), admin.getPasswordSalt())) {
			admin.setAdminPassword(PasswordUtils.encrypt(newPassword, admin.getPasswordSalt()));
			logger.info("modifyPassword sucess");
			return RetCodeConsts.RET_CODE_SUCCESS;
		}else{
			logger.info("oldPassword is wrong");
			return RetCodeConsts.PASSWORD_NOT_MATCH_NAME;
		}
	}
	
	@Override
	public List<Admin> getAdminList(){
		return adminDAO.getAdminList();
	}
	

}

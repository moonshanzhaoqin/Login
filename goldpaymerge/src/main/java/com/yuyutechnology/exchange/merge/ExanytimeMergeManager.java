/**
 * 
 */
package com.yuyutechnology.exchange.merge;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.BindDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.User;
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
	GoldpayTransManager goldpayTransManager;
	
	
	public void mergeExUserGoldpayToGoldpayServer(Integer userId) {
		User user=userDAO.getUser(userId);
		
		//TODO 用手机号创建Goldpay账号 
		
		
		//TODO: 查找绑定信息
		Bind bind=bindDAO.getBindByUserId(userId);
		if(bind==null) {
			//TODO  绑定goldpay

		}else {
			//TODO 查看两个账号是否一致
			if (true) {
				//TODO  不一致 重新绑定goldpay
			}
		}
		
		
		//TODO 将EX的GDQ转到Goldpay中
		
		
		
	}
	
	
	
	
}

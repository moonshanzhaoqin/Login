package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.GoldpayAccount;

public class UserManagerTest extends BaseSpringJunit4 {

	@Autowired
	UserManager userManager;
	
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;
	
	@Test
	public void testUpdate(){
//		userManager.updateUser(2, "", "1", "");
//		userManager.updateHappyLivesVIP("123", 5);
//		System.out.println(userManager.isHappyLivesVIP(5));
		GoldpayAccount account = goldpayTrans4MergeManager.getGoldpayUserAccount(13);
		System.out.println(account.getAccountNum() + " : " + account.getBalance());
	}
}

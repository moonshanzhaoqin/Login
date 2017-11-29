package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.UserManager;

public class UserManagerTest extends BaseSpringJunit4 {

	@Autowired
	UserManager userManager;
	
	@Test
	public void testUpdate(){
//		userManager.updateUser(2, "", "1", "");
		userManager.updateHappyLivesVIP("123", 5);
		System.out.println(userManager.isHappyLivesVIP(5));
	}
}

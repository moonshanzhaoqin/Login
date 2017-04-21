package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.manager.UserManager;

public class UserManagerTest extends BaseSpringJunit4 {

	@Autowired
	UserManager userManager;
	
	@Test
	public void testRegister(){
		int userId = userManager.register("+86", "12345", "test", "test", "", "", "en_US");
		
		userManager.userFreeze(userId, ServerConsts.USER_AVAILABLE_OF_UNAVAILABLE);
	}
}

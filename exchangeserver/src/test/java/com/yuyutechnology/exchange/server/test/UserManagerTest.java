package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.UserManager;

public class UserManagerTest extends BaseSpringJunit4 {

	@Autowired
	UserManager userManager;
	
	@Test
	public void testRegister(){
		userManager.register("+86", "123", "test", "test", "", "", "en_US");
	}
}

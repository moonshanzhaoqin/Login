/**
 * 
 */
package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.WithdrawManager;

/**
 * @author silent.sun
 *
 */
public class WithdrawManagerTest extends BaseSpringJunit4 {

	@Autowired
	WithdrawManager withdrawManager;
	
	@Test
	public void testApplyConfirm(){
		withdrawManager.applyConfirm(13, 1, "silent.sun@aaa");
	}
}

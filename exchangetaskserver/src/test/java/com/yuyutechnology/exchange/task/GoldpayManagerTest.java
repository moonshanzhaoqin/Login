/**
 * 
 */
package com.yuyutechnology.exchange.task;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.GoldpayManager;

/**
 * @author silent.sun
 *
 */
public class GoldpayManagerTest extends BaseSpringJunit4{

	@Autowired
	GoldpayManager goldpayManager;
	
	@Test
	public void testCopy() {
		goldpayManager.startCopyGoldpayAccount();
	}
}

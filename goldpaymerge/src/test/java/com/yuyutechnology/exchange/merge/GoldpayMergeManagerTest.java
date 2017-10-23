/**
 * 
 */
package com.yuyutechnology.exchange.merge;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author silent.sun
 *
 */
public class GoldpayMergeManagerTest extends BaseSpringJunit4{
	
	@Autowired
	GoldpayMergeManager goldpayMergeManager;

	@Test
	public void testGetGoldpUser() {
		goldpayMergeManager.getAllGoldpayUser();
	}
}

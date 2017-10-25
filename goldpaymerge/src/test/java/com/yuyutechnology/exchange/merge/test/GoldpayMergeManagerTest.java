package com.yuyutechnology.exchange.merge.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.merge.GoldpayMergeManager;

/**
 * @author silent.sun
 *
 */
public class GoldpayMergeManagerTest extends BaseSpringJunit4 {

	@Autowired
	GoldpayMergeManager goldpayMergeManager;

	@Test
	public void testGetGoldpUser() {
		goldpayMergeManager.getAllGoldpayUser();
	}
}

/**
 * 
 */
package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.dto.MsgFlagInfo;
import com.yuyutechnology.exchange.manager.CommonManager;

/**
 * @author silent.sun
 *
 */
public class CommonManagerTest extends BaseSpringJunit4 {

	@Autowired
	CommonManager commonManager;
	
//	@Test
	public void testFlag() throws InterruptedException {
//		commonManager.addMsgFlag(7, 0);
//		commonManager.addMsgFlag(7, 1);
		MsgFlagInfo msgFlagInfo = commonManager.getMsgFlag(7);
		System.out.println("addMsgFlag : "+ msgFlagInfo.isNewTrans());
		System.out.println("addMsgFlag : " + msgFlagInfo.isNewRequestTrans());
//		commonManager.readMsgFlag(7, 1);
//		commonManager.readMsgFlag(7, 2);
		msgFlagInfo = commonManager.getMsgFlag(7);
		System.out.println("readMsgFlag : " + msgFlagInfo.isNewTrans());
		System.out.println("readMsgFlag : " + msgFlagInfo.isNewRequestTrans());
	}
}

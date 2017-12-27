/**
 * 
 */
package com.yuyutechnology.exchange.manager.impl;

import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yuyutechnology.exchange.manager.GoldpayManager;

/**
 * @author silent.sun
 *
 */
public class CopyGoldpayAccountRunnable implements Runnable{

	public static Logger logger = LogManager.getLogger(CopyGoldpayAccountRunnable.class);
	private int id;
	private int startIndex;
	private CountDownLatch begin;
	private CountDownLatch end;
	private GoldpayManager goldpayManager;
	
	public CopyGoldpayAccountRunnable(int id, int startIndex, CountDownLatch begin, CountDownLatch end, GoldpayManager goldpayManager) {
		super();
		this.id = id;
		this.startIndex = startIndex;
		this.begin = begin;
		this.end = end;
		this.goldpayManager = goldpayManager;
	}

	@Override
	public void run() {
		try {
			begin.await();
			goldpayManager.copyGoldpayAccount(startIndex);
		} catch (Exception e) {
			logger.error(
					"error CopyGoldpayAccountRunnable.run(),the id "
							+ id + "send error !!!", e);
		}
		finally
		{
			end.countDown();
		}
	}
}

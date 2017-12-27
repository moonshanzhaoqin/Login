/**
 * 
 */
package com.yuyutechnology.exchange.manager.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dao.GoldpayDAO;
import com.yuyutechnology.exchange.manager.GoldpayManager;

/**
 * @author silent.sun
 *
 */
@Service
public class GoldpayManagerImpl implements GoldpayManager {
	
	public static Logger logger = LogManager.getLogger(GoldpayManager.class);
	public final int copySize = 50;
	
	@Autowired
	GoldpayDAO goldpayDAO;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	@Override
	public void startCopyGoldpayAccount() {
		try {
			int totalCount = goldpayDAO.getGoldpayAccountTotalCount();
			int copyCount = totalCount % copySize != 0 ? totalCount / copySize + 1 :  totalCount / copySize;
			CountDownLatch begin = new CountDownLatch(1);
			CountDownLatch end = new CountDownLatch(copyCount);
			int startIndex = 0;
			for (int i = 0; i < copyCount; i++) {
				CopyGoldpayAccountRunnable runnable = new CopyGoldpayAccountRunnable(i, startIndex, begin, end, this);
				startIndex += copySize;
				executorService.execute(runnable);
			}
			begin.countDown();
			end.await();
		} catch (Exception e) {
			logger.error("startCopyGoldpayAccount error", e);
		}
	}

	@Override
	public void copyGoldpayAccount(int startIndex) {
		logger.info("copyGoldpayAccount ===="+startIndex);
		List<Map<String, Object>> accounts = goldpayDAO.getGoldpayAccountList(startIndex, copySize);
		goldpayDAO.replaceGAccount(accounts);
	}
}

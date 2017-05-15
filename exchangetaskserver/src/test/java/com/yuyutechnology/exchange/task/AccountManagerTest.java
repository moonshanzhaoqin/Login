/**
 * 
 */
package com.yuyutechnology.exchange.task;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.AccountingManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.util.DateFormatUtils;

/**
 * @author silent.sun
 *
 */
public class AccountManagerTest extends BaseSpringJunit4 {

	@Autowired
	AccountingManager accountManager;
	
	@Autowired
	ExchangeManager exchangeManager;
	
//	@Test
	public void testAccounting() {
//		Date stareDate = DateFormatUtils.fromString("2017-03-23 10:20:00", "yyyy-MM-dd HH:mm:ss");
//		Date endDate = DateFormatUtils.fromString("2017-03-24 11:20:00", "yyyy-MM-dd HH:mm:ss");
		Date startDate = DateFormatUtils.fromString("2017-03-23 12:25:00", "yyyy-MM-dd HH:mm:ss");
		Date endDate = DateFormatUtils.fromString("2017-03-23 12:50:00", "yyyy-MM-dd HH:mm:ss");
		accountManager.accounting(startDate, endDate);
	}
	
	@Test
	public void testAccountingUser() {
		accountManager.freezeUsers();
	}
	
	@Test
	public void testExchange() {
		exchangeManager.exchangeConfirm(3, "GDQ", "USD", new BigDecimal(1000));
//		exchangeManager.exchangeConfirm(15, "GDQ", "USD", new BigDecimal(1000));
//		exchangeManager.exchangeConfirm(16, "GDQ", "USD", new BigDecimal(1000));
//		exchangeManager.exchangeConfirm(17, "GDQ", "USD", new BigDecimal(1000));
//		exchangeManager.exchangeConfirm(27, "GDQ", "USD", new BigDecimal(1000));
//		exchangeManager.exchangeConfirm(45, "GDQ", "USD", new BigDecimal(1000));
//		exchangeManager.exchangeConfirm(47, "GDQ", "USD", new BigDecimal(1000));
//		exchangeManager.exchangeConfirm(57, "GDQ", "USD", new BigDecimal(1000));
//		exchangeManager.exchangeConfirm(59, "GDQ", "USD", new BigDecimal(1000));
//		exchangeManager.exchangeConfirm(60, "GDQ", "USD", new BigDecimal(1000));
		
		exchangeManager.exchangeConfirm(3, "USD", "GDQ", new BigDecimal(4));
//		exchangeManager.exchangeConfirm(15, "USD", "GDQ", new BigDecimal(4));
//		exchangeManager.exchangeConfirm(16, "USD", "GDQ", new BigDecimal(4));
//		exchangeManager.exchangeConfirm(17, "USD", "GDQ", new BigDecimal(4));
//		exchangeManager.exchangeConfirm(27, "USD", "GDQ", new BigDecimal(4));
//		exchangeManager.exchangeConfirm(45, "USD", "GDQ", new BigDecimal(4));
//		exchangeManager.exchangeConfirm(47, "USD", "GDQ", new BigDecimal(4));
//		exchangeManager.exchangeConfirm(57, "USD", "GDQ", new BigDecimal(4));
//		exchangeManager.exchangeConfirm(59, "USD", "GDQ", new BigDecimal(4));
//		exchangeManager.exchangeConfirm(60, "USD", "GDQ", new BigDecimal(4));
	}
	
}

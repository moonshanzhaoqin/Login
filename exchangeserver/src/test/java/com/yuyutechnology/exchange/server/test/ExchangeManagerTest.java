/**
 * 
 */
package com.yuyutechnology.exchange.server.test;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.server.controller.TransferController;

/**
 * @author silent.sun
 *
 */
public class ExchangeManagerTest extends BaseSpringJunit4 {

	@Autowired
	UserDAO userDAO;
	@Autowired
	ExchangeManager exchangeManager;
	@Autowired
	CommonManager commonManager;

	public static Logger logger = LogManager.getLogger(TransferController.class);

	@Test
	public void testRate() {
		List<String[]> instruments = commonManager.getInstruments();
		for (String[] strings : instruments) {
			 exchangeManager.exchangeCalculation(strings[0].split("_")[0],
			 strings[0].split("_")[1], new BigDecimal("100"));
			 exchangeManager.exchangeCalculation(strings[1].split("_")[0],
			 strings[1].split("_")[1], new BigDecimal("100"));
		}

		List<Currency> cs = commonManager.getAllCurrencies();
		for (Currency currency : cs) {
			if (!currency.getCurrency().equals("GDQ")) {
				exchangeManager.exchangeCalculation(currency.getCurrency(), "GDQ", new BigDecimal("10000"));
				exchangeManager.exchangeCalculation("GDQ", currency.getCurrency(), new BigDecimal("1"));
			}
		}
	}
	
	@Test
	public void testExchange() {
		exchangeManager.exchangeConfirm(15, "GDQ", "USD", new BigDecimal(1000));
		exchangeManager.exchangeConfirm(99, "GDQ", "USD", new BigDecimal(1000));
		exchangeManager.exchangeConfirm(106, "GDQ", "USD", new BigDecimal(1000));
		exchangeManager.exchangeConfirm(113, "GDQ", "USD", new BigDecimal(1000));
		exchangeManager.exchangeConfirm(182, "GDQ", "USD", new BigDecimal(1000));
		exchangeManager.exchangeConfirm(310, "GDQ", "USD", new BigDecimal(1000));
		exchangeManager.exchangeConfirm(324, "GDQ", "USD", new BigDecimal(1000));
		exchangeManager.exchangeConfirm(394, "GDQ", "USD", new BigDecimal(1000));
		exchangeManager.exchangeConfirm(408, "GDQ", "USD", new BigDecimal(1000));
		exchangeManager.exchangeConfirm(415, "GDQ", "USD", new BigDecimal(1000));
		
		exchangeManager.exchangeConfirm(15, "USD", "GDQ", new BigDecimal(4));
		exchangeManager.exchangeConfirm(99, "USD", "GDQ", new BigDecimal(4));
		exchangeManager.exchangeConfirm(106, "USD", "GDQ", new BigDecimal(4));
		exchangeManager.exchangeConfirm(113, "USD", "GDQ", new BigDecimal(4));
		exchangeManager.exchangeConfirm(182, "USD", "GDQ", new BigDecimal(4));
		exchangeManager.exchangeConfirm(310, "USD", "GDQ", new BigDecimal(4));
		exchangeManager.exchangeConfirm(324, "USD", "GDQ", new BigDecimal(4));
		exchangeManager.exchangeConfirm(394, "USD", "GDQ", new BigDecimal(4));
		exchangeManager.exchangeConfirm(408, "USD", "GDQ", new BigDecimal(4));
		exchangeManager.exchangeConfirm(415, "USD", "GDQ", new BigDecimal(4));
	}

	public static void main(String[] args) {
		System.out.println(new BigDecimal("1").divide(new BigDecimal("3"), 8, BigDecimal.ROUND_UP)
				.multiply(new BigDecimal("2")));
		System.out.println(new BigDecimal("1")
				.multiply(new BigDecimal("2").divide(new BigDecimal("3"), 8, BigDecimal.ROUND_UP)));
	}
}

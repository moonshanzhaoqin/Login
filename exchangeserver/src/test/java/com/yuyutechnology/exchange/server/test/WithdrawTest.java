package com.yuyutechnology.exchange.server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.manager.FeeManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.WithdrawManager;

public class WithdrawTest extends BaseSpringJunit4 {
	@Autowired
	FeeManager feeManager;
	@Autowired
	OandaRatesManager oandaRatesManager;
	@Autowired
	WithdrawManager withdrawManager;


	@Test
	public void calculateTest() {
//		System.out.println(oandaRatesManager.getExchangedAmount(ServerConsts.CURRENCY_OF_GOLD, new BigDecimal("1"),
//				ServerConsts.CURRENCY_OF_GOLDPAY));
	}

}

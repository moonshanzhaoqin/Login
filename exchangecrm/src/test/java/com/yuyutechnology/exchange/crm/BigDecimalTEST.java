package com.yuyutechnology.exchange.crm;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BigDecimalTEST extends BaseSpringJunit4 {
	public static Logger logger = LoggerFactory.getLogger(AdminTest.class);

	@Test
	public void divideTEST() {
		BigDecimal bigDecimal =(new BigDecimal("1").subtract(
				(new BigDecimal("0.0387").subtract(new BigDecimal("0.0387"))).divide(new BigDecimal("0.0038"), 5, RoundingMode.DOWN)))
						.multiply(new BigDecimal("100"));
		System.out.println(bigDecimal);
	}
}

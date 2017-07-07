package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;

public interface TransDetailsManager {

	void addTransDetails(String transferId, Integer payerId, Integer traderId, String traderName, String traderAreaCode,
			String traderPhone, String transCurrency, BigDecimal transAmount, String transRemarks, Integer transType);

}

package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;

public interface PayPalTransManager {

	public HashMap<String, Object> paypalTransInit(Integer userId,String currencyLeft,BigDecimal amount);
	
	public HashMap<String, String> paypalTransConfirm(Integer userId,String transId,String nonce);
	
}

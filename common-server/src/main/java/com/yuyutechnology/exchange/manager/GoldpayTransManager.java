package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;

public interface GoldpayTransManager {
	
	public HashMap<String, String> goldpayPurchase(int userId,String goldpayAccount,BigDecimal amount);

}
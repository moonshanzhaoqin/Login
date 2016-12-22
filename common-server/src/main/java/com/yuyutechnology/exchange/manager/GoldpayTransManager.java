package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;

public interface GoldpayTransManager {
	
	public HashMap<String, String> goldpayPurchase(int userId,BigDecimal amount);
	
	public HashMap<String, String> requestPin(int userId,String transferId);
	
	public HashMap<String, String> goldpayTransConfirm(int userId,String pin,String transferId);
	
	public HashMap<String, String> goldpayWithdraw(int userId,double amount);
	
	public HashMap<String, String> withdrawConfirm(int userId,String payPwd,String transferId);

}

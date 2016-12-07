package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;

public interface TransferManager {
	
	public String transferInitiate(int userId,String areaCode,
			String userPhone,String currency,BigDecimal amount,String transferComment);
	
	public String payPwdConfirm(int userId,String transferId,String userPayPwd);
	
	public void transferConfirm(String transferId);
	

}

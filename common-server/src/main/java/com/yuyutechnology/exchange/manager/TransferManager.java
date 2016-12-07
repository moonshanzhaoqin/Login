package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;

public interface TransferManager {
	
	/**
	 * @Descrition : 交易初始化
	 * @author : nicholas.chi
	 * @time : 2016年12月7日 下午3:17:19
	 * @param userId
	 * @param areaCode
	 * @param userPhone
	 * @param currency
	 * @param amount
	 * @param transferComment
	 * @return
	 */
	public String transferInitiate(int userId,String areaCode,
			String userPhone,String currency,BigDecimal amount,String transferComment);
	
	/**
	 * @Descrition : 支付密码验证
	 * @author : nicholas.chi
	 * @time : 2016年12月7日 下午3:17:22
	 * @param userId
	 * @param transferId
	 * @param userPayPwd
	 * @return
	 */
	public String payPwdConfirm(int userId,String transferId,String userPayPwd);
	
	/**
	 * @Descrition : 交易确认
	 * @author : nicholas.chi
	 * @time : 2016年12月7日 下午3:17:25
	 * @param transferId
	 */
	public void transferConfirm(String transferId);
	

}

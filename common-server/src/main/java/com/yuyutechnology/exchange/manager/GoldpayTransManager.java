package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.pojo.Transfer;

public interface GoldpayTransManager {

	public HashMap<String, String> goldpayPurchase(int userId, BigDecimal amount);

	public HashMap<String, String> requestPin(int userId, String transferId);

	public HashMap<String, String> goldpayTransConfirm(int userId, String pin, String transferId);

	public HashMap<String, String> goldpayWithdraw(int userId, double amount);

//	public HashMap<String, String> withdrawConfirm(int userId, String payPwd, String transferId);

	/**
	 * 提现第一步：exanytime划账
	 * 
	 * @param userId
	 * @param payPwd
	 * @param transferId
	 * @return
	 */

	public HashMap<String, String> withdrawConfirm1(int userId, String payPwd, String transferId);

	/**
	 * 提现第二步： goldpay划账
	 * 
	 * @param userId
	 * @param transferId
	 * @return
	 */
	public HashMap<String, String> withdrawConfirm2(int userId, String transferId);

	public void withdrawRefund(int userId, String transferId, String transferCurrency, BigDecimal transferAmount);

	public List<Transfer> findGoldpayWithdrawByTimeBefore(Date date);

}

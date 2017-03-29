package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.dto.WithdrawDetail;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.Withdraw;
import com.yuyutechnology.exchange.utils.page.PageBean;

public interface GoldpayTransManager {

	public HashMap<String, String> goldpayPurchase(int userId, BigDecimal amount);

	public HashMap<String, String> requestPin(int userId, String transferId);

	public HashMap<String, String> goldpayTransConfirm(int userId, String pin, String transferId);

	public HashMap<String, String> goldpayWithdraw(int userId, double amount);

	// public HashMap<String, String> withdrawConfirm(int userId, String payPwd,
	// String transferId);

	/**
	 * 提现第一步：exanytime划账
	 * 
	 * @param userId
	 * @param payPwd
	 * @param transferId
	 * @return
	 */

	public HashMap<String, String> withdrawConfirm1(int userId, String payPwd, String transferId);

	// public void withdrawRefund(int userId, String transferId, String
	// transferCurrency, BigDecimal transferAmount);

	public List<Transfer> findGoldpayWithdrawByTimeBefore(Date date);

	// public WithdrawDetail getWithdrawDetail(Integer withdrawId);

	public List<Transfer> getNeedGoldpayRemitWithdraws();

	public List<Transfer> getNeedReviewWithdraws();

	public PageBean getWithdrawRecordByPage(Integer userId, int currentPage, int pageSize);

	HashMap<String, String> goldpayRemit(String transferId);

	public PageBean getWithdrawList(int currentPage, String userPhone, String transferId, String transferStatus);

	void withdrawRefund(String transferId);

	void withdrawReviewManual(String transferId);

	void withdrawReviewAuto(String transferId);

}

package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.util.page.PageBean;

public interface GoldpayTransManager {

	HashMap<String, String> goldpayPurchase(int userId, BigDecimal amount);

	HashMap<String, String> requestPin(int userId, String transferId);

	HashMap<String, String> goldpayTransConfirm(int userId, String pin, String transferId);

	HashMap<String, String> goldpayWithdraw(int userId, double amount);

	/**
	 * 提现第一步：exanytime划账
	 * 
	 * @param userId
	 * @param payPwd
	 * @param transferId
	 * @return
	 */
	HashMap<String, String> withdrawConfirm1(int userId, String payPwd, String transferId);

	List<Transfer> getNeedGoldpayRemitWithdraws();

	List<Transfer> getNeedReviewWithdraws();

	PageBean getWithdrawRecordByPage(Integer userId, int currentPage, int pageSize);

	HashMap<String, String> goldpayRemit(String transferId);

	PageBean getWithdrawList(int currentPage, String userPhone, String transferId, String[] transferStatus);

	void withdrawRefund(String transferId);

	// void withdrawReviewManual(String transferId);

	void forbiddenGoldpayRemitWithdraws(String forbidden);

	boolean getGoldpayRemitWithdrawsforbidden();

	void withdrawReviewAuto(String transferId);

	void withdrawReviewPending(String transferId);

	void goldpayRemitPending(String transferId);

}

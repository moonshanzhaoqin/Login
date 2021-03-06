package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;

import com.yuyutechnology.exchange.dto.TransDetailsDTO;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.util.page.PageBean;

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
	HashMap<String, String> transferInitiate(int userId, String areaCode, String userPhone, String currency,
			BigDecimal amount, String transferComment, int noticeId);

	/**
	 * @Descrition : 支付密码验证
	 * @author : nicholas.chi
	 * @time : 2016年12月7日 下午3:17:22
	 * @param userId
	 * @param transferId
	 * @param userPayPwd
	 * @return
	 */
	HashMap<String, String> payPwdConfirm(int userId, String transferId, String userPayPwd);

	/**
	 * @Descrition : 交易确认
	 * @author : nicholas.chi
	 * @time : 2016年12月7日 下午3:17:25
	 * @param transferId
	 */
	String transferConfirm(int userId, String transferId);


	/**
	 * @Descrition : 批量系统退款
	 * @author : nicholas.chi
	 * @time : 2016年12月8日 下午5:25:58
	 */
//	void systemRefundBatch();

	/**
	 * @Descrition :
	 * @author : nicholas.chi
	 * @time : 2016年12月15日 下午6:09:28
	 * @param period
	 * @param userId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	HashMap<String, Object> getTransactionRecordByPage(String period, String type, int userId, int currentPage,
			int pageSize);

	/**
	 * @Descrition :
	 * @author : nicholas.chi
	 * @time : 2016年12月16日 上午10:12:19
	 * @param userId
	 * @param payerAreaCode
	 * @param payerPhone
	 * @param currency
	 * @param amount
	 * @return
	 */
	HashMap<String, Object> makeRequest(int userId, String payerAreaCode, String payerPhone, String currency,
			BigDecimal amount);

	/**
	 * @Descrition :
	 * @author : nicholas.chi
	 * @time : 2016年12月20日 下午3:59:04
	 * @param userId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	HashMap<String, Object> getNotificationRecordsByPage(int userId, int currentPage, int pageSize);

	/**
	 * @Descrition :
	 * @author : nicholas.chi
	 * @time : 2016年12月23日 上午10:37:31
	 * @param userId
	 * @param areaCode
	 * @param userPhone
	 * @param currency
	 * @param amount
	 * @param transferComment
	 * @param noticeId
	 * @return
	 */
	HashMap<String, String> respond2Request(int userId, String areaCode, String userPhone, String currency,
			BigDecimal amount, String transferComment, int noticeId);

	HashMap<String, String> regenerateQRCode(String currency, BigDecimal amount);

	/**
	 * 获取交易详情 for crm
	 * 
	 * @param transferId
	 * @return
	 */
	Object getTransfer(String transferId);

	TransDetailsDTO getTransDetails(String transferId, int userId);

	String updateSystemPhone(String transferId, String phoneNum);

	HashMap<String, Object> getTransactionRecord(String period, String type, int userId, int currentPage, int pageSize);

	BigDecimal getAccumulatedAmount(String key);

	HashMap<String, Object> getTransactionRecordByPageNew(String period, String type, int userId, int currentPage,
			int pageSize);

	HashMap<String, Object> getTransactionRecordNew(String period, String type, int userId, int currentPage,
			int pageSize);

	PageBean getRechargeList(int currentPage, String userPhone, String lowerAmount, String upperAmount,
			String startTime, String endTime, String transferType) throws ParseException;

	HashMap<String, String> transConfirm4TPPS(int userId, String transferId, String userPayPwd);
	
	HashMap<String, String> transferInitiate(int payerId, int payeeId, String currency,
			BigDecimal amount, String transferComment, int noticeId);

	String systemRefundStep1(Unregistered unregistered);

	void systemRefundStep2(String transferId, Unregistered unregistered);

}

package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;

import com.yuyutechnology.exchange.pojo.Unregistered;

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
	public HashMap<String, String> transferInitiate(int userId,String areaCode,
			String userPhone,String currency,BigDecimal amount,String transferComment,int noticeId);
	
	/**
	 * @Descrition : 支付密码验证
	 * @author : nicholas.chi
	 * @time : 2016年12月7日 下午3:17:22
	 * @param userId
	 * @param transferId
	 * @param userPayPwd
	 * @return
	 */
//	public String payPwdConfirm(int userId,String transferId,String userPayPwd);
	public HashMap<String, String> payPwdConfirm(int userId,String transferId,String userPayPwd);
	
	/**
	 * @Descrition : 交易确认
	 * @author : nicholas.chi
	 * @time : 2016年12月7日 下午3:17:25
	 * @param transferId
	 */
	public String transferConfirm(int userId,String transferId);
	
	/**
	 * @Descrition : 系统退款
	 * @author : nicholas.chi
	 * @time : 2016年12月8日 下午6:03:32
	 * @param unregistered
	 */
	public void systemRefund(Unregistered unregistered);
	
	/**
	 * @Descrition : 批量系统退款
	 * @author : nicholas.chi
	 * @time : 2016年12月8日 下午5:25:58
	 */
	public void systemRefundBatch();
	
	
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
	public HashMap<String, Object> getTransactionRecordByPage(String period,String type, int userId,int currentPage, int pageSize);
	
	
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
	public String makeRequest(int userId,String payerAreaCode,String payerPhone,String currency,BigDecimal amount);
	
	/**
	 * @Descrition : 
	 * @author : nicholas.chi
	 * @time : 2016年12月20日 下午3:59:04
	 * @param userId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public HashMap<String, Object> getNotificationRecordsByPage(int userId,int currentPage, int pageSize);

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
	public HashMap<String, String> respond2Request(int userId, String areaCode, String userPhone, String currency,
			BigDecimal amount, String transferComment, int noticeId);
	
	
	public HashMap<String, String> regenerateQRCode(String currency,BigDecimal amount);
	
	public HashMap<String, Object> getTransDetails(String transferId,int userId);
	
}

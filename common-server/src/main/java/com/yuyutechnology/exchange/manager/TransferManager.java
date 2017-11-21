package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;

import com.yuyutechnology.exchange.dto.TransDetailsDTO;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.util.page.PageBean;

public interface TransferManager {

	HashMap<String, String> transferInitiate(Integer userId, String areaCode, String userPhone, String currency,
			BigDecimal bigDecimal, String transferComment, int i);

	HashMap<String, String> whenPayPwdConfirmed(Integer userId, String transferId, String userPayPwd);

	String transferConfirm(Integer userId, String transferId);

	HashMap<String, Object> makeRequest(Integer userId, String areaCode, String phone, String currency,
			BigDecimal bigDecimal);

	HashMap<String, String> regenerateQRCode(String currency, BigDecimal amount);

	HashMap<String, String> respond2Request(Integer userId, String areaCode, String userPhone, String currency,
			BigDecimal bigDecimal, String transferComment, int noticeId);

	HashMap<String, Object> getTransRecordbyPage(String period, String type, Integer userId, int currentPage,
			int pageSize);

	HashMap<String, Object> getNotificationRecordsByPage(Integer userId, int currentPage, int pageSize);

	TransDetailsDTO getTransDetails(String transferId, Integer userId);

	HashMap<String, String> transInit4ThirdParty(Boolean isRestricted,int payerId, int payeeId, String currency, BigDecimal amount,
			String transferComment, Boolean isFeeDeduction, BigDecimal fee, int feepayerId);

	HashMap<String, String> transConfirm4ThirdParty(Boolean isRestricted,int userId, String transferId, String userPayPwd);

	BigDecimal getAccumulatedAmount(String key);

	Object getTransfer(String transferId);

	PageBean getRechargeList(int parseInt, String userPhone, String lowerAmount, String upperAmount, String startTime,
			String endTime, String transferType);

	String systemRefundStep1(Unregistered unregistered);

	void systemRefundStep2(String transferId, Unregistered unregistered);

	
}

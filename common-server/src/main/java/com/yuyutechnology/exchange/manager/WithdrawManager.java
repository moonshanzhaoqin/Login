package com.yuyutechnology.exchange.manager;

import com.yuyutechnology.exchange.dto.WithdrawCalResult;
import com.yuyutechnology.exchange.util.page.PageBean;

public interface WithdrawManager {

	PageBean getWithdrawByPage(int currentPage, String userPhone, String userName, String startTime, String endTime);

	Integer applyConfirm(Integer userId, int goldBullion,String userEmail);

	String cancelWithdraw(Integer withdrawId, String adminName);

	String finishWithdraw(Integer withdrawId, String adminName);

	WithdrawCalResult withdrawCalculate(Integer userId, int goldBullion);

	void goldpayTrans4Apply(Integer withdrawId);

	void goldpayTrans4Handle(Integer withdrawId);

	void notifyWithdraw(Integer userId, int goldBullion);

}

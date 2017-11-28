package com.yuyutechnology.exchange.manager;

import java.util.List;

import com.yuyutechnology.exchange.dto.WithdrawCalResult;
import com.yuyutechnology.exchange.dto.WithdrawDTO;
import com.yuyutechnology.exchange.util.page.PageBean;

public interface WithdrawManager {
	/**
	 * 计算N根金条需要的GDQ和手续费
	 * @param userId
	 * @param goldBullion
	 * @return
	 */
	WithdrawCalResult withdrawCalculate(Integer userId, int goldBullion);

	String applyConfirm(Integer userId, int goldBullion, String userEmail);

	String goldpayTrans4Apply(String withdrawId);

	String cancelWithdraw(String withdrawId);

	String goldpayTrans4cancel(String withdrawId, String adminName);

	String finishWithdraw(String withdrawId);

	String goldpayTrans4finish(String withdrawId, String adminName);

	PageBean getWithdrawByPage(int currentPage, String userPhone, String userName, String startTime, String endTime);

	List<WithdrawDTO> getWithdrawRecord(Integer userId);

}

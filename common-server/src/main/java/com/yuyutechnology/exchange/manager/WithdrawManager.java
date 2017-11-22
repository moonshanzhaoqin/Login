package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.Map;

import com.yuyutechnology.exchange.dto.WithdrawCalResult;
import com.yuyutechnology.exchange.pojo.Admin;
import com.yuyutechnology.exchange.util.page.PageBean;

public interface WithdrawManager {

	PageBean getWithdrawByPage(int currentPage, String userPhone, String userName, String startTime, String endTime);

	String applyConfirm(Integer userId, int goldBullion);

	String cancelWithdraw(Integer withdrawId, String adminName);

	String finishWithdraw(Integer withdrawId, String adminName);

	WithdrawCalResult withdrawCalculate(Integer userId, int goldBullion);

}

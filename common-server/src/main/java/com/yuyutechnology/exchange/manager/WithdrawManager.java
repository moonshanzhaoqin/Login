package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.Map;

import com.yuyutechnology.exchange.util.page.PageBean;

public interface WithdrawManager {


	Map<String, BigDecimal> goldBullion2Goldpay(Integer userId, int goldBullion);

	PageBean getWithdrawByPage(int currentPage, String userPhone, String userName, String startTime, String endTime);

	void applyConfirm(Integer userId, int goldBullion);

}

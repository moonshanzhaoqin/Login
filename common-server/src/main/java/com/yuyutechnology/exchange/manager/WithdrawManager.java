package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.Map;

public interface WithdrawManager {

//	BigDecimal goldBullion2Goldpay(int goldBullion);

	Map<String, BigDecimal> goldBullion2Goldpay(Integer userId, int goldBullion);

}

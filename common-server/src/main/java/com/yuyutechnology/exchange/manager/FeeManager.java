package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;

import com.yuyutechnology.exchange.FeePurpose;

public interface FeeManager {

	void refresh();




	BigDecimal figureOutFee(FeePurpose feePurpose, BigDecimal amount);

}

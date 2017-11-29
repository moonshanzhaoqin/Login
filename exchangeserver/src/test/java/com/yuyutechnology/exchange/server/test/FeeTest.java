package com.yuyutechnology.exchange.server.test;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.enums.FeePurpose;
import com.yuyutechnology.exchange.manager.FeeManager;

public class FeeTest extends BaseSpringJunit4 {
	@Autowired
	FeeManager feeManager;

	 @Test
	 public void figureOutFeeTest() {
	 feeManager.figureOutFee(FeePurpose.PayPal_Purchase_GoldBullion_Ordinary, new
	 BigDecimal("50"));
	 feeManager.figureOutFee(FeePurpose.PayPal_Purchase_GoldBullion_Ordinary, new
	 BigDecimal("100"));
	 feeManager.figureOutFee(FeePurpose.PayPal_Purchase_GoldBullion_Ordinary, new
	 BigDecimal("200"));
	 feeManager.figureOutFee(FeePurpose.PayPal_Purchase_GoldBullion_Ordinary, new
	 BigDecimal("1000"));
	 feeManager.figureOutFee(FeePurpose.PayPal_Purchase_GoldBullion_Ordinary, new
	 BigDecimal("2000"));
	 feeManager.figureOutFee(FeePurpose.PayPal_Purchase_GoldBullion_Ordinary, new
	 BigDecimal("100000"));
	 feeManager.figureOutFee(FeePurpose.PayPal_Purchase_GoldBullion_Ordinary, new
	 BigDecimal("200000"));
	 }


}

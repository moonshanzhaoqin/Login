package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.List;

import com.yuyutechnology.exchange.dto.FeeResult;
import com.yuyutechnology.exchange.enums.FeePurpose;
import com.yuyutechnology.exchange.pojo.FeeTemplate;

public interface FeeManager {

	void refresh();

	FeeResult figureOutFee(FeePurpose feePurpose, BigDecimal amount);

	List<FeeTemplate> listAllFeeTemplate();

	void updateFeeTemplate(FeeTemplate feeTemplate);

}

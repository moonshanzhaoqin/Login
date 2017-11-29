package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.FeeTemplate;

public interface FeeTemplateDAO {

	void updateFeeTemplate(FeeTemplate feeTemplate);

	List<FeeTemplate> listAllFeeTemplate();

}

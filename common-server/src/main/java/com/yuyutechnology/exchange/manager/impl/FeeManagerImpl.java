package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dao.FeeTemplateDAO;
import com.yuyutechnology.exchange.dto.FeeResult;
import com.yuyutechnology.exchange.enums.FeePurpose;
import com.yuyutechnology.exchange.manager.FeeManager;
import com.yuyutechnology.exchange.pojo.FeeTemplate;
import com.yuyutechnology.exchange.util.ResourceUtils;

@Service
public class FeeManagerImpl implements FeeManager {
	private static Logger logger = LogManager.getLogger(FeeManagerImpl.class);

	@Autowired
	FeeTemplateDAO feeTemplateDAO;

	Map<String, FeeTemplate> feeTmeplateMap = new HashMap<String, FeeTemplate>();
	List<FeeTemplate> feeTemplateList = new ArrayList<FeeTemplate>();

	@Override
	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void refresh() {
		ResourceUtils.clearCache();
		init();
	}

	private void init() {
		feeTemplateList = feeTemplateDAO.listAllFeeTemplate();
		for (FeeTemplate feeTemplate : feeTemplateList) {
			feeTmeplateMap.put(feeTemplate.getFeePurpose().trim(), feeTemplate);
		}
	}

	@Override
	public List<FeeTemplate> listAllFeeTemplate() {
		return feeTemplateList;
	}
	@Override
	public void updateFeeTemplate(FeeTemplate feeTemplate) {
		feeTemplateDAO.updateFeeTemplate(feeTemplate);
	}

	@Override
	public FeeResult figureOutFee(FeePurpose feePurpose, BigDecimal amount) {
		FeeResult feeResult=new FeeResult();
		FeeTemplate feeTemplate = feeTmeplateMap.get(feePurpose.getPurpose());
		logger.info("{} aoumout={} -->", feeTemplate.toString(), amount);
		
		String formula="exempt_amount:" + feeTemplate.getExemptAmount() + ";min_fee:" + feeTemplate.getMinFee()
		+ ";max_fee:" + feeTemplate.getMaxFee() + ";formula:ceiling((" + amount.toString() + "-"
		+ feeTemplate.getExemptAmount() + ")*" + feeTemplate.getFeePercent() + ")";
		logger.info(formula);
		
		BigDecimal fee = BigDecimal.ZERO;
		if (amount.compareTo(feeTemplate.getExemptAmount()) <= 0) {
			logger.info("the amout({}) is not more than exempt_amount({})", amount, feeTemplate.getExemptAmount());
		} else {
			fee = (amount.subtract(feeTemplate.getExemptAmount())).multiply(feeTemplate.getFeePercent()).setScale(0, RoundingMode.CEILING);
			if (fee.compareTo(feeTemplate.getMinFee()) < 0) {
				logger.info("the fee({}) is less than min_fee({})", fee, feeTemplate.getMinFee());
				fee = feeTemplate.getMinFee();
			} else if (fee.compareTo(feeTemplate.getMaxFee()) > 0) {
				logger.info("the fee({}) is more than max_fee({})", fee, feeTemplate.getMaxFee());
				fee = feeTemplate.getMaxFee();
			}
		}
		logger.info("fee is {}", fee);
		feeResult.setFee(fee);
		feeResult.setFormula(formula);
		return feeResult;
	}

	

}

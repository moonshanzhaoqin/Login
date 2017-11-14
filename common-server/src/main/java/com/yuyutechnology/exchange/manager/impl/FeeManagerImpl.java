package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.FeePurpose;
import com.yuyutechnology.exchange.dao.FeeTemplateDAO;
import com.yuyutechnology.exchange.manager.FeeManager;
import com.yuyutechnology.exchange.pojo.FeeTemplate;

@Service
public class FeeManagerImpl implements FeeManager {
	private static Logger logger = LogManager.getLogger(FeeManagerImpl.class);

	@Autowired
	FeeTemplateDAO feeTemplateDAO;

	Map<String, FeeTemplate> feeTmeplateMap = new HashMap<String, FeeTemplate>();

	@Override
	@PostConstruct
	@Scheduled(cron = "0 1/10 * * * ?")
	public void refresh() {
		init();
	}

	private void init() {
		List<FeeTemplate> feeTemplates = feeTemplateDAO.listAllFeeTemplate();
		for (FeeTemplate feeTemplate : feeTemplates) {
			feeTmeplateMap.put(feeTemplate.getFeePurpose().trim(), feeTemplate);
		}
	}

	@Override
	public BigDecimal figureOutFee(FeePurpose feePurpose, BigDecimal amount) {
		FeeTemplate feeTemplate = feeTmeplateMap.get(feePurpose.getPurpose());
		logger.info("{} aoumout={} -->", feeTemplate.toString(), amount);
		BigDecimal fee = BigDecimal.ZERO;
		if (amount.compareTo(feeTemplate.getExemptAmount()) <= 0) {
			logger.info("the amout({}) is not more than exempt_amount({})", amount, feeTemplate.getExemptAmount());
		} else {
			fee = amount.multiply(feeTemplate.getFeePercent()).setScale(0, RoundingMode.CEILING);
			if (fee.compareTo(feeTemplate.getMinFee()) < 0) {
				logger.info("the fee({}) is less than min_fee({})", fee, feeTemplate.getMinFee());
				fee = feeTemplate.getMinFee();
			} else if (fee.compareTo(feeTemplate.getMaxFee()) > 0) {
				logger.info("the fee({}) is more than min_fee({})", fee, feeTemplate.getMaxFee());
				fee = feeTemplate.getMaxFee();
			}
		}
		logger.info("fee is {}", fee);
		return fee;
	}

}

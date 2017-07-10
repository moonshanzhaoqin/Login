package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CampaignDAO;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.manager.CampaignManager;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.CurrencyManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.pojo.Campaign;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Wallet;

@Service
public class CampaignManagerImpl implements CampaignManager {

	private static Logger logger = LogManager.getLogger(CampaignManagerImpl.class);
@Autowired
CampaignDAO campaignDAO;

	@Autowired
	WalletDAO walletDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CommonManager commonManager;


	
	
	@Override
	public void addCampaign(Date startTime, Date endTime, BigDecimal campaignBudget, BigDecimal inviterBonus,
			BigDecimal inviteeBonus) {
	 	campaignDAO.updateCampaign(new Campaign(startTime, endTime, ServerConsts.CAMPAIGN_STATUS_OFF, campaignBudget,
				campaignBudget, inviterBonus, inviteeBonus, new Date(), new Date()));
	 	return;
	}

	
	
	
	
	
	
	
	
	
	
}

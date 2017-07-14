package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.CampaignDAO;
import com.yuyutechnology.exchange.pojo.Campaign;

@Repository
public class CampaignDAOImpl implements CampaignDAO {

	@Resource
	HibernateTemplate hibernateTemplate;

	public static Logger logger = LogManager.getLogger(CampaignDAOImpl.class);

	@Override
	public Campaign getCampaign(Integer campaignId) {
		return hibernateTemplate.get(Campaign.class, campaignId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Campaign> getCampaigns() {
		List<?> list = hibernateTemplate.find("from Campaign");
		return list.isEmpty() ? null : (List<Campaign>) list;
	}

	@Override
	public void updateCampaign(Campaign campaign) {
		hibernateTemplate.saveOrUpdate(campaign);
	}
	
}
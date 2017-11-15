package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.CampaignDAO;
import com.yuyutechnology.exchange.pojo.Campaign;
import com.yuyutechnology.exchange.util.page.PageBean;
import com.yuyutechnology.exchange.util.page.PageUtils;

@Repository
public class CampaignDAOImpl implements CampaignDAO {

	@Resource
	HibernateTemplate hibernateTemplate;

	public static Logger logger = LogManager.getLogger(CampaignDAOImpl.class);

	@Override
	public Campaign getCampaign(Integer campaignId) {
		return hibernateTemplate.get(Campaign.class, campaignId);
	}

	@Override
	public PageBean getCampaignsByPage(String hql, List<Object> values, int currentPage, int pageSize) {
		return PageUtils.getPageContent(hibernateTemplate, hql.toString(), values, currentPage, pageSize);
	}

	@Override
	public void updateCampaign(Campaign campaign) {
		hibernateTemplate.saveOrUpdate(campaign);
	}

}

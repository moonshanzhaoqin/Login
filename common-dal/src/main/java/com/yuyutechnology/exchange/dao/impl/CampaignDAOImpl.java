package com.yuyutechnology.exchange.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
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

	@Override
	public Integer updateCampaignSurplus(final Integer campaignId, final BigDecimal inviterBonus,
			final BigDecimal inviteeBonus) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(
						"update Campaign set budgetSurplus = budgetSurplus - :inviterBonus - :inviteeBonus, updateTime = :date where campaignId = :campaignId ");
				query.setBigDecimal("inviterBonus", inviterBonus);
				query.setBigDecimal("inviteeBonus", inviteeBonus);
				query.setDate("date", new Date());
				query.setInteger("campaignId", campaignId);
				return query.executeUpdate();
			}
		});
	}

}

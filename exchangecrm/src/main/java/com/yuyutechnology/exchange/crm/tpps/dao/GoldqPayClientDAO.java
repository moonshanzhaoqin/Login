package com.yuyutechnology.exchange.crm.tpps.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayClient;
import com.yuyutechnology.exchange.util.page.PageBean;
import com.yuyutechnology.exchange.util.page.PageUtils;

@Repository
public class GoldqPayClientDAO {
	@Resource
	HibernateTemplate hibernateTemplateTPPS;

	public Long saveGoldqPayClient(GoldqPayClient goldqPayClient) {
		return (Long) hibernateTemplateTPPS.save(goldqPayClient);
	}

	public PageBean getGoldqPayClientByPage(String hql, List<Object> values, int currentPage, int pageSize) {
		return PageUtils.getPageContent(hibernateTemplateTPPS, hql.toString(), values, currentPage, pageSize);
	}

	public void updateGoldqPayClient(GoldqPayClient goldqPayClient) {
		hibernateTemplateTPPS.update(goldqPayClient);
	}

}

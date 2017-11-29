package com.yuyutechnology.exchange.crm.tpps.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayClient;
import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayFee;
import com.yuyutechnology.exchange.util.page.PageBean;
import com.yuyutechnology.exchange.util.page.PageUtils;

@Repository
public class TppsDAO {
	@Resource
	HibernateTemplate hibernateTemplateTPPS;

	public Integer saveGoldqPayFee(GoldqPayFee goldqPayFee) {
		return (Integer) hibernateTemplateTPPS.save(goldqPayFee);
	}

	public void updateGoldqPayFee(GoldqPayFee goldqPayFee) {
		hibernateTemplateTPPS.update(goldqPayFee);
	}

	@SuppressWarnings("unchecked")
	public List<GoldqPayFee> getGoldqPayFeeByClientId(String clientId) {
		return (List<GoldqPayFee>) hibernateTemplateTPPS.find("from GoldqPayFee where clientId = ?", clientId);
	}

	public Long saveGoldqPayClient(GoldqPayClient goldqPayClient) {
		return (Long) hibernateTemplateTPPS.save(goldqPayClient);
	}

	public PageBean getGoldqPayClientByPage(String hql, List<Object> values, int currentPage, int pageSize) {
		return PageUtils.getPageContent(hibernateTemplateTPPS, hql.toString(), values, currentPage, pageSize);
	}

	public void updateGoldqPayClient(GoldqPayClient goldqPayClient) {
		hibernateTemplateTPPS.update(goldqPayClient);
	}

	public GoldqPayClient getGoldqPayClientByClientId(String clientId) {
		List<?> list = hibernateTemplateTPPS.find("from GoldqPayClient where clientId = ?", clientId);
		return list.isEmpty() ? null : (GoldqPayClient) list.get(0);

	}

}

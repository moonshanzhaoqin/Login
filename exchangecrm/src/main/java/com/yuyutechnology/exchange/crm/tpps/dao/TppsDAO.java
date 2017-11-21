package com.yuyutechnology.exchange.crm.tpps.dao;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayClient;
import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayFee;

@Repository
public class TppsDAO {
	@Resource
	HibernateTemplate hibernateTemplateTPPS;

	public Integer saveFeeTemplate(GoldqPayFee goldqPayFee) {
		return (Integer) hibernateTemplateTPPS.save(goldqPayFee);
	}

	public void updateFeeTemplate(GoldqPayFee goldqPayFee) {
		hibernateTemplateTPPS.update(goldqPayFee);
	}

	public Long name(GoldqPayClient goldqPayClient) {
		return (Long) hibernateTemplateTPPS.save(goldqPayClient);
	}

}

package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.FeeTemplateDAO;
import com.yuyutechnology.exchange.pojo.FeeTemplate;


@Repository
public class FeeTemplateDAOImpl implements FeeTemplateDAO {
	@Resource
	HibernateTemplate hibernateTemplate;
	@Override
	public void updateFeeTemplate(FeeTemplate feeTemplate){
		hibernateTemplate.update(feeTemplate);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FeeTemplate> listAllFeeTemplate() {
		return (List<FeeTemplate>) hibernateTemplate.find("from FeeTemplate");
	}
	

}

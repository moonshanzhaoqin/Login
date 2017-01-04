package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.ConfigDAO;
import com.yuyutechnology.exchange.pojo.Config;

@Repository
public class ConfigDAOImpl implements ConfigDAO {
	
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public void saveOrUpdateConfig(Config config) {
		hibernateTemplate.saveOrUpdate(config);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Config> getConfigValues() {
		return (List<Config>) hibernateTemplate.find("from Config");
	}

}

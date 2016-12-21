package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.AppVersionDAO;
import com.yuyutechnology.exchange.pojo.AppVersion;

@Repository
public class AppVersionDAOImpl implements AppVersionDAO {
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public AppVersion getAppVersionInfo(String platformType, String updateWay) {
		List<?> list = hibernateTemplate.find("from AppVersion where platformType = ? and updateWay = ?", platformType,
				updateWay);
		if (!list.isEmpty()) {
			return (AppVersion) list.get(0);
		}
		return null;
	}
}

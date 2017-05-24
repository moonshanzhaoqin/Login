package com.yuyutechnology.exchange.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.ReplicationMode;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.CrmUserInfoDAO;
import com.yuyutechnology.exchange.pojo.CrmUserInfo;
import com.yuyutechnology.exchange.util.page.PageBean;
import com.yuyutechnology.exchange.util.page.PageUtils;

@Repository
public class CrmUserInfoDAOImpl implements CrmUserInfoDAO {
	
	@Resource
	HibernateTemplate hibernateTemplate;
	
	public static Logger logger = LogManager.getLogger(CrmUserInfoDAOImpl.class);
	
	@Override
	public void updateUserInfo(CrmUserInfo crmUserInfo){
		
		hibernateTemplate.replicate(crmUserInfo, ReplicationMode.LATEST_VERSION);
		
	}

	@Override
	public CrmUserInfo getCrmUserInfoByUserId(int userId) {
		return hibernateTemplate.get(CrmUserInfo.class, userId);
	}

	@Override
	public HashMap<String, Object> getUserAccountInfoListByPage(String sql, List<Object> values, int currentPage,
			int pageSize) {
		
		int firstResult = (currentPage -1)*pageSize;
		int masResult = pageSize;
		
		List<?> list = PageUtils.getListByPage(hibernateTemplate, sql, values, firstResult, masResult);
		
		long total = PageUtils.getTotal(hibernateTemplate,sql, values);
		int pageTotal = PageUtils.getPageTotal(total, pageSize);
		
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("currentPage",currentPage);
		map.put("pageSize",pageSize);
		map.put("total",total);
		map.put("pageTotal",pageTotal);
		map.put("list",list);
		
		return map;
	}

	@Override
	public void userFreeze(Integer userId, int userAvailable) {
		CrmUserInfo crmUserInfo = hibernateTemplate.get(CrmUserInfo.class, userId);
		if (crmUserInfo!= null) {
			crmUserInfo.setUserAvailable(userAvailable);
			hibernateTemplate.update(crmUserInfo);
		}
	}
	
	@Override
	public PageBean getUserInfoByPage(String userPhone, String userName, int currentPage, int pageSize) {
		List<Object> values = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(
				"from User");
		if (StringUtils.isNotBlank(userPhone)) {
			if (hql.length()>9) {
				hql.append(" and");
			}else{
				hql.append(" where");
			}
			hql.append(" userPhone = ?");
			values.add(userPhone);
		}
		if (StringUtils.isNotBlank(userName)) {
			if (hql.length()>9) {
				hql.append(" and");
			}else{
				hql.append(" where");
			}
			hql.append(" userName like ?");
			values.add("%"+userName+"%");
		}
		
		hql.append(" order by loginTime desc");

		PageBean pageBean =PageUtils.getPageContent(hibernateTemplate, hql.toString(), values, currentPage, pageSize);
		return pageBean;
	}
}

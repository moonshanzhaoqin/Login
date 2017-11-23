package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.WithdrawDAO;
import com.yuyutechnology.exchange.pojo.Withdraw;
import com.yuyutechnology.exchange.util.page.PageBean;
import com.yuyutechnology.exchange.util.page.PageUtils;

@Repository
public class WithdrawDAOImpl implements WithdrawDAO {
	public static Logger logger = LogManager.getLogger(WithdrawDAOImpl.class);
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public Withdraw getWithdraw(Integer withdrawId) {
		return hibernateTemplate.get(Withdraw.class, withdrawId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Withdraw> listWithdraw() {
		List<?> list = hibernateTemplate.find("from Withdraw order by applyTime desc");
		return (List<Withdraw>) list;
	}

	@Override
	public void updateWithdraw(Withdraw withdraw) {
		hibernateTemplate.saveOrUpdate(withdraw);
	}

	@Override
	public Integer saveWithdraw(Withdraw withdraw) {
		return (Integer) hibernateTemplate.save(withdraw);
	}

	@Override
	public PageBean getWithdrawByPage(String hql, List<Object> values, int currentPage, int pageSize) {
		return PageUtils.getPageContent(hibernateTemplate, hql.toString(), values, currentPage, pageSize);
	}
}

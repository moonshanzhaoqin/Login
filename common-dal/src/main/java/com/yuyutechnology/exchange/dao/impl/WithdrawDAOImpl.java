package com.yuyutechnology.exchange.dao.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.WithdrawDAO;
import com.yuyutechnology.exchange.pojo.Withdraw;
import com.yuyutechnology.exchange.util.page.PageBean;
import com.yuyutechnology.exchange.util.page.PageUtils;

@Repository
public class WithdrawDAOImpl implements WithdrawDAO {
	public static Logger logger = LogManager.getLogger(WithdrawDAOImpl.class);

	@Resource
	HibernateTemplate hibernateTemplate;
	@Autowired
	RedisDAO redisDAO;

	private final String ASSIGN_WITHDRAWID = "ASSIGN_WITHDRAWID";

	@Override
	public String createWithdrawId() {
		Long id = redisDAO.incrementValue(ASSIGN_WITHDRAWID, 1);
		StringBuilder sb = new StringBuilder();
		sb.append(DateFormatUtils.format(new Date(), "yyyyMMddHHmm")).append("W");
		String idStr = String.valueOf(id);
		if (idStr.length() < 6) {
			for (int i = 0; i < 6 - idStr.length(); i++) {
				sb.append("0");
			}
		}
		sb.append(idStr);
		return sb.toString();
	}

	@Override
	public Withdraw getWithdraw(String withdrawId) {
		return hibernateTemplate.get(Withdraw.class, withdrawId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Withdraw> listWithdrawByUserId(Integer userId) {
		List<?> list = hibernateTemplate.find("from Withdraw where userId = ? order by applyTime desc", userId);
		return (List<Withdraw>) list;
	}

	@Override
	public void updateWithdraw(Withdraw withdraw) {
		hibernateTemplate.saveOrUpdate(withdraw);
	}

	@Override
	public String saveWithdraw(Withdraw withdraw) {
		return (String) hibernateTemplate.save(withdraw);
	}

	@Override
	public PageBean getWithdrawByPage(String hql, List<Object> values, int currentPage, int pageSize) {
		return PageUtils.getPageContent(hibernateTemplate, hql.toString(), values, currentPage, pageSize);
	}

}

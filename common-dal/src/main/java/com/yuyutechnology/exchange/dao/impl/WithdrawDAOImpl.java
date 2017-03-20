package com.yuyutechnology.exchange.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.WithdrawDAO;
import com.yuyutechnology.exchange.pojo.Withdraw;
import com.yuyutechnology.exchange.utils.page.PageBean;
import com.yuyutechnology.exchange.utils.page.PageUtils;

@Repository
public class WithdrawDAOImpl implements WithdrawDAO {
	public static Logger logger = LogManager.getLogger(WithdrawDAOImpl.class);

	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public void saveOrUpdateWithdraw(Withdraw withdraw) {
		hibernateTemplate.saveOrUpdate(withdraw);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Withdraw> getNeedReviewWithdraws() {
		List<?> list = hibernateTemplate.find("from Withdraw where reviewStatus = ? ", ServerConsts.REVIEW_STATUS_NEED);
		return (List<Withdraw>) list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Withdraw> getNeedGoldpayRemitWithdraws() {
		List<?> list = hibernateTemplate.find("from Withdraw where goldpayRemit = ? and  reviewStatus = ?",
				ServerConsts.GOLDPAY_REMIT_, ServerConsts.REVIEW_STATUS_PASS);
		return (List<Withdraw>) list;
	}

	@Override
	public Withdraw getWithdrawByTransferId(String transferId) {
		List<?> list = hibernateTemplate.find("from Withdraw where transferId = ? ", transferId);
		return (list.isEmpty()) ? null : (Withdraw) list.get(0);
	}

	@Override
	public PageBean searchWithdrawsByPage(String userPhone, String reviewStatus, String goldpayRemit, int currentPage,
			int pageSize) {
		logger.info("userPhone={},reviewStatus={},goldpayRemit={},currentPage={},pageSize={}", userPhone, reviewStatus,
				goldpayRemit, currentPage, pageSize);
		List<Object> values = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from Withdraw");
//		 logger.info("hql.length()={}",hql.length());
		if (StringUtils.isNotBlank(userPhone)) {
			if (hql.length() > 13) {
				hql.append(" and ");
			} else {
				hql.append(" where ");
			}
			hql.append("userPhone = ? ");
			values.add(userPhone);
		}
		if (StringUtils.isNotBlank(reviewStatus)) {
			if (hql.length() > 13) {
				hql.append(" and ");
			} else {
				hql.append(" where ");
			}
			hql.append("reviewStatus = ? ");
			values.add(Integer.parseInt(reviewStatus));
		}
		if (StringUtils.isNotBlank(goldpayRemit)) {
			if (hql.length() > 13) {
				hql.append(" and ");
			} else {
				hql.append(" where ");
			}
			hql.append("goldpayRemit = ? ");
			values.add(Integer.parseInt(goldpayRemit));
		}
		logger.info("hql:{}", hql);
		PageBean pageBean = PageUtils.getPageContent(hibernateTemplate, hql.toString(), values, currentPage, pageSize);
		return pageBean;
	}

	@Override
	public Withdraw getWithdraw(Integer withdrawId) {
		return hibernateTemplate.get(Withdraw.class, withdrawId);
	}


}

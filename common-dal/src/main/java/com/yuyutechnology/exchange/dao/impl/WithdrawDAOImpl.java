package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.WithdrawDAO;
import com.yuyutechnology.exchange.pojo.Withdraw;

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
		List<?> list = hibernateTemplate.find("from Withdraw where goldpayRemit = ? and  reviewStatus = ?", ServerConsts.GOLDPAY_REMIT_TODO,ServerConsts.REVIEW_STATUS_PASS);
		return (List<Withdraw>) list;
	}

	
	@Override
	public Withdraw getWithdrawByTransferId(String transferId) {
		List<?> list = hibernateTemplate.find("from Withdraw where transferId = ? ", transferId);
		return (list.isEmpty()) ? null : (Withdraw) list.get(0);
	}
}

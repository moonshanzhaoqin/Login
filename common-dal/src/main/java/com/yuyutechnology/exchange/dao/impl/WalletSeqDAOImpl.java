package com.yuyutechnology.exchange.dao.impl;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.pojo.WalletSeq;

@Repository
public class WalletSeqDAOImpl implements WalletSeqDAO {
	
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public void addWalletSeq(WalletSeq walletSeq) {
		hibernateTemplate.save(walletSeq);
	}

}

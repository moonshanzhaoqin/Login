package com.yuyutechnology.exchange.dao.impl;

import java.math.BigDecimal;

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
	
	
	@Override
	public void addWalletSeq(int userId,int transferType,String transactionId,
			String currencyOut,BigDecimal amountOut,String currencyIn,BigDecimal amountIn){
		WalletSeq inSeq = new WalletSeq(userId,transferType,currencyIn,amountIn,transactionId);
		addWalletSeq(inSeq);
		//negate 取负数
		WalletSeq outSeq = new WalletSeq(userId,transferType,currencyOut,amountOut.negate(),transactionId);
		addWalletSeq(outSeq);
	}

}

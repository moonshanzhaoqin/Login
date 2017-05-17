package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

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
	public List<?> getWalletSeq(int userId, String currency, long startSeqId, long endSeqId) {
		List<?> list = hibernateTemplate.find(
				"from WalletSeq where userId = ? and currency = ? and seqId > ? and seqId <= ?", userId, currency,
				startSeqId, endSeqId);
		return list;
	}

	@Override
	public List<?> getWalletSeq2(int userId, long startSeqId, long endSeqId) {
		List<?> list = hibernateTemplate.find("from WalletSeq where userId = ?  and seqId > ? and seqId <= ?", userId,
				startSeqId, endSeqId);
		return list;
	}

	// @Override
	// public void addWalletSeq4Exchange(int userId,int transferType,String
	// transactionId,
	// String currencyOut,BigDecimal amountOut,String currencyIn,BigDecimal
	// amountIn){
	// WalletSeq inSeq = new
	// WalletSeq(userId,transferType,currencyIn,amountIn,transactionId, new
	// Date());
	// addWalletSeq(inSeq);
	// //negate 取负数
	// WalletSeq outSeq = new
	// WalletSeq(userId,transferType,currencyOut,amountOut.negate(),transactionId,
	// new Date());
	// addWalletSeq(outSeq);
	// }

	// @Override
	// public void addWalletSeq4Transaction(int payerId, int payeeId, int
	// transferType, String transactionId,
	// String currency, BigDecimal amount) {
	// WalletSeq payerSeq = new
	// WalletSeq(payerId,transferType,currency,amount.negate(),transactionId,
	// new Date());
	// addWalletSeq(payerSeq);
	// WalletSeq payeeSeq = new
	// WalletSeq(payeeId,transferType,currency,amount,transactionId, new
	// Date());
	// addWalletSeq(payeeSeq);
	// }

}

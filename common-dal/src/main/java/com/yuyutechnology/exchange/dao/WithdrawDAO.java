package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Withdraw;

public interface WithdrawDAO {

	void saveOrUpdateWithdraw(Withdraw withdraw);


	List<Withdraw> getNeedReviewWithdraws();
	List<Withdraw> getNeedGoldpayRemitWithdraws();
	Withdraw getWithdrawByTransferId(String transferId);




	
	
}

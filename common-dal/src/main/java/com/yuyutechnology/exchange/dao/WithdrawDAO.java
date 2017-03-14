package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Withdraw;
import com.yuyutechnology.exchange.utils.page.PageBean;

public interface WithdrawDAO {

	void saveOrUpdateWithdraw(Withdraw withdraw);

	List<Withdraw> getNeedReviewWithdraws();

	List<Withdraw> getNeedGoldpayRemitWithdraws();

	Withdraw getWithdrawByTransferId(String transferId);

	Withdraw getWithdraw(Integer withdrawId);

	PageBean searchWithdrawsByPage(String userId, String reviewStatus, String goldpayRemit, int currentPage,
			int pageSize);
	
	
	

}

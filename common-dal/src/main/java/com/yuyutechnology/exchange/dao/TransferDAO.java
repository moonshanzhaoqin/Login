package com.yuyutechnology.exchange.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.utils.page.PageBean;

public interface TransferDAO {

	public String createTransId(int transferType);

	public void addTransfer(Transfer transfer);
	
	public void updateTransfer(Transfer transfer);

	public Transfer getTransferById(String transferId);
	
	public Transfer getTranByIdAndStatus(String transferId,int transferStatus);

	public void updateAccumulatedAmount(String key, BigDecimal amoumt);
	
	public void updateCumulativeNumofTimes(String key, BigDecimal amoumt);

	public BigDecimal getAccumulatedAmount(String key);
	
	public int getCumulativeNumofTimes(String key);

	public void updateTransferStatus(String transferId, int transferStatus);

	public void updateTransferStatusAndUserTo(String transferId,
			int transferStatus, Integer userTo);
	
	public HashMap<String, Object> getTransactionRecordByPage(
			String sql,String countSql,List<Object> values,int currentPage, int pageSize);
	
	public List<Transfer> findTransferByStatusAndTimeBefore(int transferStatus, int transferType, Date date);
	
	public BigDecimal sumGoldpayTransAmount(int transferType);
	
	public Integer getDayTradubgVolume(final int transferType);

	public PageBean getWithdrawRecordByPage(Integer userId, int currentPage, int pageSize);

	public List<Transfer> getNeedGoldpayRemitWithdraws();

	public List<Transfer> getNeedReviewWithdraws();

	public PageBean searchWithdrawsByPage(String userPhone, String reviewStatus, String goldpayRemit, int currentPage,
			int pageSize);

//	void testByPage();
	
}

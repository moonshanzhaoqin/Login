package com.yuyutechnology.exchange.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.util.page.PageBean;

public interface TransferDAO {

	String createTransId(int transferType);

	void addTransfer(Transfer transfer);

	void updateTransfer(Transfer transfer);

	Transfer getTransferById(String transferId);

	Transfer getTranByIdAndStatus(String transferId, int transferStatus);

	void updateAccumulatedAmount(String key, BigDecimal amoumt);

	void updateCumulativeNumofTimes(String key, BigDecimal amoumt);

	BigDecimal getAccumulatedAmount(String key);

	int getCumulativeNumofTimes(String key);

	void updateTransferStatus(String transferId, int transferStatus);

	void updateTransferStatusAndUserTo(String transferId, int transferStatus, Integer userTo);

	HashMap<String, Object> getTransactionRecordByPage(String sql, String countSql, List<Object> values,
			int currentPage, int pageSize);

	List<Transfer> findTransferByStatusAndTimeBefore(int transferStatus, int transferType, Date date);

	BigDecimal sumGoldpayTransAmount(int transferType);

	Integer getDayTradubgVolume(final int transferType);

	Object getTransferByIdJoinUser(String transferId);

	BigDecimal getTotalPaypalExchange(Date finishTime, int transferType, int transferStatus);

	/**
	 * 分页搜索Transfer
	 * 
	 * @param hql
	 * @param values
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	PageBean searchTransfersByPage(String hql, List<Object> values, int currentPage, int pageSize);

//	int saveTransfer(Transfer transfer);

}

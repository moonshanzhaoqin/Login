package com.yuyutechnology.exchange.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.pojo.Transfer;

public interface TransferDAO {

	public String createTransId(int transferType);

	public void addTransfer(Transfer transfer);

	public Transfer getTransferById(String transferId);
	
	public Transfer getTransferByIdAndUserId(String transferId,int userId);

	public void updateAccumulatedAmount(String key, BigDecimal amoumt);

	public BigDecimal getAccumulatedAmount(String key);

	public void updateTransferStatus(String transferId, int transferStatus);

	public void updateTransferStatusAndUserTo(String transferId, int transferStatus, Integer userTo);
	
	public HashMap<String, Object> getTransactionRecordByPage(String sql,String countSql,List<Object> values,int currentPage, int pageSize);

}

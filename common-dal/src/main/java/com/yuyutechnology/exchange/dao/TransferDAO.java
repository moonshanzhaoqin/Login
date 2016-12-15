package com.yuyutechnology.exchange.dao;

import java.math.BigDecimal;

import com.yuyutechnology.exchange.pojo.Transfer;

public interface TransferDAO {

	public String createTransId(int transferType);

	public void addTransfer(Transfer transfer);

	public Transfer getTransferById(String transferId);

	public void updateAccumulatedAmount(String key, BigDecimal amoumt);

	public BigDecimal getAccumulatedAmount(String key);

	public void updateTransferStatus(String transferId, int transferStatus);

	public void updateTransferStatusAndUserTo(String transferId, int transferStatus, Integer userTo);
	
	public void getTransactionRecordByPage(String period,int status);

}

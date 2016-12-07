package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.Transfer;

public interface TransferDAO {

	public String createTransId(int transferType);

	public void addTransfer(Transfer transfer);

	public Transfer getTransferById(String transferId);

	public void updateTransferStatus(String transferId, int transferStatus);

	/**
	 * 更改转账状态和转入账户
	 * 
	 * @param transferId
	 * @param transferStatus
	 * @param userTo
	 */

	public void updateTransferStatusAndUserTo(String transferId, int transferStatus, Integer userTo);

}

package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.Transfer;

public interface TransferDAO {
	
	public String createTransId(int transferType);
	
	public void addTransfer(Transfer transfer);
	
	public Transfer getTransferById(String transferId);
	
	public void updateTransferStatus(String transferId,int transferStatus);

}

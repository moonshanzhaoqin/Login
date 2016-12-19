package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.TransactionNotification;

public interface NotificationDAO {

	public void addNotification(TransactionNotification transactionNotification);
	
	public TransactionNotification getNotificationById(int noticeId);
	
	public List<TransactionNotification> getNotificationListByUserId(int userId);
	
	public void updateNotification(TransactionNotification transactionNotification);
	
}

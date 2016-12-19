package com.yuyutechnology.exchange.manager.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.manager.GoldpayTransManager;

@Service
public class GoldpayTransManagerImpl implements GoldpayTransManager{
	
	@Autowired
	TransferDAO transferDAO;

	@Override
	public HashMap<String, Object> goldpayPurchase() {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.yuyutechnology.exchange.crm.tpps.manager;

import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayClient;
import com.yuyutechnology.exchange.util.MathUtils;
import com.yuyutechnology.exchange.util.UidUtils;

public class TppsMananger {

	public void addPayClient(Integer exId) {
		// TODO Auto-generated method stub
		GoldqPayClient goldqPayClient=new GoldqPayClient(exId, UidUtils.genUid(),  UidUtils.genUid());
		
		
	}


}

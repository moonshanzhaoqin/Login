package com.yuyutechnology.exchange.crm.tpps.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.crm.tpps.dao.TppsDAO;
import com.yuyutechnology.exchange.crm.tpps.pojo.GoldqPayClient;
import com.yuyutechnology.exchange.util.UidUtils;
@Service
public class TppsMananger {

	@Autowired
	TppsDAO tppsDAO;
	public void addPayClient(Integer exId) {
		// TODO Auto-generated method stub
		GoldqPayClient goldqPayClient=new GoldqPayClient(exId, UidUtils.genUid(),  UidUtils.genUid());
		tppsDAO.saveGoldqPayClient(goldqPayClient);
	}


}

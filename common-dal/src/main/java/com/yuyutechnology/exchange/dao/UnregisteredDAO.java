package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Unregistered;

public interface UnregisteredDAO {
	
	public void addUnregistered(Unregistered unregistered);

	public List<Unregistered> getUnregisteredByUserPhone(String areaCode, String userPhone);

	public void updateUnregistered(Unregistered unregistered);
	
	public List<Unregistered> getAllUnfinishedTransaction();
	
	public Unregistered getUnregisteredByTransId(String transId);

	List<Unregistered> getUnregisteredByPhoneAllStatus(String areaCode, String userPhone);
	
}

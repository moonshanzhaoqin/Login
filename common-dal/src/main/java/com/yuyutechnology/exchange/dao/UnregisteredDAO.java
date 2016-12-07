package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Unregistered;

public interface UnregisteredDAO {

	public List<Unregistered> getUnregisteredByUserPhone(String areaCode, String userPhone);

	public void addUnregistered(Unregistered unregistered);
	
}

package com.yuyutechnology.exchange.dao;

import java.util.List;
import java.util.Map;

public interface GoldpayDAO {
	
	int getGoldpayAccountTotalCount();
	
	List<Map<String, Object>> getGoldpayAccountList(int start, int size);
	
	public void replaceGAccount(List<Map<String, Object>> lists);

}

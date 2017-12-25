package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.Bind;
import com.yuyutechnology.exchange.pojo.GoldpayAccount;

public interface BindDAO {
	public Bind getBind(Integer userId);
	
	public GoldpayAccount getGoldpayAccount(Integer userId);

	public void updateBind(Bind bind);
	
	public void clearHappyLivesId(String happyLivesId);

	void updateGoldpayAccount(GoldpayAccount account);
}

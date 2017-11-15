package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.Bind;

public interface BindDAO {
	public Bind getBind(Integer userId);

	public void updateBind(Bind bind);
	
	public void clearHappyLivesId(String happyLivesId);
}

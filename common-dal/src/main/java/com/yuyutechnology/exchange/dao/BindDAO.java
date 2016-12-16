package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.Bind;

public interface BindDAO {
	public Bind getBindByUserId(Integer userId);

	public void saveBind(Bind bind);
}

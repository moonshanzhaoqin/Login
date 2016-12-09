package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Bind;

public interface BindDAO {
	public List<Bind> getBindByUserId(Integer userId);

	public void saveBind(Bind bind);
}

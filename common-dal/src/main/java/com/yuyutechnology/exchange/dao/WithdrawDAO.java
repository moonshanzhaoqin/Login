package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Withdraw;

public interface WithdrawDAO {

	Withdraw getWithdraw(Integer withdrawId);

	void updateWithdraw(Withdraw withdraw);

	List<Withdraw> listWithdraw();

	void saveWithdraw(Withdraw withdraw);

}

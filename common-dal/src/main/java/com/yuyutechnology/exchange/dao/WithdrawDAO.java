package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Withdraw;
import com.yuyutechnology.exchange.util.page.PageBean;

public interface WithdrawDAO {

	Withdraw getWithdraw(Integer withdrawId);

	void updateWithdraw(Withdraw withdraw);

	List<Withdraw> listWithdraw();

	Integer saveWithdraw(Withdraw withdraw);

	PageBean getWithdrawByPage(String hql, List<Object> values, int currentPage, int pageSize);

}

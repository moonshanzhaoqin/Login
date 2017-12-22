package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Withdraw;
import com.yuyutechnology.exchange.util.page.PageBean;

public interface WithdrawDAO {

	Withdraw getWithdraw(String withdrawId);

	void updateWithdraw(Withdraw withdraw);

	void saveWithdraw(Withdraw withdraw);

	PageBean getWithdrawByPage(String hql, List<Object> values, int currentPage, int pageSize);

	String createWithdrawId();

	List<Withdraw> listWithdrawByUserId(Integer userId);
}

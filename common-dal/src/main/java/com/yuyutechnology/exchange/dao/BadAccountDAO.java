/**
 * 
 */
package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.BadAccount;
import com.yuyutechnology.exchange.util.page.PageBean;

/**
 * @author silent.sun
 *
 */
public interface BadAccountDAO {

	public void saveBadAccount(BadAccount badAccount);
	
	public List<Integer> findBadAccountList(int badAccountStatus);
	
	public void updateBadAccountStatus(int badAccountStatus, int userId);
	
	public PageBean getBadAccountByPage(int currentPage, int pageSize);

	BadAccount getBadAccount(Integer badAccountId);

	BadAccount getBadAccountByTransferId(String transferId);
}

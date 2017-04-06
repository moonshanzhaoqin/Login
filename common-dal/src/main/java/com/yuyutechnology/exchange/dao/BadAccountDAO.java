/**
 * 
 */
package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.BadAccount;
import com.yuyutechnology.exchange.utils.page.PageBean;

/**
 * @author silent.sun
 *
 */
public interface BadAccountDAO {

	public void saveBadAccount(BadAccount badAccount);
	
	public List<BadAccount> findBadAccountList(int badAccountStatus);

	public PageBean getBadAccountByPage(int currentPage, int pageSize);
}

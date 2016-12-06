/**
 * 
 */
package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.User;

/**
 * @author suzan.wu
 *
 */
public interface UserDAO {

	public User getUser(Integer userId);
	
	public User getUserByUserPhone(String userPhone);

	public Integer addUser(User user);

	
	

}

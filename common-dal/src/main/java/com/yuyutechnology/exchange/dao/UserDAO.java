/**
 * 
 */
package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.UserConfig;

/**
 * @author suzan.wu
 *
 */
public interface UserDAO {

	public User getSystemUser();

	public User getUser(Integer userId);

	public User getUserByUserPhone(String areaCode, String userPhone);

	public Integer addUser(User user);

	public void updateUser(User user);
	
	public List<User> getUserList();
	
	public UserConfig getUserConfig(Integer userId);
	
	public void saveUserConfig(UserConfig userConfig);

	User getUserByPhone(String userPhone);

	public List<User> getUserByPushId(String pushId);

	void updateHQL(String hql, Object[] values);

}

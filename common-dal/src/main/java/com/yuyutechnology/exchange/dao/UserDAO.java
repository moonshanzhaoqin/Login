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

	User getSystemUser();

	User getUser(Integer userId);

	User getUserByUserPhone(String areaCode, String userPhone);

	Integer addUser(User user);

	void updateUser(User user);

	List<User> getUserList();

	UserConfig getUserConfig(Integer userId);

	void saveUserConfig(UserConfig userConfig);

	User getUserByPhone(String userPhone);

	List<User> getUserByPushId(String pushId);

	/**
	 * 批量更新
	 * 
	 * @param hql
	 * @param values
	 */
	void updateHQL(String hql, Object[] values);

	/**
	 * @return
	 */
	long get24HRegistration();

}

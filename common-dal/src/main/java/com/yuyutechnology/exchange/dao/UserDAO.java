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

	Integer addUser(User user);

	User getSystemUser();

	User getUser(Integer userId);

	List<User> getUserByPushId(String pushId);

	User getUserByUserPhone(String areaCode, String userPhone);

	UserConfig getUserConfig(Integer userId);

	List<User> listAllUser();

	void saveUserConfig(UserConfig userConfig);

	/**
	 * 批量更新
	 * 
	 * @param hql
	 * @param values
	 */
	void updateSQL(String sql, Object[] values);

	void updateUser(User user);

	User getFeeUser();

	User getFrozenUser();

	User getRecoveryUser();

}

/**
 * 
 */
package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.Inviter;

/**
 * @author suzan.wu
 *
 */
public interface InviterDAO {
	Inviter getInviter(Integer userId);
	void updateInviter(Inviter inviter);
}

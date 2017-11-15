/**
 * 
 */
package com.yuyutechnology.exchange.dao;

import java.math.BigDecimal;

import com.yuyutechnology.exchange.pojo.Inviter;

/**
 * @author suzan.wu
 *
 */
public interface InviterDAO {
	Inviter getInviter(Integer userId);

	Integer updateInviter(Integer userId, int inviteQuantityIncrement, BigDecimal inviteBonusIncrement);

	void saveInviter(Inviter inviter);
}

/**
 * 
 */
package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Friend;

/**
 * @author suzan.wu
 *
 */
public interface FriendDAO {

	public List<Friend> getFriendsByUserId(Integer userId);

	public void addfriend(Friend friend);

	public Friend getFriendByUserIdAndFrindId(Integer userId, Integer friendId);

	public void deleteFriend(Friend friend2);

}

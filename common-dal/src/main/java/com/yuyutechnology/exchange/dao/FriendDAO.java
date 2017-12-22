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

	List<Friend> getFriendsByUserId(Integer userId);

	void addFriend(Friend friend);

	Friend getFriendByUserIdAndFrindId(Integer userId, Integer friendId);

	void deleteFriend(Friend friend);

	List<Friend> getFriendByUserIdAndKeyWords(Integer userId, String keyWords);

}

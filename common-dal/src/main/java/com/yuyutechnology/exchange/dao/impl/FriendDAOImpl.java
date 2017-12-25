package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.FriendDAO;
import com.yuyutechnology.exchange.pojo.Friend;

@Repository
public class FriendDAOImpl implements FriendDAO {
	public static Logger logger = LogManager.getLogger(FriendDAOImpl.class);
	@Resource
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<Friend> getFriendsByUserId(Integer userId) {
		List<?> list = hibernateTemplate.find("from Friend where id.userId = ? order by user.namePinyin", userId);
		return (List<Friend>) list;
	}

	@Override
	public void addFriend(Friend friend) {
		hibernateTemplate.saveOrUpdate(friend);
	}

	@Override
	public Friend getFriendByUserIdAndFrindId(Integer userId, Integer friendId) {
		List<?> list = hibernateTemplate.find("from Friend where id.userId = ? and id.friendId = ?", userId, friendId);
		if (!list.isEmpty()) {
			return (Friend) list.get(0);
		}
		return null;
	}

	@Override
	public void deleteFriend(Friend friend2) {
		hibernateTemplate.delete(friend2);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Friend> getFriendByUserIdAndKeyWords(Integer userId, String keyWords) {
		List<?> list = hibernateTemplate.find(
				"from Friend where id.userId = ? and (user.userName like ? or SUBSTRING(user.namePinyin, 2, LENGTH(user.namePinyin)) like ? or user.userPhone like ?) order by user.namePinyin",
				userId, '%' + keyWords + '%', '%' + keyWords + '%', '%' + keyWords + '%');
		return (List<Friend>) list;
	}
}

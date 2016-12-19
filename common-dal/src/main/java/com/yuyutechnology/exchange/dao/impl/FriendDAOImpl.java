package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.FriendDAO;
import com.yuyutechnology.exchange.pojo.Friend;

@Repository
public class FriendDAOImpl implements FriendDAO {
	@Resource
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<Friend> getFriendsByUserId(Integer userId) {
		List<?> list = hibernateTemplate.find("from Friend where id.userId = ? order by user.userName", userId);
		return (List<Friend>) list;
	}

	@Override
	public void addfriend(Friend friend) {
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

}

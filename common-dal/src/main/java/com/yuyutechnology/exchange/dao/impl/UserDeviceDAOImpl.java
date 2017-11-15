package com.yuyutechnology.exchange.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.UserDeviceDAO;
import com.yuyutechnology.exchange.pojo.UserDevice;

@Repository
public class UserDeviceDAOImpl implements UserDeviceDAO {
	@Resource
	HibernateTemplate hibernateTemplate;

	@Override
	public UserDevice getUserDeviceByUserIdAndDeviceId(Integer userId, String deviceId) {
		List<?> list = hibernateTemplate.find("from UserDevice where id.userId = ? and id.deviceId = ?", userId, deviceId);
		return list.isEmpty() ? null : (UserDevice) list.get(0);
	}

	@Override
	public void addUserDevice(UserDevice userDevice) {
		hibernateTemplate.saveOrUpdate(userDevice);
	}

}

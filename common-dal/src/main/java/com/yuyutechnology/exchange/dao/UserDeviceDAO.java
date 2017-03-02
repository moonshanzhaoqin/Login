package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.UserDevice;

public interface UserDeviceDAO {
	// 查找设备
	public UserDevice getUserDeviceByUserIdAndDeviceId(Integer userId, String deviceId);

	// 添加设备
	public void addUserDevice(UserDevice userDevice);
}

package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.AppVersion;

public interface AppVersionDAO {

	public  AppVersion getAppVersionInfo(String platformType, String updateWay);

}

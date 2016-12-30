package com.yuyutechnology.exchange.manager;

public interface AdminManager {

	int login(String adminName, String adminPassword);

	void addAdmin(String adminName);

	int modifyPassword(Integer adminId, String oldPassword, String newPassword);

}

package com.yuyutechnology.exchange.manager;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Admin;

public interface AdminManager {

	int login(String adminName, String adminPassword);

	void addAdmin(String adminName);

	int modifyPassword(String adminName, String oldPassword, String newPassword);

	public List<Admin> getAdminList();

}

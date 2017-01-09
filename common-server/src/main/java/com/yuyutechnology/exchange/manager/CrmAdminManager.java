package com.yuyutechnology.exchange.manager;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Admin;

public interface CrmAdminManager {

	String login(String adminName, String adminPassword);

	void addAdmin(String adminName);

	String modifyPassword(String adminName, String oldPassword, String newPassword);

	public List<Admin> getAdminList();

}

package com.yuyutechnology.exchange.manager;

public interface AdminManager {

	int login(String adminName, String adminPassword);

	void addAdmin(String adminName);

}

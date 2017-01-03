package com.yuyutechnology.exchange.dao;

import java.util.List;

import com.yuyutechnology.exchange.pojo.Admin;

public interface AdminDAO {

	Admin getAdminByName(String adminName);

	void updateAdmin(Admin admin);

	Admin getAdmin(Integer adminId);

	List<Admin> getAdminList();

}

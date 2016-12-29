package com.yuyutechnology.exchange.crm.request;

import org.apache.commons.lang.StringUtils;

public class LoginRquest {
	private String adminName;
	private String adminPassword;

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (StringUtils.isBlank(this.adminName)) {
			return true;
		}
		if (StringUtils.isBlank(this.adminPassword)) {
			return true;
		}
		return false;
	}
}

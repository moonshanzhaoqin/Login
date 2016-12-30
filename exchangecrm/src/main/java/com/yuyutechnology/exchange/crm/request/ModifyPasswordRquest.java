package com.yuyutechnology.exchange.crm.request;

import org.apache.commons.lang.StringUtils;

public class ModifyPasswordRquest {
	private String oldPassword;
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (StringUtils.isBlank(this.oldPassword)) {
			return true;
		}
		if (StringUtils.isBlank(this.newPassword)) {
			return true;
		}
		return false;
	}

}

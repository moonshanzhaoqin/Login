package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class ModifyUserNameRequest extends BaseRequest{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3224874534070106002L;
	private String newUserName;

	public String getNewUserName() {
		return newUserName;
	}

	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean empty() {
		if (StringUtils.isBlank(this.newUserName)) {
			return true;
		}
		return false;
	}
}

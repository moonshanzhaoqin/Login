/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

/**
 * @author suzan.wu
 *
 */
public class GetUserRequest extends BaseRequest{
	/**
	 * 
	 */
	private static final long serialVersionUID = -523026898033454633L;
	private String areaCode;
	private String userPhone;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean empty() {
		if (StringUtils.isBlank(this.areaCode)) {
			return true;
		}
		if (StringUtils.isBlank(this.userPhone)) {
			return true;
		}
		return false;
	}
}

package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.yuyutechnology.exchange.ServerConsts;

public class CheckPayPwdRequest {
	private String purpose;
	private String userPayPwd;

	@ApiModelProperty(allowableValues = ServerConsts.PAYPWD_CHANGEPHONE + "," + ServerConsts.PAYPWD_MODIFYPAYPWD)
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getUserPayPwd() {
		return userPayPwd;
	}

	public void setUserPayPwd(String userPayPwd) {
		this.userPayPwd = userPayPwd;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.purpose)) {
			return true;
		}
		if (StringUtils.isEmpty(this.userPayPwd)) {
			return true;
		}
		return false;
	}
}

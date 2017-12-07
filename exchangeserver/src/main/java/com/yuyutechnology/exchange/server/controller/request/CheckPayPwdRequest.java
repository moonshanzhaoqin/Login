package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.yuyutechnology.exchange.ServerConsts;

public class CheckPayPwdRequest {
	@ApiModelProperty(value="目的",allowableValues = ServerConsts.PAYPWD_CHANGEPHONE + "," + ServerConsts.PAYPWD_MODIFYPAYPWD + ","
			+ ServerConsts.PAYPWD_WITHDRAW)
	private String purpose;
	@ApiModelProperty(value="支付密码")
	private String userPayPwd;

	
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
	public boolean empty() {
		if (StringUtils.isBlank(this.purpose)) {
			return true;
		}
		if (StringUtils.isBlank(this.userPayPwd)) {
			return true;
		}
		return false;
	}
}

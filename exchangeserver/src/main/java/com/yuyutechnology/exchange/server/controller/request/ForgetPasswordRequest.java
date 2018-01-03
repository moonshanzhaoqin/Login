package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class ForgetPasswordRequest extends BaseRequest{
	@ApiModelProperty(value="国家码",required=true)
	private String areaCode;
	@ApiModelProperty(value="手机号",required=true)
	private String userPhone;
	@ApiModelProperty(value="验证码",required=true)
	private String verificationCode;
	@ApiModelProperty(value="新登录密码",required=true)
	private String newPassword;

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

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
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
	public boolean empty() {
		if (StringUtils.isBlank(this.areaCode)) {
			return true;
		}
		if (StringUtils.isBlank(this.userPhone)) {
			return true;
		}
		if (StringUtils.isBlank(this.newPassword)) {
			return true;
		}
		if (StringUtils.isBlank(this.verificationCode)) {
			return true;
		}
		return false;
	}
}

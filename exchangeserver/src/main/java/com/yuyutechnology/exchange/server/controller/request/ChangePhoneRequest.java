package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class ChangePhoneRequest {
	@ApiModelProperty(value="检验返回的密钥",required=true)
	private String checkToken;
	@ApiModelProperty(value="国家码",required=true)
	private String areaCode;
	@ApiModelProperty(value="手机号",required=true)
	private String userPhone;
	@ApiModelProperty(value="验证码",required=true)
	private String verificationCode;

	public String getCheckToken() {
		return checkToken;
	}

	public void setCheckToken(String checkToken) {
		this.checkToken = checkToken;
	}

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

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean empty() {
		if (StringUtils.isBlank(this.checkToken)) {
			return true;
		}
		if (StringUtils.isBlank(this.areaCode)) {
			return true;
		}
		if (StringUtils.isBlank(this.userPhone)) {
			return true;
		}
		if (StringUtils.isBlank(this.verificationCode)) {
			return true;
		}
		return false;
	}
}

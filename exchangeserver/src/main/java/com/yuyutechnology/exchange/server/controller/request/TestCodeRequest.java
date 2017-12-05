package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.yuyutechnology.exchange.ServerConsts;

public class TestCodeRequest {
	private String purpose;
	private String areaCode;
	private String userPhone;
	private String verificationCode;

	@ApiModelProperty(allowableValues = ServerConsts.PIN_FUNC_REGISTER + "," + ServerConsts.PIN_FUNC_CHANGEPHONE + ","
			+ ServerConsts.PIN_FUNC_FORGETPASSWORD + "," + ServerConsts.PIN_FUNC_MODIFYPAYPWD)
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
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
		if (StringUtils.isBlank(this.purpose)) {
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

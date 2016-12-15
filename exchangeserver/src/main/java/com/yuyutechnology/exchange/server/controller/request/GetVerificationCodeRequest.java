package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class GetVerificationCodeRequest {
	@Override
	public String toString() {
		return "[purpose=" + purpose + ", areaCode=" + areaCode + ", userPhone=" + userPhone
				+ "]";
	}

	private String purpose;
	private String areaCode;
	private String userPhone;

	@ApiModelProperty(allowableValues = "REGISTER,FORGETPASSWORD,CHANGEPHONE")
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

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.purpose)) {
			return true;
		}
		if (StringUtils.isEmpty(this.areaCode)) {
			return true;
		}
		if (StringUtils.isEmpty(this.userPhone)) {
			return true;
		}
		return false;
	}
}

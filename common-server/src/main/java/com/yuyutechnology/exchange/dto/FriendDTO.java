package com.yuyutechnology.exchange.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class FriendDTO {

	private String areaCode;
	private String phone;
	private String name;
	@ApiModelProperty(value = "头像")
	private String portrait;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

}

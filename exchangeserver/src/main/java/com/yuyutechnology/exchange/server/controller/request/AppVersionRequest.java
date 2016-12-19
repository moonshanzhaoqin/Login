package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

public class AppVersionRequest {
	private String appVersionNum; // 版本号
	private String platformType; // 平台 0Android 1iOS
	private String updateWay; // GooglePlay;YingYongBao

	public String getAppVersionNum() {
		return appVersionNum;
	}

	public void setAppVersionNum(String appVersionNum) {
		this.appVersionNum = appVersionNum;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getUpdateWay() {
		return updateWay;
	}

	public void setUpdateWay(String updateWay) {
		this.updateWay = updateWay;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.appVersionNum)) {
			return true;
		}
		if (StringUtils.isEmpty(this.platformType)) {
			return true;
		}
		if (StringUtils.isEmpty(this.updateWay)) {
			return true;
		}
		return false;
	}

}

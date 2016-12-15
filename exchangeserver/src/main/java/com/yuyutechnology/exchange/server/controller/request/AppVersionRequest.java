package com.yuyutechnology.exchange.server.controller.request;

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

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}

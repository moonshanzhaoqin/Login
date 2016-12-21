package com.yuyutechnology.exchange.server.controller.response;

import com.yuyutechnology.exchange.pojo.AppVersion;

public class AppVersionResponse extends BaseResponse {
	private AppVersion appVersion;

	public AppVersion getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(AppVersion appVersion) {
		this.appVersion = appVersion;
	}


}

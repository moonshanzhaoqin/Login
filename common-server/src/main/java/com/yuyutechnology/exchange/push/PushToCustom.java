package com.yuyutechnology.exchange.push;

public class PushToCustom {
	private String appName;
	private String deviceID;
	private String userId;
	private String title;
	private String body;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "PushToCustom [appName=" + appName + ", deviceID=" + deviceID + ", userId=" + userId + ", title=" + title
				+ ", body=" + body + "]";
	}

}

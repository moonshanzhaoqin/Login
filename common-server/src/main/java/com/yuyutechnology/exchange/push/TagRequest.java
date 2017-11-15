package com.yuyutechnology.exchange.push;

public class TagRequest {
	private String appName;
	private String deviceIds;
	private String tagName;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	@Override
	public String toString() {
		return "TagRequest [appName=" + appName + ", deviceIds=" + deviceIds + ", tagName=" + tagName + "]";
	}
}

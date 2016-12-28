package com.yuyutechnology.exchange.push;

public class PushToCustom {
	private String appName;
	private String deviceID;
	private String title;
	private String body;
	private String extParameters;

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

	public String getExtParameters() {
		return extParameters;
	}

	public void setExtParameters(String extParameters) {
		this.extParameters = extParameters;
	}

	@Override
	public String toString() {
		return "PushToCustom [appName=" + appName + ", deviceID=" + deviceID + ", title=" + title + ", body=" + body
				+ ", extParameters=" + extParameters + "]";
	}

}

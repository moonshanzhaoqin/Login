package com.yuyutechnology.exchange.server.msg;

public class BaseMsgS2C {
	
	private int retCode;
	private String  msg;
	
	public int getRetCode() {
		return retCode;
	}
	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}

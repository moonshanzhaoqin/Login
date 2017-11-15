/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.response;

/**
 * @author silent.sun
 *
 */
public class GetMsgFlagResponse extends BaseResponse {

	private boolean newTrans;
	private boolean newRequestTrans;

	public boolean isNewTrans() {
		return newTrans;
	}

	public void setNewTrans(boolean newTrans) {
		this.newTrans = newTrans;
	}

	public boolean isNewRequestTrans() {
		return newRequestTrans;
	}

	public void setNewRequestTrans(boolean newRequestTrans) {
		this.newRequestTrans = newRequestTrans;
	}
}

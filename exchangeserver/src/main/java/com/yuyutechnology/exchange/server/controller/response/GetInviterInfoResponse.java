/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.response;

import com.yuyutechnology.exchange.dto.InviterInfo;

/**
 * @author suzan.wu
 *
 */
public class GetInviterInfoResponse extends BaseResponse {
	private InviterInfo inviterInfo;
	private String inviterCode;

	public String getInviterCode() {
		return inviterCode;
	}

	public void setInviterCode(String inviterCode) {
		this.inviterCode = inviterCode;
	}

	public InviterInfo getInviterInfo() {
		return inviterInfo;
	}

	public void setInviterInfo(InviterInfo inviterInfo) {
		this.inviterInfo = inviterInfo;
	}

}

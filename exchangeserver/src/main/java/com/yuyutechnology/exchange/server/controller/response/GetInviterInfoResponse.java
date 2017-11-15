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

	public InviterInfo getInviterInfo() {
		return inviterInfo;
	}

	public void setInviterInfo(InviterInfo inviterInfo) {
		this.inviterInfo = inviterInfo;
	}

}

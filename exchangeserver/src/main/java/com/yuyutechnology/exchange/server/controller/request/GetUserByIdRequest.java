/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.request;

/**
 * @author suzan.wu
 *
 */
public class GetUserByIdRequest extends BaseRequest{
	
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}

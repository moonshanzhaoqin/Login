/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.request;

/**
 * @author suzan.wu
 *
 */
public class GetUserByIdRequest extends BaseRequest{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2101854146065849741L;
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}

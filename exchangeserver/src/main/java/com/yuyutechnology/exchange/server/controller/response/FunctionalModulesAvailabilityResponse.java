/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.response;

/**
 * @author suzan.wu
 *
 */
public class FunctionalModulesAvailabilityResponse extends BaseResponse {
	private boolean paypalRecharge;

	public boolean isPaypalRecharge() {
		return paypalRecharge;
	}

	public void setPaypalRecharge(boolean paypalRecharge) {
		this.paypalRecharge = paypalRecharge;
	}
}

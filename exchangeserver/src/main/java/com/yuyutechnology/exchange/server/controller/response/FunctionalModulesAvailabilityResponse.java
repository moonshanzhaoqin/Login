/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.response;

/**
 * @author suzan.wu
 *
 */
public class FunctionalModulesAvailabilityResponse extends BaseResponse {
	private boolean goldpayWithdraw;
	private boolean paypalRecharge;

	public boolean isGoldpayWithdraw() {
		return goldpayWithdraw;
	}

	public void setGoldpayWithdraw(boolean goldpayWithdraw) {
		this.goldpayWithdraw = goldpayWithdraw;
	}

	public boolean isPaypalRecharge() {
		return paypalRecharge;
	}

	public void setPaypalRecharge(boolean paypalRecharge) {
		this.paypalRecharge = paypalRecharge;
	}
}

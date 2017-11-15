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
	private boolean bankRechage;

	public boolean isPaypalRecharge() {
		return paypalRecharge;
	}

	public void setPaypalRecharge(boolean paypalRecharge) {
		this.paypalRecharge = paypalRecharge;
	}

	public boolean isBankRechage() {
		return bankRechage;
	}

	public void setBankRechage(boolean bankRechage) {
		this.bankRechage = bankRechage;
	}

}

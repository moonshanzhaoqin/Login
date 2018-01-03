package com.yuyutechnology.exchange.server.controller.request;

public class WithdrawCalculateRequset extends BaseRequest{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2047414341591399721L;
	private int goldBullion;

	public int getGoldBullion() {
		return goldBullion;
	}

	public void setGoldBullion(int goldBullion) {
		this.goldBullion = goldBullion;
	}

	/**
	 * 判断参数是否为空
	 * 
	 * @return
	 */
	public boolean empty() {
		if (this.goldBullion <= 0) {
			return true;
		}
		return false;
	}
}

package com.yuyutechnology.exchange.server.controller.request;

public class PaypalTransConfirmRequest extends BaseRequest{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1193297752222995585L;
	private String transId;
	private String paymentMethodNonce;

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getPaymentMethodNonce() {
		return paymentMethodNonce;
	}

	public void setPaymentMethodNonce(String paymentMethodNonce) {
		this.paymentMethodNonce = paymentMethodNonce;
	}

}

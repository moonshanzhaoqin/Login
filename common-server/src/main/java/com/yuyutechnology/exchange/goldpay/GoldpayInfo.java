package com.yuyutechnology.exchange.goldpay;

public class GoldpayInfo {

	private int retCode;
	private GoldpayUser goldpayUserDTO;

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public GoldpayUser getGoldpayUserDTO() {
		return goldpayUserDTO;
	}

	public void setGoldpayUserDTO(GoldpayUser goldpayUserDTO) {
		this.goldpayUserDTO = goldpayUserDTO;
	}

}

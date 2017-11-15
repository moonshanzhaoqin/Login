package com.yuyutechnology.exchange.goldpay.msg;

public class CreateGoldpayS2C {

	private int retCode;
	private GoldpayUserDTO goldpayUserDTO;

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public GoldpayUserDTO getGoldpayUserDTO() {
		return goldpayUserDTO;
	}

	public void setGoldpayUserDTO(GoldpayUserDTO goldpayUserDTO) {
		this.goldpayUserDTO = goldpayUserDTO;
	}

}

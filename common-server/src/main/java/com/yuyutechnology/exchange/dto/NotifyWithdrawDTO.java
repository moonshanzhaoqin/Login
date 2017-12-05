package com.yuyutechnology.exchange.dto;

import java.math.BigDecimal;
import java.util.Date;

public class NotifyWithdrawDTO {
	private int userId;
	private String areaCode;
	private String userPhone;
	private String userName;
	private String userEmail;
	private int quantity;
	private Date applyTime;
	private BigDecimal goldpay;
	private BigDecimal fee;

	public NotifyWithdrawDTO() {
	}

	public NotifyWithdrawDTO(int userId, String areaCode, String userPhone, String userName, String userEmail,
			int quantity, Date applyTime, BigDecimal goldpay, BigDecimal fee) {
		super();
		this.userId = userId;
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.userName = userName;
		this.userEmail = userEmail;
		this.quantity = quantity;
		this.applyTime = applyTime;
		this.goldpay = goldpay;
		this.fee = fee;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public BigDecimal getGoldpay() {
		return goldpay;
	}

	public void setGoldpay(BigDecimal goldpay) {
		this.goldpay = goldpay;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
}

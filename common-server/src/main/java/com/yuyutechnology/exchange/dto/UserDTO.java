/**
 * 
 */
package com.yuyutechnology.exchange.dto;

/**
 * @author suzan.wu
 *
 */
public class UserDTO {
	private Integer userId;
	private String areaCode;
	private String userPhone;
	private String userName;
	private String userPassword;
	private String passwordSalt;
	private int userAvailable;
	private Long goldpayId;
	private String goldpayUserName;
	private String goldpayAccount;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
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

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public Long getGoldpayId() {
		return goldpayId;
	}

	public void setGoldpayId(Long goldpayId) {
		this.goldpayId = goldpayId;
	}

	public String getGoldpayUserName() {
		return goldpayUserName;
	}

	public void setGoldpayUserName(String goldpayUserName) {
		this.goldpayUserName = goldpayUserName;
	}

	public String getGoldpayAccount() {
		return goldpayAccount;
	}

	public void setGoldpayAccount(String goldpayAccount) {
		this.goldpayAccount = goldpayAccount;
	}

	public int getUserAvailable() {
		return userAvailable;
	}

	public void setUserAvailable(int userAvailable) {
		this.userAvailable = userAvailable;
	}
}

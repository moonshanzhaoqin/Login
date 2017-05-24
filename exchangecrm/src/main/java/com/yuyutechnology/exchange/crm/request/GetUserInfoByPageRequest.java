/**
 * 
 */
package com.yuyutechnology.exchange.crm.request;

/**
 * @author suzan.wu
 *
 */
public class GetUserInfoByPageRequest {
	private String currentPage;
	private String userPhone;
	private String userName;

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
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
}

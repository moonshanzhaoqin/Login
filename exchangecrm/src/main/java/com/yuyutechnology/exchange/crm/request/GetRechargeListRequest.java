/**
 * 
 */
package com.yuyutechnology.exchange.crm.request;

/**
 * @author suzan.wu
 *
 */
public class GetRechargeListRequest {
	private String currentPage;
	private String userPhone;
	private String lowerAmount;
	private String upperAmount;
	private String startTime;
	private String endTime;
	private String transferType;

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

	public String getLowerAmount() {
		return lowerAmount;
	}

	public void setLowerAmount(String lowerAmount) {
		this.lowerAmount = lowerAmount;
	}

	public String getUpperAmount() {
		return upperAmount;
	}

	public void setUpperAmount(String upperAmount) {
		this.upperAmount = upperAmount;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	@Override
	public String toString() {
		return "GetRechargeListRequest [currentPage=" + currentPage + ", startTime=" + startTime + ", endTime="
				+ endTime + ", transferType=" + transferType + "]";
	}

}

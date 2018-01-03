/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.request;

import org.apache.commons.lang.StringUtils;

/**
 * @author suzan.wu
 *
 */
public class CollectRequest extends BaseRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6489383585240013681L;
	private String areaCode;
	private String userPhone;
	private String inviterCode;
	private String sharePath;

	@Override
	public String toString() {
		return "CollectRequest [areaCode=" + areaCode + ", userPhone=" + userPhone + ", inviterCode=" + inviterCode
				+ ", sharePath=" + sharePath + "]";
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

	public String getInviterCode() {
		return inviterCode;
	}

	public void setInviterCode(String inviterCode) {
		this.inviterCode = inviterCode;
	}

	public String getSharePath() {
		return sharePath;
	}

	public void setSharePath(String sharePath) {
		this.sharePath = sharePath;
	}

	/**
	 * @return
	 */
	public boolean empty() {
		if (StringUtils.isBlank(this.areaCode)) {
			return true;
		}
		if (StringUtils.isBlank(this.userPhone)) {
			return true;
		}
		if (StringUtils.isBlank(this.inviterCode)) {
			return true;
		}
		if (StringUtils.isBlank(this.sharePath)) {
			return true;
		}
		return false;
	}
}

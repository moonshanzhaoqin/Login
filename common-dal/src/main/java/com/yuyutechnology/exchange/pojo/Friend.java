package com.yuyutechnology.exchange.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "friend", catalog = "anytime_exchange")
public class Friend {
	private Integer ufId;
	private Integer userId;
	private Integer friendId;
	private String areaCode;
	private String userPhone;
	private String userName;

	public Friend() {
	}

	public Friend(Integer userId, Integer friendId, String areaCode, String userPhone, String userName) {
		super();
		this.userId = userId;
		this.friendId = friendId;
		this.areaCode = areaCode;
		this.userPhone = userPhone;
		this.userName = userName;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "uf_id", unique = true, nullable = false)
	public Integer getUfId() {
		return ufId;
	}

	public void setUfId(Integer ufId) {
		this.ufId = ufId;
	}

	@Column(name = "user_id", nullable = false)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "friend_id", nullable = false)
	public Integer getFriendId() {
		return friendId;
	}

	public void setFriendId(Integer friendId) {
		this.friendId = friendId;
	}

	@Column(name = "area_code", nullable = false)
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "user_phone", nullable = false)
	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	@Column(name = "user_name")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}

package com.yuyutechnology.exchange.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "e_withdraw")
public class Withdraw implements java.io.Serializable {

	private Integer withdrawId;
	private int userId;
	private int quantity;
	private Date applyTime;
	private byte handleResult;
	private String handler;
	private Date handleTime;

	public Withdraw() {
	}

	public Withdraw(int userId, int quantity, Date applyTime, byte handleResult) {
		this.userId = userId;
		this.quantity = quantity;
		this.applyTime = applyTime;
		this.handleResult = handleResult;
	}

	public Withdraw(int userId, int quantity, Date applyTime, byte handleResult, String handler, Date handleTime) {
		this.userId = userId;
		this.quantity = quantity;
		this.applyTime = applyTime;
		this.handleResult = handleResult;
		this.handler = handler;
		this.handleTime = handleTime;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "withdraw_id", unique = true, nullable = false)
	public Integer getWithdrawId() {
		return this.withdrawId;
	}

	public void setWithdrawId(Integer withdrawId) {
		this.withdrawId = withdrawId;
	}

	@Column(name = "user_id", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "quantity", nullable = false)
	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "apply_time", nullable = false, length = 19)
	public Date getApplyTime() {
		return this.applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	@Column(name = "handle_result", nullable = false)
	public byte getHandleResult() {
		return this.handleResult;
	}

	public void setHandleResult(byte handleResult) {
		this.handleResult = handleResult;
	}

	@Column(name = "handler")
	public String getHandler() {
		return this.handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "handle_time", length = 19)
	public Date getHandleTime() {
		return this.handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

}

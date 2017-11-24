package com.yuyutechnology.exchange.pojo;

import java.math.BigDecimal;
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
	private String userEmail;
	private int quantity;
	private BigDecimal goldpay;
	private BigDecimal fee;
	private Date applyTime;
	private byte handleResult;
	private String handler;
	private Date handleTime;
	private String goldTransferA;
	private String feeTransferA;
	private String goldTransferB;
	private String feeTransferB;

	public Withdraw() {
	}

	public Withdraw(int userId, String userEmail, int quantity, BigDecimal goldpay, BigDecimal fee, Date applyTime) {
		this.userId = userId;
		this.userEmail = userEmail;
		this.quantity = quantity;
		this.goldpay = goldpay;
		this.fee = fee;
		this.applyTime = applyTime;
	}

	public Withdraw(int userId, String userEmail, int quantity, BigDecimal goldpay, BigDecimal fee, Date applyTime,
			byte handleResult, String handler, Date handleTime, String goldTransferA, String feeTransferA,
			String goldTransferB, String feeTransferB) {
		this.userId = userId;
		this.userEmail = userEmail;
		this.quantity = quantity;
		this.goldpay = goldpay;
		this.fee = fee;
		this.applyTime = applyTime;
		this.handleResult = handleResult;
		this.handler = handler;
		this.handleTime = handleTime;
		this.goldTransferA = goldTransferA;
		this.feeTransferA = feeTransferA;
		this.goldTransferB = goldTransferB;
		this.feeTransferB = feeTransferB;
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

	@Column(name = "user_email", nullable = false)
	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Column(name = "quantity", nullable = false)
	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Column(name = "goldpay", nullable = false, precision = 20, scale = 0)
	public BigDecimal getGoldpay() {
		return this.goldpay;
	}

	public void setGoldpay(BigDecimal goldpay) {
		this.goldpay = goldpay;
	}

	@Column(name = "fee", nullable = false, precision = 20, scale = 0)
	public BigDecimal getFee() {
		return this.fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
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

	@Column(name = "gold_transfer_a")
	public String getGoldTransferA() {
		return this.goldTransferA;
	}

	public void setGoldTransferA(String goldTransferA) {
		this.goldTransferA = goldTransferA;
	}

	@Column(name = "fee_transfer_a")
	public String getFeeTransferA() {
		return this.feeTransferA;
	}

	public void setFeeTransferA(String feeTransferA) {
		this.feeTransferA = feeTransferA;
	}

	@Column(name = "gold_transfer_b")
	public String getGoldTransferB() {
		return this.goldTransferB;
	}

	public void setGoldTransferB(String goldTransferB) {
		this.goldTransferB = goldTransferB;
	}

	@Column(name = "fee_transfer_b")
	public String getFeeTransferB() {
		return this.feeTransferB;
	}

	public void setFeeTransferB(String feeTransferB) {
		this.feeTransferB = feeTransferB;
	}
}

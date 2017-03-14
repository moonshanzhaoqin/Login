package com.yuyutechnology.exchange.pojo;
// Generated Mar 7, 2017 5:51:42 PM by Hibernate Tools 3.5.0.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Withdraw generated by hbm2java
 */
@Entity
@Table(name = "e_withdraw")
public class Withdraw implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9028304727043517316L;
	private Integer withdrawId;
	private int userId;
	private String transferId;
	private int reviewStatus;
	private int goldpayRemit;

	public Withdraw() {
	}

	public Withdraw(int userId, String transferId, int reviewStatus, int goldpayRemit) {
		this.userId = userId;
		this.transferId = transferId;
		this.reviewStatus = reviewStatus;
		this.goldpayRemit = goldpayRemit;
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

	@Column(name = "transfer_id", nullable = false)
	public String getTransferId() {
		return this.transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	@Column(name = "review_status", nullable = false)
	public int getReviewStatus() {
		return this.reviewStatus;
	}

	public void setReviewStatus(int reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	@Column(name = "goldpay_remit", nullable = false)
	public int getGoldpayRemit() {
		return this.goldpayRemit;
	}

	public void setGoldpayRemit(int goldpayRemit) {
		this.goldpayRemit = goldpayRemit;
	}

}
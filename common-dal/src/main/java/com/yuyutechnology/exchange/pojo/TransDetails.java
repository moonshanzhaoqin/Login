package com.yuyutechnology.exchange.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "e_trans_details")
public class TransDetails {
	
	private Integer detailsId;
	private String transferId;
	private Integer userId;
	private String traderName;
	private String traderAreaCode;
	private String traderPhone;
	private String transCurrency;
	private BigDecimal transAmount;
	private String transRemarks;
	private Date detailsCreateTime;
	
	public TransDetails() {
		super();
	}
	
	public TransDetails(String transferId, Integer userId, String traderName, String traderAreaCode, String traderPhone,
			String transCurrency, BigDecimal transAmount, String transRemarks) {
		super();
		this.transferId = transferId;
		this.userId = userId;
		this.traderName = traderName;
		this.traderAreaCode = traderAreaCode;
		this.traderPhone = traderPhone;
		this.transCurrency = transCurrency;
		this.transAmount = transAmount;
		this.transRemarks = transRemarks;
		this.detailsCreateTime = new Date();
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "details_id", unique = true, nullable = false)
	public Integer getDetailsId() {
		return detailsId;
	}
	public void setDetailsId(Integer detailsId) {
		this.detailsId = detailsId;
	}
	
	@Column(name = "transfer_id", nullable = false, length = 100)
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	
	@Column(name = "user_id")
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@Column(name = "trader_name")
	public String getTraderName() {
		return traderName;
	}
	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}
	
	@Column(name = "trader_area_code")
	public String getTraderAreaCode() {
		return traderAreaCode;
	}
	public void setTraderAreaCode(String traderAreaCode) {
		this.traderAreaCode = traderAreaCode;
	}
	
	@Column(name = "trader_phone")
	public String getTraderPhone() {
		return traderPhone;
	}
	public void setTraderPhone(String traderPhone) {
		this.traderPhone = traderPhone;
	}
	
	@Column(name = "trans_currency")
	public String getTransCurrency() {
		return transCurrency;
	}
	public void setTransCurrency(String transCurrency) {
		this.transCurrency = transCurrency;
	}
	
	@Column(name = "trans_amount")
	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	
	@Column(name = "trans_remarks")
	public String getTransRemarks() {
		return transRemarks;
	}
	public void setTransRemarks(String transRemarks) {
		this.transRemarks = transRemarks;
	}

	@Column(name = "details_create_time")
	public Date getDetailsCreateTime() {
		return detailsCreateTime;
	}

	public void setDetailsCreateTime(Date detailsCreateTime) {
		this.detailsCreateTime = detailsCreateTime;
	}
	
	
	

}

package com.yuyutechnology.exchange.pojo;
// Generated Dec 2, 2016 4:27:04 PM by Hibernate Tools 4.0.0

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Currency generated by hbm2java
 */
@Entity
@Table(name = "currency", catalog = "anytime_exchange")
public class Currency implements java.io.Serializable {

	private String currency;
	private String nameEn;
	private String nameCn;
	private String nameHk;
	private String currencyImage;
	private String currencyStatus;
	private BigDecimal transferMax;
	private BigDecimal transferLarge;
	private BigDecimal assetThreshold;

	public Currency() {
	}

	public Currency(String currency) {
		this.currency = currency;
	}

	public Currency(String currency, BigDecimal transferMax, BigDecimal transferLarge) {
		this.currency = currency;
		this.transferMax = transferMax;
		this.transferLarge = transferLarge;
	}

	@Id
	@Column(name = "currency", unique = true, nullable = false, length = 3)
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "name_en")
	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	@Column(name = "name_cn")
	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}
	@Column(name = "name_hk")
	public String getNameHk() {
		return nameHk;
	}

	public void setNameHk(String nameHk) {
		this.nameHk = nameHk;
	}
	@Column(name = "currency_image")
	public String getCurrencyImage() {
		return currencyImage;
	}
	
	public void setCurrencyImage(String currencyImage) {
		this.currencyImage = currencyImage;
	}
	
	@Column(name = "currency_status")
	public String getCurrencyStatus() {
		return currencyStatus;
	}

	public void setCurrencyStatus(String currencyStatus) {
		this.currencyStatus = currencyStatus;
	}

	@Column(name = "asset_threshold", precision = 10)
	public BigDecimal getAssetThreshold() {
		return assetThreshold;
	}

	public void setAssetThreshold(BigDecimal assetThreshold) {
		this.assetThreshold = assetThreshold;
	}

	@Column(name = "transfer_max", precision = 10)
	public BigDecimal getTransferMax() {
		return this.transferMax;
	}

	public void setTransferMax(BigDecimal transferMax) {
		this.transferMax = transferMax;
	}

	@Column(name = "transfer_large", precision = 10)
	public BigDecimal getTransferLarge() {
		return this.transferLarge;
	}

	public void setTransferLarge(BigDecimal transferLarge) {
		this.transferLarge = transferLarge;
	}

}

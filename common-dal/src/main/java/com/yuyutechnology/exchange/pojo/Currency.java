package com.yuyutechnology.exchange.pojo;
// Generated Dec 17, 2016 2:05:54 PM by Hibernate Tools 5.1.0.Alpha1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Currency generated by hbm2java
 */
@Entity
@Table(name = "e_currency")
public class Currency implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5962261170598609844L;
	private String currency;
	private String currencyUnit;
	private String nameEn;
	private String nameCn;
	private String nameHk;
	private int currencyStatus;
	private int currencyOrder;

	public Currency() {
	}

	public Currency(String currency, int currencyStatus) {
		this.currency = currency;
		this.currencyStatus = currencyStatus;
	}

	public Currency(String currency, String currencyUnit, String nameEn, String nameCn, String nameHk,
			int currencyStatus, int currencyOrder) {
		this.currency = currency;
		this.currencyUnit = currencyUnit;
		this.nameEn = nameEn;
		this.nameCn = nameCn;
		this.nameHk = nameHk;
		this.currencyStatus = currencyStatus;
		this.currencyOrder = currencyOrder;
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
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	@Column(name = "name_cn")
	public String getNameCn() {
		return this.nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	@Column(name = "name_hk")
	public String getNameHk() {
		return this.nameHk;
	}

	public void setNameHk(String nameHk) {
		this.nameHk = nameHk;
	}

	@Column(name = "currency_status", nullable = false)
	public int getCurrencyStatus() {
		return this.currencyStatus;
	}

	public void setCurrencyStatus(int currencyStatus) {
		this.currencyStatus = currencyStatus;
	}

	@Column(name = "currency_order", unique = true)
	public int getCurrencyOrder() {
		return this.currencyOrder;
	}

	public void setCurrencyOrder(int currencyOrder) {
		this.currencyOrder = currencyOrder;
	}

	@Column(name = "currency_unit", unique = true)
	public String getCurrencyUnit() {
		return currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Currency other = (Currency) obj;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Currency [currency=" + currency + ", currencyUnit=" + currencyUnit + ", nameEn=" + nameEn + ", nameCn="
				+ nameCn + ", nameHk=" + nameHk + ", currencyStatus=" + currencyStatus + ", currencyOrder="
				+ currencyOrder + "]";
	}
}

package com.yuyutechnology.exchange.crm.request;

public class CurrencyRequest {
	private String currency;
	private String currencyUnit;
	private String nameEn;
	private String nameCn;
	private String nameHk;
	private String currencyStatus;
	private String currencyOrder;

	public String getCurrencyUnit() {
		return currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	public String getNameEn() {
		return nameEn;
	}

	@Override
	public String toString() {
		return "CurrencyRequest [currency=" + currency + ", currencyUnit=" + currencyUnit + ", nameEn=" + nameEn
				+ ", nameCn=" + nameCn + ", nameHk=" + nameHk + ", currencyStatus=" + currencyStatus
				+ ", currencyOrder=" + currencyOrder + "]";
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getNameHk() {
		return nameHk;
	}

	public void setNameHk(String nameHk) {
		this.nameHk = nameHk;
	}


	public String getCurrencyStatus() {
		return currencyStatus;
	}

	public void setCurrencyStatus(String currencyStatus) {
		this.currencyStatus = currencyStatus;
	}

	public String getCurrencyOrder() {
		return currencyOrder;
	}

	public void setCurrencyOrder(String currencyOrder) {
		this.currencyOrder = currencyOrder;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}

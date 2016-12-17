package com.yuyutechnology.exchange.dto;

public class CurrencyInfo {
	private String currency;
	private String nameEn;
	private String nameCn;
	private String nameHk;
	private String currencyImage;
	private int currencyStatus;

	public CurrencyInfo() {
	}

	public CurrencyInfo(String currency, String nameEn, String nameCn, String nameHk, 
			int currencyStatus) {
		super();
		this.currency = currency;
		this.nameEn = nameEn;
		this.nameCn = nameCn;
		this.nameHk = nameHk;
		this.currencyStatus = currencyStatus;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getNameEn() {
		return nameEn;
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

	public String getCurrencyImage() {
		return currencyImage;
	}

	public void setCurrencyImage(String currencyImage) {
		this.currencyImage = currencyImage;
	}

	public int getCurrencyStatus() {
		return currencyStatus;
	}

	public void setCurrencyStatus(int currencyStatus) {
		this.currencyStatus = currencyStatus;
	}
}

package com.yuyutechnology.exchange.server.controller.response;

import java.math.BigDecimal;
import java.util.Date;

import com.yuyutechnology.exchange.dto.TransDetailsDTO;
import com.yuyutechnology.exchange.util.MathUtils;

public class GetTransDetailsResponse extends BaseResponse {

	private String trader;
	private String areaCode;
	private String phone;
	private String currency;
	private BigDecimal amount;
	private String paypalCurrency;
	private BigDecimal paypalExchange;
	private String unit;
	private int transferType;
	private Date createTime;
	private Date finishTime;
	private String transferId;
	private String goldpayName;
	private String transferComment;

	private boolean isFriend;
	private boolean isRegiste;

	public GetTransDetailsResponse() {
		super();
	}

	public GetTransDetailsResponse(String trader, String areaCode, String phone, String currency, BigDecimal amount,
			String paypalCurrency, BigDecimal paypalExchange, String unit, int transferType, Date createTime,
			Date finishTime, String transferId, String goldpayName, String transferComment, boolean isFriend,
			boolean isRegiste) {
		super();
		this.trader = trader;
		this.areaCode = areaCode;
		this.phone = phone;
		this.currency = currency;
		this.amount = amount;
		this.paypalCurrency = paypalCurrency;
		this.paypalExchange = paypalExchange;
		this.unit = unit;
		this.transferType = transferType;
		this.createTime = createTime;
		this.finishTime = finishTime;
		this.transferId = transferId;
		this.goldpayName = goldpayName;
		this.transferComment = transferComment;
		this.isFriend = isFriend;
		this.isRegiste = isRegiste;
	}

	public GetTransDetailsResponse(TransDetailsDTO dto) {
		super();
		this.trader = dto.getTraderName();
		this.areaCode = dto.getTraderAreaCode();
		this.phone = dto.getTraderPhone();
		this.currency = dto.getTransCurrency();
		this.amount = dto.getTransAmount();
		this.paypalCurrency = dto.getPaypalCurrency();
		this.paypalExchange = dto.getPaypalExchange();
		this.unit = dto.getTransUnit();
		this.transferType = dto.getTransAmount().compareTo(BigDecimal.ZERO)>=0 && dto.getTransType()==0?1:dto.getTransType();
		this.createTime = dto.getCreateTime();
		this.finishTime = dto.getFinishTime();
		this.transferId = dto.getTransId();
		this.goldpayName = MathUtils.hideString(dto.getGoldpayName());
		this.transferComment = dto.getTransRemarks();
		this.isFriend = dto.isFriend();
		this.isRegiste = dto.isRegistered();
	}
	
	
	public String getTrader() {
		return trader;
	}

	public void setTrader(String trader) {
		this.trader = trader;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPaypalCurrency() {
		return paypalCurrency;
	}

	public void setPaypalCurrency(String paypalCurrency) {
		this.paypalCurrency = paypalCurrency;
	}

	public BigDecimal getPaypalExchange() {
		return paypalExchange;
	}

	public void setPaypalExchange(BigDecimal paypalExchange) {
		this.paypalExchange = paypalExchange;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getTransferType() {
		return transferType;
	}

	public void setTransferType(int transferType) {
		this.transferType = transferType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getGoldpayName() {
		return goldpayName;
	}

	public void setGoldpayName(String goldpayName) {
		this.goldpayName = goldpayName;
	}
	public String getTransferComment() {
		return transferComment;
	}

	public void setTransferComment(String transferComment) {
		this.transferComment = transferComment;
	}

	public boolean isFriend() {
		return isFriend;
	}

	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}

	public boolean isRegiste() {
		return isRegiste;
	}

	public void setRegiste(boolean isRegiste) {
		this.isRegiste = isRegiste;
	}

}

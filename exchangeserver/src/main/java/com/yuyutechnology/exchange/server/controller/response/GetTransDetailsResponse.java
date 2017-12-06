package com.yuyutechnology.exchange.server.controller.response;

import java.math.BigDecimal;
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.yuyutechnology.exchange.dto.TransDetailsDTO;
import com.yuyutechnology.exchange.util.MathUtils;

@ApiModel
public class GetTransDetailsResponse extends BaseResponse {

	private String trader;
	private String areaCode;
	private String phone;
	private String currency;
	private BigDecimal amount;
	private BigDecimal fee;
	private BigDecimal fee4Gp;
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
		this.transferType = dto.getTransAmount().compareTo(BigDecimal.ZERO) >= 0 && dto.getTransType() == 0 ? 1
				: dto.getTransType();
		this.createTime = dto.getCreateTime();
		this.finishTime = dto.getFinishTime();
		this.transferId = dto.getTransId();
		this.goldpayName = MathUtils.hideString(dto.getGoldpayName());
		this.transferComment = dto.getTransRemarks();
		this.isFriend = dto.isFriend();
		this.isRegiste = dto.isRegistered();
		this.fee = dto.getTransFee();
		this.fee4Gp = dto.getTransFee4GP();
	}

	@ApiModelProperty(required=true,value="交易方用户名")
	public String getTrader() {
		return trader;
	}

	public void setTrader(String trader) {
		this.trader = trader;
	}

	@ApiModelProperty(required=true,value="手机区号")
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@ApiModelProperty(required=true,value="手机号码")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@ApiModelProperty(required=true,value="货币类型")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@ApiModelProperty(required=true,value="金额")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@ApiModelProperty(required=true,value="手续费")
	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	@ApiModelProperty(required=true,value="")
	public BigDecimal getFee4Gp() {
		return fee4Gp;
	}

	public void setFee4Gp(BigDecimal fee4Gp) {
		this.fee4Gp = fee4Gp;
	}

	@ApiModelProperty(required=true,value="")
	public String getPaypalCurrency() {
		return paypalCurrency;
	}

	public void setPaypalCurrency(String paypalCurrency) {
		this.paypalCurrency = paypalCurrency;
	}

	@ApiModelProperty(required=true,value="")
	public BigDecimal getPaypalExchange() {
		return paypalExchange;
	}

	public void setPaypalExchange(BigDecimal paypalExchange) {
		this.paypalExchange = paypalExchange;
	}

	@ApiModelProperty(required=true,value="货币单位")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@ApiModelProperty(required=true,value="交易类型")
	public int getTransferType() {
		return transferType;
	}

	public void setTransferType(int transferType) {
		this.transferType = transferType;
	}

	@ApiModelProperty(required=true,value="创建时间")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ApiModelProperty(required=true,value="完成时间")
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@ApiModelProperty(required=true,value="订单ID")
	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	@ApiModelProperty(required=true,value="goldpay账户名")
	public String getGoldpayName() {
		return goldpayName;
	}

	public void setGoldpayName(String goldpayName) {
		this.goldpayName = goldpayName;
	}

	@ApiModelProperty(required=true,value="备注")
	public String getTransferComment() {
		return transferComment;
	}

	public void setTransferComment(String transferComment) {
		this.transferComment = transferComment;
	}
	
	@ApiModelProperty(required=true,value="交易对象是否是好友")
	public boolean isFriend() {
		return isFriend;
	}

	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}

	@ApiModelProperty(required=true,value="交易对象是否已注册")
	public boolean isRegiste() {
		return isRegiste;
	}

	public void setRegiste(boolean isRegiste) {
		this.isRegiste = isRegiste;
	}

}

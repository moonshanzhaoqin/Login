package com.yuyutechnology.exchange.utils.oanda;

import java.math.BigDecimal;

public class PriceInfo {
	
	private String instrument;
	private String time;
	private BigDecimal bid;
	private BigDecimal ask;
	private String status;
	
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public BigDecimal getBid() {
		return bid;
	}
	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}
	public BigDecimal getAsk() {
		return ask;
	}
	public void setAsk(BigDecimal ask) {
		this.ask = ask;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "PriceInfo [instrument=" + instrument + ", time=" + time + ", bid=" + bid + ", ask=" + ask + ", status="
				+ status + "]";
	}
}

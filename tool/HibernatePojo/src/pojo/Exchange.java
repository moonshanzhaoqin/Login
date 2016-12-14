package pojo;
// Generated Dec 14, 2016 6:26:12 PM by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Exchange generated by hbm2java
 */
@Entity
@Table(name = "exchange", catalog = "anytime_exchange")
public class Exchange implements java.io.Serializable {

	private String exchangeId;
	private int userId;
	private String currencyOut;
	private String currencyIn;
	private BigDecimal amountOut;
	private BigDecimal amountIn;
	private BigDecimal exchangeRate;
	private Date createTime;
	private Date finishTime;
	private int exchangeStatus;

	public Exchange() {
	}

	public Exchange(String exchangeId, int userId, String currencyOut, String currencyIn, BigDecimal amountOut,
			BigDecimal amountIn, BigDecimal exchangeRate, int exchangeStatus) {
		this.exchangeId = exchangeId;
		this.userId = userId;
		this.currencyOut = currencyOut;
		this.currencyIn = currencyIn;
		this.amountOut = amountOut;
		this.amountIn = amountIn;
		this.exchangeRate = exchangeRate;
		this.exchangeStatus = exchangeStatus;
	}

	public Exchange(String exchangeId, int userId, String currencyOut, String currencyIn, BigDecimal amountOut,
			BigDecimal amountIn, BigDecimal exchangeRate, Date createTime, Date finishTime, int exchangeStatus) {
		this.exchangeId = exchangeId;
		this.userId = userId;
		this.currencyOut = currencyOut;
		this.currencyIn = currencyIn;
		this.amountOut = amountOut;
		this.amountIn = amountIn;
		this.exchangeRate = exchangeRate;
		this.createTime = createTime;
		this.finishTime = finishTime;
		this.exchangeStatus = exchangeStatus;
	}

	@Id

	@Column(name = "exchange_id", unique = true, nullable = false)
	public String getExchangeId() {
		return this.exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

	@Column(name = "user_id", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "currency_out", nullable = false, length = 3)
	public String getCurrencyOut() {
		return this.currencyOut;
	}

	public void setCurrencyOut(String currencyOut) {
		this.currencyOut = currencyOut;
	}

	@Column(name = "currency_in", nullable = false, length = 3)
	public String getCurrencyIn() {
		return this.currencyIn;
	}

	public void setCurrencyIn(String currencyIn) {
		this.currencyIn = currencyIn;
	}

	@Column(name = "amount_out", nullable = false, precision = 10)
	public BigDecimal getAmountOut() {
		return this.amountOut;
	}

	public void setAmountOut(BigDecimal amountOut) {
		this.amountOut = amountOut;
	}

	@Column(name = "amount_in", nullable = false, precision = 10)
	public BigDecimal getAmountIn() {
		return this.amountIn;
	}

	public void setAmountIn(BigDecimal amountIn) {
		this.amountIn = amountIn;
	}

	@Column(name = "exchange_rate", nullable = false, precision = 10, scale = 4)
	public BigDecimal getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "finish_time", length = 19)
	public Date getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Column(name = "exchange_status", nullable = false)
	public int getExchangeStatus() {
		return this.exchangeStatus;
	}

	public void setExchangeStatus(int exchangeStatus) {
		this.exchangeStatus = exchangeStatus;
	}

}

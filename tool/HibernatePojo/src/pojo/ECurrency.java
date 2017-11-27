package pojo;
// Generated Nov 27, 2017 12:17:17 PM by Hibernate Tools 5.2.6.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ECurrency generated by hbm2java
 */
@Entity
@Table(name = "e_currency", catalog = "anytime_exchange")
public class ECurrency implements java.io.Serializable {

	private String currency;
	private String nameEn;
	private String nameCn;
	private String nameHk;
	private String currencyUnit;
	private int currencyStatus;
	private Integer currencyOrder;

	public ECurrency() {
	}

	public ECurrency(String currency, int currencyStatus) {
		this.currency = currency;
		this.currencyStatus = currencyStatus;
	}

	public ECurrency(String currency, String nameEn, String nameCn, String nameHk, String currencyUnit,
			int currencyStatus, Integer currencyOrder) {
		this.currency = currency;
		this.nameEn = nameEn;
		this.nameCn = nameCn;
		this.nameHk = nameHk;
		this.currencyUnit = currencyUnit;
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

	@Column(name = "currency_unit")
	public String getCurrencyUnit() {
		return this.currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	@Column(name = "currency_status", nullable = false)
	public int getCurrencyStatus() {
		return this.currencyStatus;
	}

	public void setCurrencyStatus(int currencyStatus) {
		this.currencyStatus = currencyStatus;
	}

	@Column(name = "currency_order")
	public Integer getCurrencyOrder() {
		return this.currencyOrder;
	}

	public void setCurrencyOrder(Integer currencyOrder) {
		this.currencyOrder = currencyOrder;
	}

}

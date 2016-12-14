package pojo;
// Generated Dec 14, 2016 6:26:12 PM by Hibernate Tools 4.0.0

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
	private int currencyStutas;
	private BigDecimal transferMax;
	private BigDecimal transferLarge;
	private BigDecimal assetThreshold;

	public Currency() {
	}

	public Currency(String currency, int currencyStutas) {
		this.currency = currency;
		this.currencyStutas = currencyStutas;
	}

	public Currency(String currency, String nameEn, String nameCn, String nameHk, String currencyImage,
			int currencyStutas, BigDecimal transferMax, BigDecimal transferLarge, BigDecimal assetThreshold) {
		this.currency = currency;
		this.nameEn = nameEn;
		this.nameCn = nameCn;
		this.nameHk = nameHk;
		this.currencyImage = currencyImage;
		this.currencyStutas = currencyStutas;
		this.transferMax = transferMax;
		this.transferLarge = transferLarge;
		this.assetThreshold = assetThreshold;
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

	@Column(name = "currency_image")
	public String getCurrencyImage() {
		return this.currencyImage;
	}

	public void setCurrencyImage(String currencyImage) {
		this.currencyImage = currencyImage;
	}

	@Column(name = "currency_stutas", nullable = false)
	public int getCurrencyStutas() {
		return this.currencyStutas;
	}

	public void setCurrencyStutas(int currencyStutas) {
		this.currencyStutas = currencyStutas;
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

	@Column(name = "asset_threshold", precision = 10)
	public BigDecimal getAssetThreshold() {
		return this.assetThreshold;
	}

	public void setAssetThreshold(BigDecimal assetThreshold) {
		this.assetThreshold = assetThreshold;
	}

}

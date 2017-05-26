package pojo;
// Generated May 25, 2017 5:41:24 PM by Hibernate Tools 5.2.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EAppVersion generated by hbm2java
 */
@Entity
@Table(name = "e_app_version", catalog = "anytime_exchange")
public class EAppVersion implements java.io.Serializable {

	private Long id;
	private String appVersionNum;
	private String platformType;
	private String updateContentCn;
	private String updateContentEn;
	private String updateContentHk;
	private String updateWay;
	private String updateLink;
	private Integer isMustUpdated;
	private String publisher;
	private Date releaseTime;

	public EAppVersion() {
	}

	public EAppVersion(String appVersionNum, String platformType) {
		this.appVersionNum = appVersionNum;
		this.platformType = platformType;
	}

	public EAppVersion(String appVersionNum, String platformType, String updateContentCn, String updateContentEn,
			String updateContentHk, String updateWay, String updateLink, Integer isMustUpdated, String publisher,
			Date releaseTime) {
		this.appVersionNum = appVersionNum;
		this.platformType = platformType;
		this.updateContentCn = updateContentCn;
		this.updateContentEn = updateContentEn;
		this.updateContentHk = updateContentHk;
		this.updateWay = updateWay;
		this.updateLink = updateLink;
		this.isMustUpdated = isMustUpdated;
		this.publisher = publisher;
		this.releaseTime = releaseTime;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "appVersionNum", nullable = false, length = 30)
	public String getAppVersionNum() {
		return this.appVersionNum;
	}

	public void setAppVersionNum(String appVersionNum) {
		this.appVersionNum = appVersionNum;
	}

	@Column(name = "platformType", nullable = false, length = 1)
	public String getPlatformType() {
		return this.platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	@Column(name = "updateContent_cn", length = 65535)
	public String getUpdateContentCn() {
		return this.updateContentCn;
	}

	public void setUpdateContentCn(String updateContentCn) {
		this.updateContentCn = updateContentCn;
	}

	@Column(name = "updateContent_en", length = 65535)
	public String getUpdateContentEn() {
		return this.updateContentEn;
	}

	public void setUpdateContentEn(String updateContentEn) {
		this.updateContentEn = updateContentEn;
	}

	@Column(name = "updateContent_hk", length = 65535)
	public String getUpdateContentHk() {
		return this.updateContentHk;
	}

	public void setUpdateContentHk(String updateContentHk) {
		this.updateContentHk = updateContentHk;
	}

	@Column(name = "updateWay", length = 500)
	public String getUpdateWay() {
		return this.updateWay;
	}

	public void setUpdateWay(String updateWay) {
		this.updateWay = updateWay;
	}

	@Column(name = "updateLink", length = 500)
	public String getUpdateLink() {
		return this.updateLink;
	}

	public void setUpdateLink(String updateLink) {
		this.updateLink = updateLink;
	}

	@Column(name = "isMustUpdated")
	public Integer getIsMustUpdated() {
		return this.isMustUpdated;
	}

	public void setIsMustUpdated(Integer isMustUpdated) {
		this.isMustUpdated = isMustUpdated;
	}

	@Column(name = "publisher")
	public String getPublisher() {
		return this.publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "releaseTime", length = 19)
	public Date getReleaseTime() {
		return this.releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

}

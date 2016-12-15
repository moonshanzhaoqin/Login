package com.yuyutechnology.exchange.pojo;
// Generated Dec 15, 2016 7:25:59 PM by Hibernate Tools 5.1.0.Alpha1

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
 * AppVersion generated by hbm2java
 */
@Entity
@Table(name = "app_version", catalog = "anytime_exchange")
public class AppVersion implements java.io.Serializable {

	private Long id;
	private String appVersionNum;
	private String platformType;
	private String updateContent;
	private String updateWay;
	private String updateLink;
	private Integer isMustUpdated;
	private String publisher;
	private Date releaseTime;

	public AppVersion() {
	}

	public AppVersion(String appVersionNum, String platformType) {
		this.appVersionNum = appVersionNum;
		this.platformType = platformType;
	}

	public AppVersion(String appVersionNum, String platformType, String updateContent, String updateWay,
			String updateLink, Integer isMustUpdated, String publisher, Date releaseTime) {
		this.appVersionNum = appVersionNum;
		this.platformType = platformType;
		this.updateContent = updateContent;
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

	@Column(name = "updateContent", length = 65535)
	public String getUpdateContent() {
		return this.updateContent;
	}

	public void setUpdateContent(String updateContent) {
		this.updateContent = updateContent;
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

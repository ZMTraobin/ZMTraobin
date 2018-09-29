package com.cmig.future.csportal.module.kpi.dto;

/**
 * Auto Generated By Hap Code Generator
 **/

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.hap.core.BaseConstants;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@ExtensionAttribute(disable = true)
@Table(name = "csp_ljh_kpi_result")
public class LjhKpiResult extends BaseExtDTO {
	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty
	private String appName;

	@DateTimeFormat(pattern =BaseConstants.DATE_FORMAT )
	private Date kpiDate;

	private Long pageView;

	private Long uniqueVisitor;

	private Long internetProtocolNum;

	private Double outPercent;

	private Double accessSecondsAverage;

	private Double pageViewAverage;

	private Double newVisitorPercent;

	private Double oldVisitorPercent;

	private Long newVisitorPageView;

	private Long oldVisitorPageView;

	private Long newVisitor;

	private Long oldVisitor;

	private Double newVisitorOutPercent;

	private Double oldVisitorOutPercent;

	private Double newVisitorAccessSecondsAverage;

	private Double oldVisitorAccessSecondsAverage;

	private Double newVisitorPageViewAverage;

	private Double oldVisitorPageViewAverage;

	@Transient
	@JsonFormat(pattern = BaseConstants.DATE_FORMAT)
	@DateTimeFormat(pattern = BaseConstants.DATE_TIME_FORMAT)
	private Date startDate;

	@Transient
	@JsonFormat(pattern = BaseConstants.DATE_FORMAT)
	@DateTimeFormat(pattern = BaseConstants.DATE_TIME_FORMAT)
	private Date endDate;

	private Date creationDate;
	@JsonFormat(pattern = BaseConstants.DATE_TIME_FORMAT)
	private Date lastUpdateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Date getKpiDate() {
		return kpiDate;
	}

	public void setKpiDate(Date kpiDate) {
		this.kpiDate = kpiDate;
	}

	public Long getPageView() {
		return pageView;
	}

	public void setPageView(Long pageView) {
		this.pageView = pageView;
	}

	public Long getUniqueVisitor() {
		return uniqueVisitor;
	}

	public void setUniqueVisitor(Long uniqueVisitor) {
		this.uniqueVisitor = uniqueVisitor;
	}

	public Long getInternetProtocolNum() {
		return internetProtocolNum;
	}

	public void setInternetProtocolNum(Long internetProtocolNum) {
		this.internetProtocolNum = internetProtocolNum;
	}

	public Double getOutPercent() {
		return outPercent;
	}

	public void setOutPercent(Double outPercent) {
		this.outPercent = outPercent;
	}

	public Double getAccessSecondsAverage() {
		return accessSecondsAverage;
	}

	public void setAccessSecondsAverage(Double accessSecondsAverage) {
		this.accessSecondsAverage = accessSecondsAverage;
	}

	public Double getPageViewAverage() {
		return pageViewAverage;
	}

	public void setPageViewAverage(Double pageViewAverage) {
		this.pageViewAverage = pageViewAverage;
	}

	public Double getNewVisitorPercent() {
		return newVisitorPercent;
	}

	public void setNewVisitorPercent(Double newVisitorPercent) {
		this.newVisitorPercent = newVisitorPercent;
	}

	public Double getOldVisitorPercent() {
		return oldVisitorPercent;
	}

	public void setOldVisitorPercent(Double oldVisitorPercent) {
		this.oldVisitorPercent = oldVisitorPercent;
	}

	public Long getNewVisitorPageView() {
		return newVisitorPageView;
	}

	public void setNewVisitorPageView(Long newVisitorPageView) {
		this.newVisitorPageView = newVisitorPageView;
	}

	public Long getOldVisitorPageView() {
		return oldVisitorPageView;
	}

	public void setOldVisitorPageView(Long oldVisitorPageView) {
		this.oldVisitorPageView = oldVisitorPageView;
	}

	public Long getNewVisitor() {
		return newVisitor;
	}

	public void setNewVisitor(Long newVisitor) {
		this.newVisitor = newVisitor;
	}

	public Long getOldVisitor() {
		return oldVisitor;
	}

	public void setOldVisitor(Long oldVisitor) {
		this.oldVisitor = oldVisitor;
	}

	public Double getNewVisitorOutPercent() {
		return newVisitorOutPercent;
	}

	public void setNewVisitorOutPercent(Double newVisitorOutPercent) {
		this.newVisitorOutPercent = newVisitorOutPercent;
	}

	public Double getOldVisitorOutPercent() {
		return oldVisitorOutPercent;
	}

	public void setOldVisitorOutPercent(Double oldVisitorOutPercent) {
		this.oldVisitorOutPercent = oldVisitorOutPercent;
	}

	public Double getNewVisitorAccessSecondsAverage() {
		return newVisitorAccessSecondsAverage;
	}

	public void setNewVisitorAccessSecondsAverage(Double newVisitorAccessSecondsAverage) {
		this.newVisitorAccessSecondsAverage = newVisitorAccessSecondsAverage;
	}

	public Double getOldVisitorAccessSecondsAverage() {
		return oldVisitorAccessSecondsAverage;
	}

	public void setOldVisitorAccessSecondsAverage(Double oldVisitorAccessSecondsAverage) {
		this.oldVisitorAccessSecondsAverage = oldVisitorAccessSecondsAverage;
	}

	public Double getNewVisitorPageViewAverage() {
		return newVisitorPageViewAverage;
	}

	public void setNewVisitorPageViewAverage(Double newVisitorPageViewAverage) {
		this.newVisitorPageViewAverage = newVisitorPageViewAverage;
	}

	public Double getOldVisitorPageViewAverage() {
		return oldVisitorPageViewAverage;
	}

	public void setOldVisitorPageViewAverage(Double oldVisitorPageViewAverage) {
		this.oldVisitorPageViewAverage = oldVisitorPageViewAverage;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	@Transient
	private String kpiDateStr;
	public String getKpiDateStr() {
		if(null!=this.kpiDate) {
			return DateUtils.formatDate(this.kpiDate, "yyyy-MM-dd");
		}
		return null;
	}

	public void setKpiDateStr(String kpiDateStr) {
		this.kpiDateStr = kpiDateStr;
	}
}
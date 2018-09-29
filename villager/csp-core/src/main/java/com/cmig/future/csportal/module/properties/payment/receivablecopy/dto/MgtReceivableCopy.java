package com.cmig.future.csportal.module.properties.payment.receivablecopy.dto;

/**Auto Generated By Hap Code Generator**/
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "csp_mgt_receivable_copy")
public class MgtReceivableCopy extends BaseDTO {
     @Id
     @GeneratedValue
      private Long receivableId;

     @NotEmpty
      private String buildingType;

     @NotNull
      private String sourceBuildCode;

     @NotEmpty
      private String expenditure;

     @NotEmpty
      private String periodName;

     @NotNull
      private Long totalAmount;

     @NotNull
      private Long discountAmount;

     @NotNull
      private Long breakContractAmount;

     @NotNull
      private Long receivableAmount;

      private Date paidTime;

      private String backUrl;

      private String description;

     @NotEmpty
      private String notifyFlag;

     @NotEmpty
      private String checkFlag;

     @NotEmpty
      private String payStatus;

     @NotEmpty
      private String enableFlag;

      private String appId;


	public Long getReceivableId() {
		return receivableId;
	}

	public void setReceivableId(Long receivableId) {
		this.receivableId = receivableId;
	}

	public String getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	public String getSourceBuildCode() {
		return sourceBuildCode;
	}

	public void setSourceBuildCode(String sourceBuildCode) {
		this.sourceBuildCode = sourceBuildCode;
	}

	public String getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(String expenditure) {
		this.expenditure = expenditure;
	}

	public String getPeriodName() {
		return periodName;
	}

	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Long discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Long getBreakContractAmount() {
		return breakContractAmount;
	}

	public void setBreakContractAmount(Long breakContractAmount) {
		this.breakContractAmount = breakContractAmount;
	}

	public Long getReceivableAmount() {
		return receivableAmount;
	}

	public void setReceivableAmount(Long receivableAmount) {
		this.receivableAmount = receivableAmount;
	}

	public Date getPaidTime() {
		return paidTime;
	}

	public void setPaidTime(Date paidTime) {
		this.paidTime = paidTime;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotifyFlag() {
		return notifyFlag;
	}

	public void setNotifyFlag(String notifyFlag) {
		this.notifyFlag = notifyFlag;
	}

	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}
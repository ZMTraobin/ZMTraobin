package com.cmig.future.csportal.module.properties.payment.receivable.dto;

/**Auto Generated By Hap Code Generator**/

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "csp_mgt_receivable_detail")
public class MgtReceivableDetail extends BaseExtDTO {
     @Id
     @GeneratedValue
      private Long receivableId;

     @NotEmpty
      private String buildingType;

     @NotNull
      private Long buildingId;

     @NotEmpty
      private String expenditure;

      private Date startDate;

      private Date endDate;

     @NotEmpty
      private String periodName;

      private Long priceAmout;

      private BigDecimal area;

     @NotNull
      private Long totalAmount;

     @NotNull
      private Long discountAmount;

     @NotNull
      private Long breakContractAmount;

     @NotNull
      private Long receivableAmount;

      private Date buildTime;

      private Date paidTime;

     @NotEmpty
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

      private String sourceSystem;

      private String sourceReceivableId;   
	  @Transient
      private String houseCode;
    
      @Transient
      private String communityName;
	  @Transient
      private String expenditureDesc;
      
      @Transient
      private Long uncalledFee;
      @Transient
      private Long payableFee;
      @Transient
      private String periodStart;
      @Transient
      private String periodEnd;
      @Transient
      private String communityId;
      @Transient
      private String houseFullName;

    public String getHouseFullName() {
        return houseFullName;
    }

    public void setHouseFullName(String houseFullName) {
        this.houseFullName = houseFullName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(String periodStart) {
        this.periodStart = periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(String periodEnd) {
        this.periodEnd = periodEnd;
    }

    public Long getUncalledFee() {
        return uncalledFee;
    }

    public void setUncalledFee(Long uncalledFee) {
        this.uncalledFee = uncalledFee;
    }

    public Long getPayableFee() {
        return payableFee;
    }

    public void setPayableFee(Long payableFee) {
        this.payableFee = payableFee;
    }

    public String getExpenditureDesc() {
        return expenditureDesc;
    }

    public void setExpenditureDesc(String expenditureDesc) {
        this.expenditureDesc = expenditureDesc;
    }

    public String getHouseCode() {
		return houseCode;
	}

	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
      
     public void setReceivableId(Long receivableId){
         this.receivableId = receivableId;
     }

     public Long getReceivableId(){
         return receivableId;
     }

     public void setBuildingType(String buildingType){
         this.buildingType = buildingType;
     }

     public String getBuildingType(){
         return buildingType;
     }

     public void setBuildingId(Long buildingId){
         this.buildingId = buildingId;
     }

     public Long getBuildingId(){
         return buildingId;
     }

     public void setExpenditure(String expenditure){
         this.expenditure = expenditure;
     }

     public String getExpenditure(){
         return expenditure;
     }

     public void setStartDate(Date startDate){
         this.startDate = startDate;
     }

     public Date getStartDate(){
         return startDate;
     }

     public void setEndDate(Date endDate){
         this.endDate = endDate;
     }

     public Date getEndDate(){
         return endDate;
     }

     public void setPeriodName(String periodName){
         this.periodName = periodName;
     }

     public String getPeriodName(){
         return periodName;
     }

     public void setPriceAmout(Long priceAmout){
         this.priceAmout = priceAmout;
     }

     public Long getPriceAmout(){
         return priceAmout;
     }

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public void setTotalAmount(Long totalAmount){
         this.totalAmount = totalAmount;
     }

     public Long getTotalAmount(){
         return totalAmount;
     }

     public void setDiscountAmount(Long discountAmount){
         this.discountAmount = discountAmount;
     }

     public Long getDiscountAmount(){
         return discountAmount;
     }

     public void setBreakContractAmount(Long breakContractAmount){
         this.breakContractAmount = breakContractAmount;
     }

     public Long getBreakContractAmount(){
         return breakContractAmount;
     }

     public void setReceivableAmount(Long receivableAmount){
         this.receivableAmount = receivableAmount;
     }

     public Long getReceivableAmount(){
         return receivableAmount;
     }

     public void setBuildTime(Date buildTime){
         this.buildTime = buildTime;
     }

     public Date getBuildTime(){
         return buildTime;
     }

     public void setPaidTime(Date paidTime){
         this.paidTime = paidTime;
     }

     public Date getPaidTime(){
         return paidTime;
     }

     public void setBackUrl(String backUrl){
         this.backUrl = backUrl;
     }

     public String getBackUrl(){
         return backUrl;
     }

     public void setDescription(String description){
         this.description = description;
     }

     public String getDescription(){
         return description;
     }

     public void setNotifyFlag(String notifyFlag){
         this.notifyFlag = notifyFlag;
     }

     public String getNotifyFlag(){
         return notifyFlag;
     }

     public void setCheckFlag(String checkFlag){
         this.checkFlag = checkFlag;
     }

     public String getCheckFlag(){
         return checkFlag;
     }

     public void setPayStatus(String payStatus){
         this.payStatus = payStatus;
     }

     public String getPayStatus(){
         return payStatus;
     }

     public void setEnableFlag(String enableFlag){
         this.enableFlag = enableFlag;
     }

     public String getEnableFlag(){
         return enableFlag;
     }

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setSourceSystem(String sourceSystem){
         this.sourceSystem = sourceSystem;
     }

     public String getSourceSystem(){
         return sourceSystem;
     }

     public void setSourceReceivableId(String sourceReceivableId){
         this.sourceReceivableId = sourceReceivableId;
     }

     public String getSourceReceivableId(){
         return sourceReceivableId;
     }

     }
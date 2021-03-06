package com.cmig.future.csportal.module.properties.base.customer.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

/**
 * Auto Generated By Hap Code Generator
 **/

@ExtensionAttribute(disable = true)
@Table(name = "csp_bp_house_map")
public class BpHouseMap extends BaseExtDTO {
    @Id
    @GeneratedValue
    private Long mapId;
    
    private String appUserId;

    @NotNull
    private String bpType;

    @NotNull
    private Long bpExtId;

    @NotNull
    private String buildingType;

    @NotNull
    private Long buildingId;

    private Date effectiveStartDate;

    private Date effectiveEndDate;

    private String status;

    private String authenticateStatus;
    
    @Transient
    private String houseCode;
    @Transient
    private Long houseId;
    @Transient
    private String houseFullName;
    @Transient
    private String houseNickName;
    @Transient
    private String communityId;
    @Transient
    private String useType;
    @Transient
    private String communityCode;
    @Transient
    private String communityName;
    @Transient
    private String companyId;
    @Transient
    private String companyName;
    @Transient
    private String sourceHouseCode;
    
    @Transient
    private String mobile;
    
    private BigDecimal buildingArea;
    private BigDecimal paymentArea;
    
    @Transient
    private String verifyCode;

    @Transient
    private String isBind;

    public String getIsBind() {
        return isBind;
    }

    public void setIsBind(String isBind) {
        this.isBind = isBind;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public BigDecimal getBuildingArea() {
        return buildingArea;
    }

    public void setBuildingArea(BigDecimal buildingArea) {
        this.buildingArea = buildingArea;
    }

    public BigDecimal getPaymentArea() {
        return paymentArea;
    }

    public void setPaymentArea(BigDecimal paymentArea) {
        this.paymentArea = paymentArea;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getSourceHouseCode() {
        return sourceHouseCode;
    }

    public void setSourceHouseCode(String sourceHouseCode) {
        this.sourceHouseCode = sourceHouseCode;
    }

    public String getCommunityCode() {
        return communityCode;
    }

    public void setCommunityCode(String communityCode) {
        this.communityCode = communityCode;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getHouseFullName() {
        return houseFullName;
    }

    public void setHouseFullName(String houseFullName) {
        this.houseFullName = houseFullName;
    }

    public String getHouseNickName() {
        return houseNickName;
    }

    public void setHouseNickName(String houseNickName) {
        this.houseNickName = houseNickName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public Long getMapId() {
        return mapId;
    }

    

    public String getBpType() {
        return bpType;
    }

    public void setBpType(String bpType) {
        this.bpType = bpType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public void setBpExtId(Long bpExtId) {
        this.bpExtId = bpExtId;
    }

    public Long getBpExtId() {
        return bpExtId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setEffectiveStartDate(Date effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public Date getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveEndDate(Date effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public Date getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setAuthenticateStatus(String authenticateStatus) {
        this.authenticateStatus = authenticateStatus;
    }

    public String getAuthenticateStatus() {
        return authenticateStatus;
    }

}
